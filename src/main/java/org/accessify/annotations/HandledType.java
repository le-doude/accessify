package org.accessify.annotations;

/**
 * Created by edouard on 14/06/12.
 */
public @interface HandledType {

    String handlerName() default "";

}
