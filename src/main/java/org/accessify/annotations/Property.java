package org.accessify.annotations;

import java.lang.annotation.*;

/**
 * Created by edouard on 14/06/11.
 */

public @interface Property {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface Getter{
        String name();
        Class<?> type() default None.class;
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface Setter{
        String name();
        Class<?> type() default None.class;
    }


    public static class None {
    }
}
