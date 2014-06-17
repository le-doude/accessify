package org.accessify.handlers;

import java.util.HashMap;

/**
 * Created by edouard on 14/06/12.
 */
public class HandlingFactory {


    private HandlingFactory(){}

    public static HandlingFactory get(){
        return new HandlingFactory();
    }

    final HashMap<Class, ObjectHandler> handlers = new HashMap<>();

    public void register(ObjectHandler handler) {
        handlers.put(handler.handledType(), handler);
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectHandler<T> handler(Class<T> clazz) {
        return (ObjectHandler<T>) handlers.get(clazz);
    }

}
