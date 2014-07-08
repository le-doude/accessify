package org.accessify.handlers.list;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.accessify.handlers.*;
import org.accessify.utils.ConfigurationUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by edouard on 14/07/08.
 */
public abstract class AbstractListObjectHandler<T extends List<CONTENT>, CONTENT>
    extends AbstractObjectHandler<T> {

    private final HandlingFactory factory =
        HandlingFactory.get(ConfigurationUtils.FACTORY_INSTANCE_NAME);

    public AbstractListObjectHandler() {
        super();
    }

    private AbstractListObjectHandler(PropertyHandler<T, ?>... handlers) {
        super(handlers);
    }

    @Override public Collection<String> properties() {
        return Arrays.asList("[index]");
    }

    protected abstract Class<CONTENT> contentType();

    protected abstract IndexValueHandler indexHandler(int index);

    private final LoadingCache<String, IndexValueHandler> _handlersCache = CacheBuilder.
        newBuilder().
        maximumSize(1000).
        weakKeys().
        weakValues().
        build(new CacheLoader<String, IndexValueHandler>() {
            @Override public IndexValueHandler load(String s) throws Exception {
                return indexHandler(Integer.valueOf(s));
            }
        });

    @SuppressWarnings("unchecked")
    @Override public final <V> PropertyHandler<T, V> obtainHandler(String s) {
        try {
            return (PropertyHandler<T, V>) _handlersCache.get(s);
        } catch (ExecutionException e) {
            throw new NoSuchFieldError(e.getMessage());
        }
    }

    protected class IndexValueHandler implements PropertyHandler<T, CONTENT> {

        private final int index;

        private IndexValueHandler(int index) {
            this.index = index;
        }

        @Override public void set(T instance, CONTENT value) {
            instance.set(index, value);
        }

        @Override public CONTENT get(T instance) {
            return instance.get(index);
        }

        @Override public String property() {
            return Integer.toString(index);
        }

        @Override public Class<CONTENT> type() {
            return contentType();
        }
    }


    protected class EmbeddedHandledTypeIndexValueHandler extends IndexValueHandler implements
        EmbeddedPropertyHandler<CONTENT> {

        private EmbeddedHandledTypeIndexValueHandler(int index) {
            super(index);
        }

        @Override public ObjectHandler<CONTENT> getHandler() {
            return factory.handler(contentType());
        }
    }

}
