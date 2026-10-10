package interfaces;

/**
 * A functional interface used to listen for value change events emitted
 * by an {@link ObservableValue}.
 *
 * @param <T> the type of the value being observed
 */
@FunctionalInterface
public interface ValueChangeListener<T> {

    /**
     * Invoked when the observed value changes.
     *
     * @param oldValue the previous value before the change
     * @param newValue the new updated value
     */
    void onChange(T oldValue, T newValue);

}
