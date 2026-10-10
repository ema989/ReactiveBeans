package entities;

import java.lang.ref.WeakReference;

import interfaces.ObservableValue;
import interfaces.ValueChangeListener;

/**
 * A wrapper around a {@link ValueChangeListener} utilizing a
 * {@link WeakReference}
 * to prevent memory leaks when observing long-lived observable sources. If the
 * underlying
 * listener is garbage-collected, this wrapper automatically unregisters itself
 * from the observable.
 *
 * @param <T> the type of the value being observed
 */
public class WeakValueChangeListener<T> implements ValueChangeListener<T> {

    private final ObservableValue<T> observableValue;
    private final WeakReference<ValueChangeListener<T>> weakReference;

    /**
     * Constructs a weak listener wrapper.
     *
     * @param observableValue the observable source to auto-unregister from if the
     *                        listener dies
     * @param listener        the target listener to wrap weakly
     */
    public WeakValueChangeListener(ObservableValue<T> observableValue, ValueChangeListener<T> listener) {
        this.observableValue = observableValue;
        weakReference = new WeakReference<>(listener);
    }

    @Override
    public void onChange(T oldValue, T newValue) {
        ValueChangeListener<T> listener = weakReference.get();

        if (listener == null) {
            if (observableValue != null)
                observableValue.removeListener(this);
            return;
        }
        listener.onChange(oldValue, newValue);
    }

}
