package org.accessify.annotations;

import org.accessify.handlers.ToStringConverter;

import java.lang.annotation.*;

/**
 * Created by edouard on 14/06/11.
 */

public @interface Property {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface Getter {
        String name();
    }


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface Setter {
        String name();
    }


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface HandledMap {
        Class<? extends ToStringConverter> keyBridge() default IdentityStringBridge.class;

        Class<?> contentAs();

        boolean contentIsHandledType() default false;
    }


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public static @interface HandledList {
        Class<?> contentAs();

        boolean contentIsHandledType() default false;
    }


    public static final class IdentityStringBridge implements ToStringConverter<String> {
        @Override public String apply(String s) {
            return s;
        }
    }

}
