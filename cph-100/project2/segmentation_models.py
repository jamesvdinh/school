"""
Simple U-Net architecture for box segmentation.
Includes MLP baseline, CNN, and U-Net for comparison.
"""

import torch
import torch.nn as nn
import torch.nn.functional as F


class MLPSegmentation(nn.Module):
    """
    Simple MLP model for segmentation.
    Flattens input, applies multiple linear layers, reshapes to output mask.
    """

    def __init__(self, in_channels=3, out_channels=1):
        super(MLPSegmentation, self).__init__()

        # PathMNIST images are 3x28x28 = 2352 input features
        # Output should be 1x28x28 = 784 features

        self.flatten = nn.Flatten()
        self.fc = nn.Sequential(
            nn.Linear(in_channels * 28 * 28, 1024),
            nn.ReLU(inplace=True),
            nn.Linear(1024, 512),
            nn.ReLU(inplace=True),
            nn.Linear(512, out_channels * 28 * 28)
        )

    def forward(self, x):
        b = x.size(0)
        x = self.flatten(x)
        x = self.fc(x)
        x = x.view(b, -1, 28, 28)
        return x


class TinyUNet(nn.Module):
    """
    Tiny U-Net for segmentation of 28x28 images.
    Optimized for small images and simple segmentation tasks.
    """

    def __init__(self, in_channels=3, out_channels=1, base_channels=16):
        super(TinyUNet, self).__init__()

        # Encoder (contracting path)
        self.enc1 = nn.Sequential(
            nn.Conv2d(in_channels, base_channels, 3, padding=1),
            nn.BatchNorm2d(base_channels),
            nn.ReLU(inplace=True)
        )
        self.enc2 = nn.Sequential(
            nn.Conv2d(base_channels, base_channels * 2, 3, padding=1),
            nn.BatchNorm2d(base_channels * 2),
            nn.ReLU(inplace=True)
        )
        self.pool = nn.MaxPool2d(2, 2)

        # Bottleneck
        self.bottleneck = nn.Sequential(
            nn.Conv2d(base_channels * 2, base_channels * 4, 3, padding=1),
            nn.BatchNorm2d(base_channels * 4),
            nn.ReLU(inplace=True)
        )

        # Decoder (Upsampling path)
        self.up1 = nn.ConvTranspose2d(
            base_channels * 4, base_channels * 2, 2, stride=2)
        self.dec1 = nn.Sequential(
            nn.Conv2d(base_channels * 4, base_channels * 2, 3, padding=1),
            nn.BatchNorm2d(base_channels * 2),
            nn.ReLU(inplace=True)
        )

        self.up2 = nn.ConvTranspose2d(
            base_channels * 2, base_channels, 2, stride=2)
        self.dec2 = nn.Sequential(
            nn.Conv2d(base_channels * 2, base_channels, 3, padding=1),
            nn.BatchNorm2d(base_channels),
            nn.ReLU(inplace=True)
        )

        # Output layer
        self.out_conv = nn.Conv2d(base_channels, out_channels, 1)

    def forward(self, x):
        # Encoder
        x1 = self.enc1(x)         # [B, base, 28, 28]
        x2 = self.pool(x1)
        x2 = self.enc2(x2)        # [B, 2*base, 14, 14]

        # Bottleneck
        x3 = self.pool(x2)
        x3 = self.bottleneck(x3)  # [B, 4*base, 7, 7]

        # Decoder
        x = self.up1(x3)          # [B, 2*base, 14, 14]
        x = torch.cat([x, x2], dim=1)
        x = self.dec1(x)

        x = self.up2(x)           # [B, base, 28, 28]
        x = torch.cat([x, x1], dim=1)
        x = self.dec2(x)

        # Output segmentation mask (logits)
        return self.out_conv(x)


def get_segmentation_model(model_name, in_channels=3, out_channels=1):
    """Get segmentation model by name."""
    if model_name == 'mlp':
        return MLPSegmentation(in_channels=in_channels, out_channels=out_channels)
    elif model_name == 'unet':
        return TinyUNet(in_channels=in_channels, out_channels=out_channels)
    else:
        raise ValueError("Unknown segmentation model: {}".format(model_name))


def count_parameters(model):
    """Count trainable parameters."""
    return sum(p.numel() for p in model.parameters() if p.requires_grad)

# Intersection over Union (IoU) metric for segmentation


def calculate_iou(pred_mask, true_mask, threshold=0.5):
    """
    Calculate Intersection over Union for binary segmentation masks.

    Args:
        pred_mask: Predicted segmentation mask [B, 1, H, W] or [B, H, W]
        true_mask: Ground truth segmentation mask [B, 1, H, W] or [B, H, W]
        threshold: Threshold for binarizing predictions

    Returns:
        IoU score (float)
    """
    # Convert to binary
    if torch.is_tensor(pred_mask):
        pred_binary = (pred_mask > threshold).float()
    else:
        pred_binary = (pred_mask > threshold).astype(float)

    if torch.is_tensor(true_mask):
        true_binary = (true_mask > 0.5).float()
    else:
        true_binary = (true_mask > 0.5).astype(float)

    # Flatten for easier computation
    if len(pred_binary.shape) > 2:
        pred_binary = pred_binary.view(pred_binary.size(0), -1)
        true_binary = true_binary.view(true_binary.size(0), -1)

    # Calculate intersection and union
    intersection = (pred_binary * true_binary).sum(dim=-1)
    union = pred_binary.sum(dim=-1) + true_binary.sum(dim=-1) - intersection

    # Handle case where both masks are empty
    # Add small epsilon to avoid division by zero
    iou = intersection / (union + 1e-8)

    return iou.mean().item() if torch.is_tensor(iou) else iou.mean()


class DiceLoss(nn.Module):
    """
    Dice Loss for segmentation tasks.
    Better than BCE for imbalanced segmentation.
    """

    def __init__(self, smooth=1e-8):
        super(DiceLoss, self).__init__()
        self.smooth = smooth

    def forward(self, pred, target):
        pred = torch.sigmoid(pred)
        pred_flat = pred.view(pred.size(0), -1)
        target_flat = target.view(target.size(0), -1)

        intersection = (pred_flat * target_flat).sum(dim=1)
        dice = (2. * intersection + self.smooth) / (
            pred_flat.sum(dim=1) + target_flat.sum(dim=1) + self.smooth
        )
        return 1 - dice.mean()


class CombinedLoss(nn.Module):
    """
    Combined BCE + Dice loss for better segmentation performance.
    """

    def __init__(self, bce_weight=0.5, dice_weight=0.5):
        super(CombinedLoss, self).__init__()
        self.bce_weight = bce_weight
        self.dice_weight = dice_weight
        self.bce_loss = nn.BCEWithLogitsLoss()
        self.dice_loss = DiceLoss()

    def forward(self, pred, target):
        bce = self.bce_loss(pred, target)
        dice = self.dice_loss(pred, target)
        return self.bce_weight * bce + self.dice_weight * dice
