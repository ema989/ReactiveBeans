package interfaces;

import java.util.Arrays;
import java.util.Collection;

/**
 * Extends {@link ObservableValue} to support unidirectional binding
 * capabilities,
 * allowing values to be synchronized across multiple bindable instances.
 *
 * @param <T> the type of the held value
 */
public interface Bindable<T> extends ObservableValue<T> {

    /**
     * Binds this bean with another target bindable. The target's value is
     * immediately
     * synchronized with this bean's current value, and future updates to this bean
     * will automatically propagate to the target.
     *
     * @param bean the target bindable to bind with
     * @return {@code true} if the binding was successfully established,
     *         {@code false} otherwise
     */
    boolean bindWith(Bindable<T> bean);

    /**
     * Unbinds a previously bound target bean.
     *
     * @param bean the target bindable to unbind
     * @return {@code true} if the binding was removed, {@code false} if it was not
     *         present
     */
    boolean unbind(Bindable<T> bean);

    /**
     * Removes all bindings associated with this bindable instance.
     */
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
