"""
Simple CNN architectures for PathMNIST classification.
Includes MLP baseline and CNN variants for comparison.
"""

import torch
import torch.nn as nn
import torch.nn.functional as F


class MLPModel(nn.Module):
    """
    Simple MLP model: Flatten input then run through hidden layers.
    """

    def __init__(self, num_classes=9):
        super(MLPModel, self).__init__()

        # PathMNIST images are 3x28x28 = 2352 features
        input_size = 3 * 28 * 28

        # 🧱 Define fully connected layers
        self.fc1 = nn.Linear(input_size, 512)
        self.fc2 = nn.Linear(512, 256)
        self.fc3 = nn.Linear(256, num_classes)

        # Optional: dropout for regularization
        self.dropout = nn.Dropout(p=0.3)

    def forward(self, x):
        # Flatten input: (batch_size, 3, 28, 28) → (batch_size, 2352)
        x = x.view(x.size(0), -1)

        # Hidden layers with ReLU activations
        x = F.relu(self.fc1(x))
        x = self.dropout(x)
        x = F.relu(self.fc2(x))
        x = self.dropout(x)

        # Output logits (no softmax needed — CrossEntropyLoss handles that)
        x = self.fc3(x)
        return x


class CNNModel(nn.Module):
    """
    Simple CNN model: TODO: Add your own architecture here
    """

    def __init__(self, num_classes=9):
        super(CNNModel, self).__init__()

        # 🧱 Convolutional feature extractor
        self.conv1 = nn.Conv2d(
            in_channels=3, out_channels=32, kernel_size=3, padding=1)
        self.conv2 = nn.Conv2d(32, 64, kernel_size=3, padding=1)
        self.conv3 = nn.Conv2d(64, 128, kernel_size=3, padding=1)

        self.pool = nn.MaxPool2d(2, 2)  # Halves spatial dimensions each time
        self.dropout = nn.Dropout(0.25)

        # After 3 poolings: 28 → 14 → 7 → 3
        self.fc1 = nn.Linear(128 * 3 * 3, 256)
        self.fc2 = nn.Linear(256, num_classes)

    def forward(self, x):
        # 🌀 Feature extraction
        x = F.relu(self.conv1(x))   # -> (32, 28, 28)
        x = self.pool(x)            # -> (32, 14, 14)
        x = F.relu(self.conv2(x))   # -> (64, 14, 14)
        x = self.pool(x)            # -> (64, 7, 7)
        x = F.relu(self.conv3(x))   # -> (128, 7, 7)
        x = self.pool(x)            # -> (128, 3, 3)
        x = self.dropout(x)

        # 🧠 Flatten
        x = torch.flatten(x, 1)     # -> (batch_size, 128*3*3)

        # 💡 Fully connected layers
        x = F.relu(self.fc1(x))
        x = self.dropout(x)
        x = self.fc2(x)

        # Return logits (CrossEntropyLoss will handle softmax)
        return x


def get_model(model_name, num_classes=9):
    """Get model by name."""
    if model_name == 'mlp':
        return MLPModel(num_classes)
    elif model_name == 'cnn':
        return CNNModel(num_classes)
    else:
        # TODO: add your models names here
        raise ValueError("Unknown model: {}".format(model_name))


def count_parameters(model):
    """Count trainable parameters."""
    return sum(p.numel() for p in model.parameters() if p.requires_grad)
