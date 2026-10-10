package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import interfaces.ValueChangeListener;
import interfaces.ObservableValue;

/**
 * A standard, concrete implementation of {@link ObservableValue} that maintains
 * a state value and a collection of change listeners with defensive copy and
 * exception isolation protection.
 *
 * @param <T> the type of the held value
 */
public class SimpleObservableValue<T> implements ObservableValue<T> {

    private T observableValue;
    private Set<ValueChangeListener<T>> listenerSet = new HashSet<>();

    /**
     * Constructs a new observable value with the specified initial state.
     *
     * @param initialValue the initial value
     */
    public SimpleObservableValue(T initialValue) {
        observableValue = initialValue;
    }

    /**
     * Constructs a new observable value with an initial value and a single initial
     * listener.
     *
     * @param initialValue the initial value
     * @param listener     the listener to register immediately
     */
    public SimpleObservableValue(T initialValue, ValueChangeListener<T> listener) {
        this(initialValue);
        if (listener != null)
            this.addListener(listener);
    }

    /**
     * Constructs a new observable value with an initial value and a collection of
     * initial listeners.
     *
     * @param initialValue the initial value
     * @param listeners    the collection of listeners to register immediately
     */
    public SimpleObservableValue(T initialValue, Collection<? extends ValueChangeListener<T>> listeners) {
        this(initialValue);
        if (listeners != null)
            this.addListeners(listeners);
    }

    /**
     * Constructs a new observable value with an initial value and variable
     * arguments of listeners.
     *
     * @param initialValue the initial value
     * @param listeners    the listeners to register immediately
     */
    @SafeVarargs
    public SimpleObservableValue(T initialValue, ValueChangeListener<T>... listeners) {
        this(initialValue);
        if (listeners != null)
            this.addListeners(listeners);
    }

    @Override
    public boolean setValue(T newValue) {
        T oldValue = observableValue;

        // Handles safe comparison even when oldValue or newValue are null
        if (Objects.equals(oldValue, newValue))
            return false;

        observableValue = newValue;
        if (!listenerSet.isEmpty()) {
            // Defensive copy to prevent ConcurrentModificationException if a listener
            // removes or adds itself
            for (ValueChangeListener<T> listener : new ArrayList<>(listenerSet)) {
                try {
                    listener.onChange(oldValue, newValue);
                } catch (Exception e) {
                    System.err.println("Exception during listener notification: " + e.getMessage());
                }
            }
        }
        return true;
    }

    @Override
    public T getValue() {
        return observableValue;
    }

    @Override
    public boolean addListener(ValueChangeListener<T> listener) {
        return listenerSet.add(listener);
    }

    @Override
    public boolean removeListener(ValueChangeListener<T> listener) {
        return listenerSet.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        listenerSet.clear();
    }

}
