package org.accessify.handlers;

import java.util.Collection;
import java.util.Map;

/**
 * Created by edouard on 14/06/11.
 */
public interface ObjectHandler<T> {

    <V> V get(T instance, String propertyName);

    <V> void set(T instance, String propertyName, V value);

    Map<String, Object> getAll(T instance);

    void setAll(T instance, Map<String, Object> values);

    Collection<String> properties();

    Class<T> handledType();

    T newInstance();

    <V> PropertyHandler<T, V> obtainHandler(String s);
}
