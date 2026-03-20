import numpy as np
from scipy.linalg import hilbert


def cgs(A):
    m, n = A.shape
    Q = np.zeros((m, n))
    R = np.zeros((n, n))
    for j in range(n):
        v = A[:, j].copy()
        for i in range(j):
            R[i, j] = np.dot(Q[:, i], A[:, j])
            v = v - R[i, j] * Q[:, i]
        R[j, j] = np.linalg.norm(v)
        Q[:, j] = v / R[j, j]
    return Q, R


def mgs(A):
    m, n = A.shape
    V = A.copy().astype(float)
    Q = np.zeros((m, n))
    R = np.zeros((n, n))
    for i in range(n):
        R[i, i] = np.linalg.norm(V[:, i])
        Q[:, i] = V[:, i] / R[i, i]
        for j in range(i + 1, n):
            R[i, j] = np.dot(Q[:, i], V[:, j])
            V[:, j] = V[:, j] - R[i, j] * Q[:, i]
    return Q, R


def householder_qr(A):
    m, n = A.shape
    R = A.copy().astype(float)
    Q = np.eye(m)
    for k in range(n):
        x = R[k:, k]
        e1 = np.zeros_like(x)
        e1[0] = 1.0
        # Householder vector v
        v = x + np.sign(x[0]) * np.linalg.norm(x) * e1
        v = v / np.linalg.norm(v)
        # Apply to R
        R[k:, k:] -= 2.0 * np.outer(v, np.dot(v, R[k:, k:]))
        # Accumulate Q
        H_k = np.eye(m)
        H_k[k:, k:] -= 2.0 * np.outer(v, v)
        Q = Q @ H_k
    return Q, R


# Testing with H7
n = 7
H7 = hilbert(n)

# (a) CGS and MGS
Q_cgs, _ = cgs(H7)
Q_mgs1, R_mgs1 = mgs(H7)

# (b) MGS twice
Q_mgs2, R_mgs2 = mgs(Q_mgs1)

# (c) Householder
Q_hh, R_hh = householder_qr(H7)

# Results
results = {
    "CGS": np.linalg.norm(Q_cgs.T @ Q_cgs - np.eye(n), ord=2),
    "MGS (1x)": np.linalg.norm(Q_mgs1.T @ Q_mgs1 - np.eye(n), ord=2),
    "MGS (2x)": np.linalg.norm(Q_mgs2.T @ Q_mgs2 - np.eye(n), ord=2),
    "Householder": np.linalg.norm(Q_hh.T @ Q_hh - np.eye(n), ord=2)
}

for method, error in results.items():
    print(f"{method:15}: Error = {error:.2e}")

''' TERMINAL OUTPUT
CGS            : Error = 5.09e-02
MGS (1x)       : Error = 1.38e-09
MGS (2x)       : Error = 2.41e-16
Householder    : Error = 7.92e-16
'''
