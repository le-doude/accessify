package org.accessify.exceptions;

/**
 * Created by edouard on 14/06/17.
 */
public class NoArgConstructorRequired extends RuntimeException {
    public NoArgConstructorRequired(NoSuchMethodException e) {
        super(e);
    }
}
