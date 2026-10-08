package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import interfaces.Bindable;
import interfaces.ValueChangeListener;

public class BindableObservableValue<T> extends SimpleObservableValue<T> implements Bindable<T> {

    private Set<Bindable<T>> bindSet = new HashSet<>();
    private boolean isUpdating = false; // Loop protection flag

    public BindableObservableValue(T initialValue) {
        super(initialValue);
    }

    public BindableObservableValue(T initialValue, ValueChangeListener<T> listener) {
        super(initialValue, listener);
    }

    public BindableObservableValue(T initialValue, Collection<? extends ValueChangeListener<T>> listeners) {
        super(initialValue, listeners);
    }

    @SafeVarargs
    public BindableObservableValue(T initialValue, ValueChangeListener<T>... listeners) {
        super(initialValue, listeners);
    }

    @Override
    public boolean setValue(T newValue) {
        if (isUpdating)
            return false;

        isUpdating = true;

        try {
            if (!super.setValue(newValue))
                return false;

            if (!bindSet.isEmpty()) {
                for (Bindable<T> target : new ArrayList<>(bindSet)) {
                    try {
                        target.setValue(newValue);
                    } catch (Exception e) {
                        System.err.println("Bind propagation error: " + e.getMessage());
                    }
                }
            }
            return true;
        } finally {
            isUpdating = false;
        }
    }

    @Override
    public boolean bindWith(Bindable<T> bean) {
        Objects.requireNonNull(bean, "bean can't be null");

        if (bean == this)
            return false;

        boolean added = bindSet.add(bean);
        if (added)
            bean.setValue(this.getValue());
        return added;
    }

    @Override
    public boolean unbind(Bindable<T> bean) {
        return bindSet.remove(Objects.requireNonNull(bean, "bean can't be null"));
    }

    @Override
    public void unbindAll() {
        bindSet.clear();
    }

}
