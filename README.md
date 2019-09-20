# java-lazy

Simple and robust implementation of Kotlin lazy in Java. Thread-safe. Suitable for Android. Require Java 8.

# Example

```java
class Example
{
    final Supplier<Integer> lazyField;
    final Supplier<String> lazyField2 = lazy(() -> String.valueOf(2 + 6));

    Example(String value)
    {
        // 'value' captured in lambda to be evaluated later

        lazyField = lazy(() -> Integer.valueOf(value));
    }

    void func()
    {
        // 'lazyField(s)' will be evaluated on the first 'get()' call

        int value = lazyField.get();
        String value2 = lazyField2.get();

        // Do things with 'value(s)'...
    }
}
```

# Download



# License

`MIT` - just do whatever.
