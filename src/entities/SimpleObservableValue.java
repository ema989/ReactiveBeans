package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import interfaces.ValueChangeListener;
import interfaces.ObservableValue;

public class SimpleObservableValue<T> implements ObservableValue<T> {

    private T observableValue;
    private Set<ValueChangeListener<T>> listenerSet = new HashSet<>();

    public SimpleObservableValue(T initialValue) {
        observableValue = initialValue;
    }

    public SimpleObservableValue(T initialValue, ValueChangeListener<T> listener) {
        this(initialValue);
        if (listener != null)
            this.addListener(listener);
    }

    public SimpleObservableValue(T initialValue, Collection<? extends ValueChangeListener<T>> listeners) {
        this(initialValue);
        if (listeners != null)
            this.addListeners(listeners);
    }

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
