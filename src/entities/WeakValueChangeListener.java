package entities;

import java.lang.ref.WeakReference;

import interfaces.ObservableValue;
import interfaces.ValueChangeListener;

public class WeakValueChangeListener<T> implements ValueChangeListener<T> {

    private final ObservableValue<T> observableValue;
    private final WeakReference<ValueChangeListener<T>> weakReference;

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
