package org.accessify.handlers; /**
 * Created by edouard on 14/06/11.
 */


/**
 * Handles calls to a specific property (of type V) of an object of type T
 *
 * @param <T>
 * @param <V>
 */
public interface PropertyHandler<T, V> {

    void set(T instance, V value);

    V get(T instance);

    String property();

    Class<V> type();

}
