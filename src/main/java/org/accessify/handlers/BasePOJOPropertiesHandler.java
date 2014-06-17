package org.accessify.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by edouard on 14/06/11.
 */
public abstract class BasePOJOPropertiesHandler<T> implements ObjectHandler<T> {

    private final Map<String, PropertyHandler<T, ?>> handlers;

    public BasePOJOPropertiesHandler(PropertyHandler<T, ?>... handlers) {
        LinkedHashMap<String, PropertyHandler<T, ?>> map = new LinkedHashMap<String, PropertyHandler<T, ?>>();
        for (PropertyHandler<T, ?> handler : handlers) {
            if (handler != null) map.put(handler.property(), handler);
        }
        this.handlers = Collections.unmodifiableMap(map);
    }

    @Override
    public <V> V get(T instance, String propertyName) {
        return this.<V>obtainHandler(propertyName).get(instance);
    }

    @SuppressWarnings("unchecked")
    private <V> PropertyHandler<T, V> obtainHandler(String propertyName) {
        PropertyHandler<T, ?> handler = this.handlers.get(propertyName);
        if (handler == null) {
            throw new NoSuchFieldError(propertyName);
        }
        return (PropertyHandler<T, V>) handler;
    }

    @Override
    public <V> void set(T instance, String propertyName, V value) {
        this.<V>obtainHandler(propertyName).set(instance, value);
    }

    @Override
    public Map<String, Object> getAll(T instance) {
        LinkedHashMap<String, Object> all = new LinkedHashMap<String, Object>(this.handlers.size());
        for (PropertyHandler<T, ?> handler : this.handlers.values()) {
            all.put(handler.property(), handler.get(instance));
        }
        return Collections.unmodifiableMap(all);
    }

    @Override
    public void setAll(T instance, Map<String, Object> values) {
        for (Map.Entry<String, Object> pair : values.entrySet()) {
            this.obtainHandler(pair.getKey()).set(instance, pair.getValue());
        }
    }

    @Override
    public Collection<String> properties() {
        return Collections.unmodifiableCollection(this.handlers.keySet());
    }

}
