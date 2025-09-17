# Lec 2 - Static Vars

## Static methods

### Class

- non-static methods should not be invoked by the class name

### Instance

- static methods should not be invoked by the instance

```java
public class Dog {
    public int weight;
    public static String binomen = "Canis familiaris";

    // constructor
    public Dog (int startingWeight{
        weight = startingWeight;
    }

    // can only be called by class
    public static Dog maxDog(Dog d1, Dog d2) {
        if (d1.weight > d2.weight) {
            return d1;
        }
        return d2;
    }

    // can only be called by instance
    public void makeNoise() {
        System.out.println("woof!");
    }
}
```

## Lists

- must choose a list type in java when defining
- indexing using `L.get(0)`

```java
public static void main(String[] args) {
    List<String> L = new LinkedList<>()
    L.add("a");
    System.out.println(L.get(0));
}
```

## Arrays

- size is defined, cannot change
- single type
- indexing is `x[0]`

```java
public static void main(String[] args) {
    String[] x = new String[5];
    x[0] = "a"
    System.out.println(x[0]);
}
```

## Maps

```java
public static void main(String[] args) {
    Map<String, String> L = new TreeMap<>()
    L.put("dog", "woof");
    String sound = L.get("dog");
}
```
