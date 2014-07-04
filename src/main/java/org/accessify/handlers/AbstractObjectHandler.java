package org.accessify.handlers;

import org.accessify.utils.ConfigurationUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by edouard on 14/06/11.
 */
public abstract class AbstractObjectHandler<T> implements ObjectHandler<T> {

    private final Map<String, PropertyHandler<T, ?>> handlers;
    private final HandlingFactory
        factory = HandlingFactory.get(ConfigurationUtils.FACTORY_INSTANCE_NAME);

    public AbstractObjectHandler(PropertyHandler<T, ?>... handlers) {
        LinkedHashMap<String, PropertyHandler<T, ?>> map = new LinkedHashMap<>();
        for (PropertyHandler<T, ?> handler : handlers) {
            if (handler != null) {
                map.put(handler.property(), handler);
            }
        }
        this.handlers = Collections.unmodifiableMap(map);
    }

    @Override
    public <V> V get(T instance, String propertyName) {
        String[] split = propertyName.split(ConfigurationUtils.FIELD_PATH_SEPARATOR);
        if (split.length > 1) {
            return this.getEmbedded(instance, split);
        } else {
            return this.<V>obtainHandler(propertyName).get(instance);
        }
    }

    @SuppressWarnings("unchecked")
    public <V> PropertyHandler<T, V> obtainHandler(String propertyName) {
        PropertyHandler<T, ?> handler = this.handlers.get(propertyName);
        if (handler == null) {
            throw new NoSuchFieldError(propertyName);
        }
        return (PropertyHandler<T, V>) handler;
    }

    @Override
    public <V> void set(T instance, String propertyName, V value) {
        String[] split = propertyName.split(ConfigurationUtils.FIELD_PATH_SEPARATOR);
        if (split.length > 1) {
            this.setEmbedded(instance, value, split);
        } else {
            this.<V>obtainHandler(propertyName).set(instance, value);
        }
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

    protected <V> void setEmbedded(T instance, V value, String... path) {
        withEmbeddedHandlerContext(PropHandlerAction.set, instance, value, path);
    }

    protected <V> V getEmbedded(T instance, String... path) {
        return withEmbeddedHandlerContext(PropHandlerAction.get, instance, null, path);
    }

    @SuppressWarnings("unchecked")
    private <V> V withEmbeddedHandlerContext(PropHandlerAction action, T instance,
        V value, String... path) {
        PropertyHandler th = this.obtainHandler(path[0]);
        Object temp = instance;
        ObjectHandler handler;
        int lastIndex = path.length - 1;
        for (int i = 1; i < lastIndex; i++) {
            if (th instanceof EmbeddedPropertyHandler) {
                handler = ((EmbeddedPropertyHandler) th).getHandler();
                temp = th.get(temp);
                if (temp == null) {
                    temp = handler.newInstance();
                    th.set(path[i], temp);
                }
                th = handler.obtainHandler(path[i]);
            } else {
                throw new NoSuchFieldError();
            }
        }
        return (V) action.apply(th, temp, value);
    }

    private static enum PropHandlerAction {
        set {
            @Override public <T, V> V apply(PropertyHandler<T, V> handler, T instance, V value) {
                handler.set(instance, value);
                return value;
            }
        },
        get {
            @Override public <T, V> V apply(PropertyHandler<T, V> handler, T instance, V value) {
                return handler.get(instance);
            }
        };

        public abstract <T, V> V apply(PropertyHandler<T, V> handler, T instance, V value);
    }

}
