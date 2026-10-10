package interfaces;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents an observable value holder that allows listeners to be notified
 * of changes whenever the underlying value is updated.
 *
 * @param <T> the type of the held value
 */
public interface ObservableValue<T> {

    /**
     * Updates the current value of this observable. If the new value is equal to
     * the old value (according to
     * {@link java.util.Objects#equals(Object, Object)}),
     * the value is not updated and listeners are not notified.
     *
     * @param newValue the new value to set
     * @return {@code true} if the value was successfully updated and listeners were
     *         notified,
     *         {@code false} otherwise
     */
    boolean setValue(T newValue);

    /**
     * Returns the current value held by this observable.
     *
     * @return the current value, or {@code null} if not set
     */
    T getValue();

    /**
     * Registers a change listener to be notified when the value changes.
     *
     * @fn listener the listener to add
     * @return {@code true} if the listener was added, {@code false} if it was
     *         already present
     */
    boolean addListener(ValueChangeListener<T> listener);

    /**
     * Unregisters a previously registered change listener.
     *
     * @param listener the listener to remove
     * @return {@code true} if the listener was removed, {@code false} if it was not
     *         found
     */
    boolean removeListener(ValueChangeListener<T> listener);

    /**
     * Removes all registered listeners from this observable.
     */
    void removeAllListeners();

    // Helper methods

    default boolean addListeners(Collection<? extends ValueChangeListener<T>> listeners) {
        boolean modified = false;
        if (listeners != null)
            for (ValueChangeListener<T> listener : listeners)
                if (addListener(listener))
                    modified = true;
        return modified;
    }

    @SuppressWarnings("unchecked")
    default boolean addListeners(ValueChangeListener<T>... listeners) {
        return addListeners(Arrays.asList(listeners));
    }

    default boolean removeListeners(Collection<? extends ValueChangeListener<T>> listeners) {
        boolean modified = false;
        if (listeners != null)
            for (ValueChangeListener<T> listener : listeners)
                if (removeListener(listener))
                    modified = true;
        return modified;
    }

    @SuppressWarnings("unchecked")
    default boolean removeListener(ValueChangeListener<T>... listeners) {
        return removeListeners(Arrays.asList(listeners));
    }

}
