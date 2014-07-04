package org.accessify.handlers;

/**
 * Created by edouard on 14/06/11.
 */
public abstract class AbstractPropertyHandler<T, V> implements PropertyHandler<T, V> {

    private final String property;

    public AbstractPropertyHandler(String property) {
        this.property = property;
    }

    @Override
    public String property() {
        return property;
    }
}
