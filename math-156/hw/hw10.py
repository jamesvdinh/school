import numpy as np
import matplotlib.pyplot as plt
from scipy.io import loadmat

# Load the data
data = loadmat('hw/mnist.mat')
testX = data['testX']
testY = data['testY']

# Extract first image in X and reshape to 28x28
first_image_flat = testX[0, :]

X_image = first_image_flat.reshape(28, 28).astype(float)

# 3. Visualize
plt.imshow(X_image, cmap='gray')
plt.title(f"Label: {testY[0][0]}")
plt.show()

print(f"First entry of testY: {testY[0][0]}")

# i. Convert to double precision
A = testX.astype(np.float64)

# ii. Center the data
column_means = np.mean(A, axis=0)
A_centered = A - column_means  # A - mu

# iii. Compute SVD
U, S, Vt = np.linalg.svd(A_centered, full_matrices=False)

# c) Project first two principal components
PCs = Vt[:2, :].T
X_projected = A_centered @ PCs

plt.figure(figsize=(10, 8))
scatter = plt.scatter(X_projected[:, 0], X_projected[:, 1], c=testY.flatten(
), cmap='tab10', s=5, alpha=0.6)
plt.colorbar(scatter, ticks=range(10), label='Digit Label')
plt.title("Projection onto First Two Principal Components")
plt.xlabel("Principal Component 1")
plt.ylabel("Principal Component 2")
plt.grid(True, linestyle='--', alpha=0.5)
plt.show()

'''TERMINAL OUTPUT
First entry of testY: 7
'''
