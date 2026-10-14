package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.ObservableValue;
import interfaces.ValueChangeListener;

/**
 * A thread-safe, concrete implementation of {@link ObservableValue} utilizing
 * {@link ReentrantLock} for concurrency control, defensive copying, and
 * exception isolation.
 *
 * @param <T> the type of the held value
 */
public class SynchronizedSimpleObservableValue<T> implements ObservableValue<T> {

    private ReentrantLock lock = new ReentrantLock();
    private T observableValue;
    private Set<ValueChangeListener<T>> listenerSet = new HashSet<>();

    /**
     * Constructs a new synchronized observable value with the specified initial
     * state.
     *
     * @param initialValue the initial value
     */
    public SynchronizedSimpleObservableValue(T initialValue) {
        observableValue = initialValue;
    }

    /**
     * Constructs a new synchronized observable value with an initial value and a
     * single initial listener.
     *
     * @param initialValue the initial value
     * @param listener     the listener to register immediately
     */
    public SynchronizedSimpleObservableValue(T initialValue, ValueChangeListener<T> listener) {
        this(initialValue);
        if (listener != null)
            this.addListener(listener);
    }

    /**
     * Constructs a new synchronized observable value with an initial value and a
     * collection of initial listeners.
     *
     * @param initialValue the initial value
     * @param listeners    the collection of listeners to register immediately
     */
    public SynchronizedSimpleObservableValue(T initialValue, Collection<? extends ValueChangeListener<T>> listeners) {
        this(initialValue);
        if (listeners != null)
            this.addListeners(listeners);
    }

    /**
     * Constructs a new synchronized observable value with an initial value and
     * variable arguments of listeners.
     *
     * @param initialValue the initial value
     * @param listeners    the listeners to register immediately
     */
    @SafeVarargs
    public SynchronizedSimpleObservableValue(T initialValue, ValueChangeListener<T>... listeners) {
        this(initialValue);
        if (listeners != null)
            this.addListeners(listeners);
    }

    @Override
    public boolean setValue(T newValue) {
        lock.lock();
        try {
            T oldValue = observableValue;

            // Handles safe comparison even when oldValue or newValue are null
            if (Objects.equals(oldValue, newValue))
                return false;

            observableValue = newValue;
            if (!listenerSet.isEmpty()) {
                // Defensive copy to prevent ConcurrentModificationException safely under lock
                for (ValueChangeListener<T> listener : new ArrayList<>(listenerSet)) {
                    try {
                        listener.onChange(oldValue, newValue);
                    } catch (Exception e) {
                        System.err.println("Exception during listener notification: " + e.getMessage());
                    }
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T getValue() {
        lock.lock();
        try {
            return observableValue;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addListener(ValueChangeListener<T> listener) {
        lock.lock();
        try {
            return listenerSet.add(listener);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeListener(ValueChangeListener<T> listener) {
        lock.lock();
        try {
            return listenerSet.remove(listener);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeAllListeners() {
        lock.lock();
        try {
            listenerSet.clear();
        } finally {
            lock.unlock();
        }
    }

    protected ReentrantLock getLock() {
        return lock;
    }

}
