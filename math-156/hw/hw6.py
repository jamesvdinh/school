import numpy as np


def classical_qr(A):
    m, n = A.shape
    Q = np.zeros((m, n))
    R = np.zeros((n, n))

    for i in range(n):
        v = A[:, i].copy()

        for j in range(i):
            R[j, i] = Q[:, j] @ A[:, i]
            v -= R[j, i] * Q[:, j]

        R[i, i] = np.linalg.norm(v)
        Q[:, i] = v / R[i, i]

    return Q, R


def modified_qr(A):
    m, n = A.shape
    Q = np.zeros((m, n))
    R = np.zeros((n, n))
    V = A.copy()

    for i in range(n):
        R[i, i] = np.linalg.norm(V[:, i])
        Q[:, i] = V[:, i] / R[i, i]

        for j in range(i + 1, n):
            R[i, j] = Q[:, i] @ V[:, j]
            V[:, j] = V[:, j] - R[i, j] * Q[:, i]

    return Q, R


def qr_errors(A, Q, R):
    residual = np.linalg.norm(A - Q @ R, 2)
    orthogonality = np.linalg.norm(Q.T @ Q - np.eye(Q.shape[1]), 2)
    return residual, orthogonality


eps = np.finfo(float).eps

A = np.array([
    [1, 1, 1],
    [eps, 0, 0],
    [0, eps, 0],
    [0, 0, eps]
], dtype=float)

Q_cgs, R_cgs = classical_qr(A)
Q_mgs, R_mgs = modified_qr(A)

res_cgs, ortho_cgs = qr_errors(A, Q_cgs, R_cgs)
res_mgs, ortho_mgs = qr_errors(A, Q_mgs, R_mgs)

print("CGS residual:", res_cgs)
print("CGS orthogonality:", ortho_cgs)

print("MGS residual:", res_mgs)
print("MGS orthogonality:", ortho_mgs)

''' TERMINAL OUTPUT
CGS residual: 6.372400086712262e-33
CGS orthogonality: 0.5000000000000001
MGS residual: 6.0612148999478584e-33
MGS orthogonality: 3.083989583803855e-16
'''
