package org.accessify.annotations;

import java.lang.annotation.*;

/**
 * Created by edouard on 14/06/12.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface HandledType {

    String handlerName() default "";

}
