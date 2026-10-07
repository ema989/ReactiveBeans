package interfaces;

@FunctionalInterface
public interface ValueChangeListener<T> {
    void onChange(T oldValue, T newValue);
}
