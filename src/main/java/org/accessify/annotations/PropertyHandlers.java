package org.accessify.annotations;

import org.accessify.handlers.PropertyHandler;

import java.lang.annotation.*;

/**
 * Created by edouard on 14/06/11.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Inherited
public @interface PropertyHandlers {
    Class<? extends PropertyHandler<?,?>>[] value();
}
