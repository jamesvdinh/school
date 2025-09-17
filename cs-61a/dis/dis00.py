def f(x):
    return x - 1


def g(x):
    return x * 2


def h(x, y):
    return int(str(x) + str(y))


if __name__ == '__main__':
    sol = g(h(g(5), g(g(f(f(5))))))
    print(sol)
