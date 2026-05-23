```ad-example
~~~py
def add(item, bucket=[]):
	bucket.append(item)
	return bucket

print(add(1))  # [1]
print(add(2))  # [1, 2]
~~~
Bug: the default parameter `bucket = []` are evaluated **once**, not on each call
- mutable defaults (list/dict) are shared across all calls

```

## Shallow vs Deep Copy
```ad-example
~~~py
# Pointer
a = [1, 2, 3]
b = a  # pointer ref
b.append(4)
print(b)  # [1, 2, 3, 4]

# Shallow copy: new outer container, SAME inner objects
x = [[1, 2], [3]]
y = list(x)
y[0].append(4)
print(x)  # [[1, 2, 4], [3]]

# Deep copy: fully independent
import copy
z = copy.deepcopy
z[0].append(0)
print(x)  # unchanged
~~~
```

**Mutable**: primitives such as ints, strings, tuples
**Immtable**: lists, dicts, sets, objects

## Generators vs Lists
```ad-example
~~~py
nums = [x ** 2 for x in range(5)]  # creates a list in memory
gen = (x ** 2 for x in range(5))
~~~

print(sum(gen))   # 30
print(sum(gen))   # 0 <-- already used up gen
```

use `yield` to return the next iter value

## OOP (Object Oriented Programming)
| **Principle**     | **Key Concept**   | **In Simple Terms**                                    | **Python Mechanism**                 |
| ----------------- | ----------------- | ------------------------------------------------------ | ------------------------------------ |
| **Encapsulation** | Data hiding       | Keep private data safe inside the object.              | Double underscores (`__variable`)    |
| **Inheritance**   | Reusability       | Pass down traits from a parent class to a child class. | Class syntax: `class Child(Parent):` |
| **Polymorphism**  | Many forms        | Same method name, completely different behavior.       | Overriding methods across classes    |
| **Abstraction**   | Hiding complexity | Show the user _what_ it does, hide _how_ it does it.   | `abc` module & `@abstractmethod`     |

## Decorators
