package org.accessify.handlers;

import org.accessify.utils.ConfigurationUtils;

/**
 * Created by edouard on 14/07/04.
 */
public interface EmbeddedPropertyHandler<T> {

    static final HandlingFactory factory = HandlingFactory.get(ConfigurationUtils.FACTORY_INSTANCE_NAME);

    ObjectHandler<T> getHandler();

}
