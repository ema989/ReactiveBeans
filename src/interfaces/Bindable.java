package interfaces;

import java.util.Arrays;
import java.util.Collection;

public interface Bindable<T> extends ObservableValue<T> {
    boolean bindWith(Bindable<T> bean);

    boolean unbind(Bindable<T> bean);

    void unbindAll();

    // Helper methods

    default boolean bindWith(Collection<? extends Bindable<T>> beans) {
        boolean modified = false;
        if (beans != null)
            for (Bindable<T> bean : beans)
                if (bindWith(bean))
                    modified = true;
        return modified;
    }

    @SuppressWarnings("unchecked")
    default boolean bindWith(Bindable<T>... beans) {
        return bindWith(Arrays.asList(beans));
    }

    default boolean unbind(Collection<? extends Bindable<T>> beans) {
        boolean modified = false;
        if (beans != null)
            for (Bindable<T> bean : beans)
                if (unbind(bean))
                    modified = true;
        return modified;
    }

    @SuppressWarnings("unchecked")
    default boolean unbind(Bindable<T>... beans) {
        return unbind(Arrays.asList(beans));
    }
}
