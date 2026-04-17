import numpy as np
from scipy.linalg import hilbert
import matplotlib.pyplot as plt


def randomized_svd(A, k):
    """
    Randomized SVD algorithm.
    Returns U_k, Sigma_k, V_k such that A ≈ U_k @ diag(Sigma_k) @ V_k.T
    """
    m, n = A.shape
    # Step 1: Random sketch
    Omega = np.random.randn(n, k)
    Y = A @ Omega                        # m x k

    # Step 2: QR of Y (economy)
    Q, _ = np.linalg.qr(Y)              # Q is m x k

    # Step 3: Project A
    B = Q.T @ A                          # k x n

    # Step 4: SVD of small matrix B
    U_hat, Sigma_k, Vt_k = np.linalg.svd(B, full_matrices=False)

    # Step 5: Recover left singular vectors
    U_k = Q @ U_hat                      # m x k

    return U_k, Sigma_k, Vt_k


def spectral_norm_error(A, U_k, Sigma_k, Vt_k):
    """||A - U Sigma V^T||_2 = largest singular value of the residual"""
    A_approx = (U_k * Sigma_k) @ Vt_k
    residual = A - A_approx
    return np.linalg.norm(residual, ord=2)


# ── Build 100x100 Hilbert matrix ──────────────────────────────────────────────
n = 100
H = hilbert(n)

# ── True singular values (for Eckart-Young optimal errors) ───────────────────
_, true_sigmas, _ = np.linalg.svd(H)

k_values = [1, 2, 3, 5, 8, 10, 15, 20]
rand_errors = []
optimal_errors = []

np.random.seed(42)
for k in k_values:
    # Randomized SVD error
    U_k, Sigma_k, Vt_k = randomized_svd(H, k)
    rand_errors.append(spectral_norm_error(H, U_k, Sigma_k, Vt_k))

    # Eckart-Young optimal error = sigma_{k+1}
    optimal_errors.append(true_sigmas[k] if k < n else 0.0)

# ── Print table ───────────────────────────────────────────────────────────────
print(f"{'k':>4} | {'Randomized Error':>18} | {'Optimal (σ_{k+1})':>18} | {'Ratio':>8}")
print("-" * 58)
for k, re, oe in zip(k_values, rand_errors, optimal_errors):
    ratio = re / oe if oe > 1e-16 else float('inf')
    print(f"{k:>4} | {re:>18.6e} | {oe:>18.6e} | {ratio:>8.3f}")

"""OUTPUT
   k |   Randomized Error |  Optimal (σ_{k+1}) |    Ratio
----------------------------------------------------------
   1 |       2.142902e+00 |       8.214456e-01 |    2.609
   2 |       2.420872e-01 |       2.185959e-01 |    1.107
   3 |       9.509982e-02 |       4.929225e-02 |    1.929
   5 |       3.233078e-02 |       1.885063e-03 |   17.151
   8 |       4.322691e-05 |       8.536281e-06 |    5.064
  10 |       7.015601e-07 |       1.788722e-07 |    3.922
  15 |       3.334944e-11 |       5.212225e-12 |    6.398
  20 |       2.489985e-15 |       1.555226e-16 |   16.010
"""
