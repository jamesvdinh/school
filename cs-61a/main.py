def add_to_each(p, edit):
    return map(lambda x: (x[0] + edit[0], x[1] + edit[1], x[2] + edit[2]), p)


p = [(0, 0, 0), (1, 1, 1)]
i = iter(add_to_each(p, (10, 10, 10)))
print(next(i))


def longest(s, n):
    if s is Link.empty:
        return s
    t = longest(s.rest, n)
    if s.first <= n:
        return longer(Link(s.first, longest(s.rest, n - s.first)), t)
    else:
        return t
