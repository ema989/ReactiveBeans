package entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import interfaces.ValueChangeListener;
import interfaces.ObservableValue;

public class SimpleObservableValue<T> implements ObservableValue<T> {

    private T observableValue;
    private Set<ValueChangeListener<T>> listenerSet = new HashSet<>();

    public SimpleObservableValue(T initialValue) {
        observableValue = Objects.requireNonNull(initialValue, "initialValue can't be null");
    }

    public SimpleObservableValue(T initialValue, ValueChangeListener<T> listener) {
        this(observableValue = Objects.requireNonNull(initialValue, "initialValue can't be null"));
        listenerSet.add(listener);
    }

    @SafeVarargs
    public SimpleObservableValue(T initialValue, ValueChangeListener<T>... listeners) {
        this(observableValue = Objects.requireNonNull(initialValue, "initialValue can't be null"));
        Collections.addAll(listenerSet, listeners);
    }

    public SimpleObservableValue(T initialValue, Collection<? extends ValueChangeListener<T>> listener) {
        this(observableValue = Objects.requireNonNull(initialValue, "initialValue can't be null"));
        listenerSet.addAll(listener);
    }

    @Override
    public void setValue(T newValue) {
        T oldValue = observableValue;
        observableValue = Objects.requireNonNull(newValue, "newValue can't be null");
        if (!listenerSet.isEmpty())
            listenerSet.forEach(listener -> listener.onChange(oldValue, newValue));
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
    @SafeVarargs
    public final boolean addListener(ValueChangeListener<T>... listeners) {
        return Collections.addAll(listenerSet, listeners);
    }

    @Override
    public boolean addListeners(Collection<? extends ValueChangeListener<T>> listeners) {
        return listenerSet.addAll(listeners);
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
