import numpy as np


def inner_product_with_error(x, y):
    n = len(x)
    eps_mch = np.finfo(float).eps

    s_hat = 0.0
    e_bound = 0.0

    for i in range(n):
        # compute floating point product z_i
        z_hat = x[i] * y[i]

        # update partial sum s_i
        s_next = s_hat + z_hat

        # update the running error bound
        e_bound = e_bound + eps_mch * (abs(s_next) + abs(z_hat))

        s_hat = s_next

    return s_hat, e_bound


x = np.random.randn(1000)
y = np.random.randn(1000)
val, error_limit = inner_product_with_error(x, y)

print(f"Result: {val}")
print(f"Running Error Bound: {error_limit}")

''' TERMINAL OUTPUT
Result: 3.126331331626953
Running Error Bound: 5.129872869599341e-12

Result: -29.611073976476906
Running Error Bound: 5.275048756180587e-12

Result: 44.69289291092224
Running Error Bound: 7.330936719092104e-12
'''
