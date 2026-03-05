import numpy as np


def compute_lu_product(L, U):
    n = L.shape[0]
    A = np.zeros((n, n))

    for i in range(n):
        for j in range(n):
            limit = min(i, j) + 1
            A[i, j] = np.dot(L[i, :limit], U[:limit, j])

    return A


L = np.array([[1, 0, 0], [0.5, 1, 0], [0.25, 0.75, 1]])
U = np.array([[4, 2, -1], [0, 1, 0.5], [0, 0, 2]])
print(f"L:\n{L}\n")
print(f"U:\n{U}\n")
A = compute_lu_product(L, U)
print(f"A:\n{A}")
