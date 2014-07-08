package org.accessify.handlers;

/**
 * Created by edouard on 14/07/08.
 */
public interface KeyToStringConverter<T> {
    String apply(T t);

    T unapply(String s);
}
