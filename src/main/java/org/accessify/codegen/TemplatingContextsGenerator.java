package org.accessify.codegen;

import org.accessify.annotations.HandledType;
import org.accessify.codegen.data.PropertyHandlerContext;
import org.accessify.codegen.data.TemplateType;
import org.accessify.codegen.fields.ObjectHandlerFields;
import org.accessify.codegen.fields.PropertyTemplateFields;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by edouard on 14/06/12.
 */
class TemplatingContextsGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TemplatingContextsGenerator.class);

    public static List<PropertyHandlerContext> generatePropertyHandlersContexts(Class<?> clazz)
        throws IntrospectionException {
        if (clazz.isAnnotationPresent(HandledType.class)) {
            PropertyDescriptor[] properties =
                Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            ArrayList<PropertyHandlerContext> contexts = new ArrayList<>(properties.length);
            VelocityContext temp;
            LOG.debug("Found {} properties", properties.length);
            for (PropertyDescriptor property : properties) {
                if (!property.getPropertyType().isAnnotationPresent(HandledType.class)) {
                    temp = toContext(property);
                    if (temp != null) {
                        contexts.add(PropertyHandlerContext.make().setContext(temp)
                            .setTemplate(TemplateType.normal));
                    }
                } else {
                    temp = toContext(property);
                    if (temp != null) {
                        contexts.add(PropertyHandlerContext.make().setContext(temp)
                            .setTemplate(TemplateType.embeddedHandledType));
                    }
                }
            }
            return contexts;
        } else {
            return Collections.emptyList();
        }
    }

    public static VelocityContext generateObjectHandlerContext(Class<?> type) {
        if (type.isAnnotationPresent(HandledType.class)) {
            VelocityContext context = new VelocityContext();
            for (ObjectHandlerFields ohtf : ObjectHandlerFields.values()) {
                ohtf.writeToContext(context, type);
            }
            return context;
        } else {
            return null;
        }
    }

    private static void addToEmbeddedHandlers(PropertyDescriptor property) {
        //TODO: Actually figure a way to embed HandledTypes properties
    }

    public static VelocityContext toContext(PropertyDescriptor property) {
        if (property != null && !StringUtils.equalsIgnoreCase("class", property.getName())) {
            VelocityContext context = new VelocityContext();
            for (PropertyTemplateFields fields : PropertyTemplateFields.values()) {
                fields.writeToContext(context, property);
            }
            return context;
        } else {
            return null;
        }
    }
}
