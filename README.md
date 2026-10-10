# ReactiveBeans

A lightweight, robust, and clean Java library implementing observable values and unidirectional data binding. It is specifically designed to prevent common pitfalls such as memory leaks (via weak references), recursive loops during updates, and unhandled listener exceptions.

---

## Features

- **Observable Values**: Track state changes efficiently with clean interfaces and support for initial listeners (via values, collections, or varargs).
- **Unidirectional Binding (`Bindable`)**: Link multiple values together so that updates propagate seamlessly from source to targets, with immediate initial value synchronization.
- **Memory Leak Protection**: Built-in `WeakValueChangeListener` wrapper leveraging `WeakReference` to automatically unregister dead listeners when reclaimed by the Garbage Collector.
- **Loop & Concurrency Safety**: Uses execution guards (`isUpdating` flags protected by `try-finally` blocks) to prevent infinite loops during propagation and defensive copying of collections to avoid `ConcurrentModificationException`.
- **Exception Isolation**: Errors within individual listeners or binding targets are caught and safely logged, preventing one faulty component from breaking the entire notification flow.

---

## Why ReactiveBeans?

While heavy reactive programming frameworks (like RxJava or Project Reactor) or framework-specific solutions (like JavaFX Properties) exist, **ReactiveBeans** was built to fill a specific gap:

- **Zero Dependencies**: A lightweight, standalone library that doesn't drag along massive ecosystems. Perfect for lightweight Java applications, custom tools, or desktop utilities.
- **Memory-Leak Safety out-of-the-box**: Standard listener patterns in Java often lead to memory leaks if developers forget to unregister them. ReactiveBeans addresses this via `WeakValueChangeListener`, automatically cleaning up dead listeners reclaimed by the Garbage Collector.
- **Predictable Binding**: Features built-in loop protection (`isUpdating` flags) and atomic updates to ensure complex unidirectional data bindings remain stable and safe from recursion errors.
- **Educational & Clean Design**: Built with clean, standard Java idioms, making it an excellent resource for developers wanting to understand the inner workings of the Observer pattern, weak references, and robust concurrency control.

---

## Project Structure

```text
src/
├── interfaces/
│   ├── ObservableValue.java
│   ├── Bindable.java
│   └── ValueChangeListener.java
└── entities/
    ├── SimpleObservableValue.java
    ├── BindableObservableValue.java
    └── WeakValueChangeListener.java
```

---

## Compilation & Packaging

If your project is structured without traditional build tools, you can easily build the library using standard JDK commands from your project root:

1. **Compile the source files into a target directory (`bin/`)**:
   ```bash
   javac -d bin src/interfaces/*.java src/entities/*.java
   ```

2. **Package them into a `.jar` library**:
   ```bash
   jar cf observable-library.jar -C bin .
   ```

3. **Use**: Add `observable-library.jar` to the classpath or build path of your consuming projects.

---

## Code Examples

### 1. Basic Observable Value
```java
import entities.SimpleObservableValue;

// Initialize an observable with a starting value
SimpleObservableValue<String> username = new SimpleObservableValue<>("Guest");

// Add a listener
username.addListener((oldVal, newVal) -> {
    System.out.println("User changed from " + oldVal + " to " + newVal);
});

// Update the value (triggers the listener)
username.setValue("Alice"); 
// Output: User changed from Guest to Alice
```

### 2. Value Binding (`Bindable`)
```java
import entities.BindableObservableValue;

BindableObservableValue<Integer> sourcePort = new BindableObservableValue<>(8080);
BindableObservableValue<Integer> targetPort = new BindableObservableValue<>(3000);

// Bind target to source (target immediately synchronizes to 8080)
sourcePort.bindWith(targetPort);

sourcePort.setValue(9090);
System.out.println(targetPort.getValue()); // Output: 9090
```

### 3. Preventing Memory Leaks with Weak Listeners
```java
import entities.SimpleObservableValue;
import entities.WeakValueChangeListener;
import interfaces.ValueChangeListener;

SimpleObservableValue<String> status = new SimpleObservableValue<>("IDLE");

ValueChangeListener<String> myListener = (oldVal, newVal) -> {
    System.out.println("Status updated: " + newVal);
};

// Wrap the listener in a WeakValueChangeListener
WeakValueChangeListener<String> weakListener = new WeakValueChangeListener<>(status, myListener);
status.addListener(weakListener);

// If 'myListener' loses all strong references in your application, 
// the Garbage Collector will clear it, and it will automatically unregister itself.
```

---

## License

This project is open-source and available under the terms of the [MIT License](LICENSE).