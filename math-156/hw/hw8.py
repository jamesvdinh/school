import numpy as np


def power_method(A, x, tol=1e-10, max_iter=1000):
    v = x / np.linalg.norm(x)

    for i in range(max_iter):
        v_new = A @ v
        v_new = v_new / np.linalg.norm(v_new)

        # check for convergence
        if np.linalg.norm(v_new - v) < tol or np.linalg.norm(v_new + v) < tol:
            print(f"Converged in {i+1} iterations")
            break
        v = v_new
    else:
        print(f"Did not converge after {max_iter} iterations")

    u = A @ v_new
    i = np.argmax(np.abs(v_new))
    lam = u[i] / v_new[i]
    return v_new, lam


A = np.array([[-1, 2, -3],
              [-3, 4, -3],
              [-1, -2, 1]], dtype=float)

print("True eigenvalues:", np.sort(np.linalg.eigvals(A))[::-1])
print()

starting_vectors = {
    "Random": np.random.randn(3),
    "x=[2,1,0]": np.array([2., 1., 0.]),
    "x=[√2,0,-√2]": np.array([np.sqrt(2), 0., -np.sqrt(2)])
}

for name, x in starting_vectors.items():
    print(f"--- {name} ---")
    v, lam = power_method(A, x)
    print(f"Eigenvalue: {lam:.6f},  Eigenvector: {v}\n")

''' TERMINAL OUTPUT
True eigenvalues: [ 4.  2. -2.]

--- Random ---
Converged in 37 iterations
Eigenvalue: 4.000000,  Eigenvector: [ 0.57735027  0.57735027 -0.57735027]

--- x=[2,1,0] ---
Converged in 89 iterations
Eigenvalue: 4.000000,  Eigenvector: [-0.57735027 -0.57735027  0.57735027]

--- x=[√2,0,-√2] ---
Converged in 1 iterations
Eigenvalue: 2.000000,  Eigenvector: [ 0.70710678  0.         -0.70710678]
'''
