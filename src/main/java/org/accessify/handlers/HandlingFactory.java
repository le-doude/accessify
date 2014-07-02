package org.accessify.handlers;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by edouard on 14/06/12.
 */
public class HandlingFactory {

    private static ConcurrentHashMap<String, HandlingFactory> factories = new ConcurrentHashMap<>();
    final HashMap<Class, ObjectHandler> handlers = new HashMap<>();

    private HandlingFactory() {
    }

    public static HandlingFactory get(String instanceName) {
        return factories.get(instanceName);
    }

    public static boolean create(String instanceName, ObjectHandler... handlers) {
        return factories.putIfAbsent(instanceName, new HandlingFactory().register(handlers))
            != null;
    }

    private HandlingFactory register(ObjectHandler... ohs) {
        for (ObjectHandler handler : ohs) {
            handlers.put(handler.handledType(), handler);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectHandler<T> handler(Class<T> clazz) {
        return (ObjectHandler<T>) handlers.get(clazz);
    }
}
