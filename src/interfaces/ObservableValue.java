package interfaces;

import java.util.Collection;

public interface ObservableValue<T> {
    void setValue(T newValue);

    T getValue();

    boolean addListener(ValueChangeListener<T> listener);

    boolean addListener(@SuppressWarnings("unchecked") ValueChangeListener<T>... listeners);

    boolean addListeners(Collection<? extends ValueChangeListener<T>> listeners);

    boolean removeListener(ValueChangeListener<T> listener);

    boolean removeListener(Collection<? extends ValueChangeListener<T>> listeners);

    boolean removeListener(@SuppressWarnings("unchecked") ValueChangeListener<T>... listeners);

    void removeAllListeners();
}
