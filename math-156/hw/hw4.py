import numpy as np


def solve_ge(A_in, b_in, use_pivoting=True):
    A = A_in.copy().astype(float)  # non-destructive
    b = b_in.copy().astype(float)  # non-destructive
    n = len(b)

    # Gaussian Elimination
    for i in range(n - 1):
        if use_pivoting:
            # Partial Pivoting: Find the largest element in current column
            pivot_row = np.argmax(np.abs(A[i:, i])) + i
            A[[i, pivot_row]] = A[[pivot_row, i]]
            b[[i, pivot_row]] = b[[pivot_row, i]]

        for k in range(i + 1, n):
            l = A[k, i] / A[i, i]
            for m in range(i, n):
                A[k, m] -= l * A[i, m]
            b[k] -= l * b[i]

    # Back Substitution
    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        S = b[i]
        for j in range(i + 1, n):
            S -= A[i, j] * x[j]
        x[i] = S / A[i, i]

    return x


# Define input
eps = np.finfo(float).eps
A = np.array([[eps, -1, 0],
              [-1,  2, -1],
              [0,  -1,  2]])

b = np.array([eps - 1, 0, 1])

# Run both versions
x_no_pivot = solve_ge(A, b, use_pivoting=False)
x_with_pivot = solve_ge(A, b, use_pivoting=True)

print(f"True solution: [1, 1, 1]")
print(f"No pivoting:   {x_no_pivot}")
print(f"With pivoting: {x_with_pivot}")

''' TERMINAL OUTPUT
True solution: [1, 1, 1]
No pivoting:   [1. 1. 1.]
With pivoting: [1. 1. 1.]
'''
