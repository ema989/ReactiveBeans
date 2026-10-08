package interfaces;

import java.util.Arrays;
import java.util.Collection;

public interface ObservableValue<T> {
    boolean setValue(T newValue);

    T getValue();

    boolean addListener(ValueChangeListener<T> listener);

    boolean removeListener(ValueChangeListener<T> listener);

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
