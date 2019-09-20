# java-lazy

Simple and robust implementation of Kotlin lazy in Java. Thread-safe. Suitable for Android. Require Java 8.

# Example

```java
import static com.goldberg.lazy.Lazy.lazy;

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

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.Mr-Goldberg:java-lazy:1.0'
}
```

#### Java 8 is required. To use it, put in gradle file:

In Java project:

```gradle
sourceCompatibility = "8"
targetCompatibility = "8"
```

In Android:

```gradle
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

# License

`MIT` - just do whatever.
