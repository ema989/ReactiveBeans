package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import interfaces.Bindable;
import interfaces.ValueChangeListener;

/**
 * A thread-safe extension of {@link SynchronizedSimpleObservableValue}
 * implementing
 * {@link Bindable} with loop protection and deadlock-free lock ordering.
 *
 * @param <T> the type of the held value
 */
public class SynchronizedBindableObservableValue<T> extends SynchronizedSimpleObservableValue<T>
        implements Bindable<T> {

    private final Set<Bindable<T>> bindSet = new HashSet<>();
    private boolean isUpdating = false;

    public SynchronizedBindableObservableValue(T initialValue) {
        super(initialValue);
    }

    public SynchronizedBindableObservableValue(T initialValue, ValueChangeListener<T> listener) {
        super(initialValue, listener);
    }

    public SynchronizedBindableObservableValue(T initialValue, Collection<? extends ValueChangeListener<T>> listeners) {
        super(initialValue, listeners);
    }

    @SafeVarargs
    public SynchronizedBindableObservableValue(T initialValue, ValueChangeListener<T>... listeners) {
        super(initialValue, listeners);
    }

    @Override
    public boolean setValue(T newValue) {
        ReentrantLock lock = super.getLock();

        lock.lock();
        try {
            if (isUpdating)
                return false;

            isUpdating = true;

            if (!super.setValue(newValue))
                return false;

            // Extract targets locally while under lock
            ArrayList<Bindable<T>> targets = new ArrayList<>(bindSet);

            // Release internal lock before propagating to external beans to prevent
            // deadlock risks
            lock.unlock();
            try {
                if (!targets.isEmpty()) {
                    for (Bindable<T> target : targets) {
                        try {
                            target.setValue(newValue);
                        } catch (Exception e) {
                            System.err.println("Bind propagation error: " + e.getMessage());
                        }
                    }
                }
            } finally {
                // Re-acquire lock to maintain structural integrity in finally block exit
                lock.lock();
            }
            return true;
        } finally {
            isUpdating = false;
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public boolean bindWith(Bindable<T> bean) {
        Objects.requireNonNull(bean, "bean can't be null");

        if (bean == this)
            return false;

        ReentrantLock lock = super.getLock();
        T currentValue;

        lock.lock();
        try {
            boolean added = bindSet.add(bean);
            if (!added)
                return false;
            currentValue = this.getValue();
        } finally {
            lock.unlock();
        }

        // Synchronize the target bean outside the primary lock to prevent nested-lock
        // deadlocks
        try {
            bean.setValue(currentValue);
        } catch (Exception e) {
            System.err.println("Initial bind synchronization error: " + e.getMessage());
        }
        return true;
    }

    @Override
    public boolean unbind(Bindable<T> bean) {
        Objects.requireNonNull(bean, "bean can't be null");

        ReentrantLock lock = super.getLock();
        lock.lock();
        try {
            return bindSet.remove(bean);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void unbindAll() {
        ReentrantLock lock = super.getLock();
        lock.lock();
        try {
            bindSet.clear();
        } finally {
            lock.unlock();
        }
    }
}
