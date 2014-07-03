package org.accessify.codegen;

import org.accessify.annotations.HandledType;
import org.accessify.codegen.fields.ObjectHandlerFields;
import org.accessify.codegen.fields.PropertyTemplateFields;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by edouard on 14/06/12.
 */
class TemplatingContextsGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TemplatingContextsGenerator.class);

    public static List<VelocityContext> generatePropertyHandlersContexts(Class<?> clazz)
        throws IntrospectionException {
        if (clazz.isAnnotationPresent(HandledType.class)) {
            PropertyDescriptor[] properties =
                Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            ArrayList<VelocityContext> contexts = new ArrayList<>(properties.length);
            VelocityContext temp;
            LOG.debug("Found {} properties", properties.length);
            for (PropertyDescriptor property : properties) {
                if (!property.getPropertyType().isAnnotationPresent(HandledType.class)) {
                    temp = toContext(property);
                    if (temp != null) {
                        contexts.add(temp);
                    }
                } else {
                    addToEmbeddedHandlers(property);
                }
            }
            return contexts;
        } else {
            return Collections.emptyList();
        }
    }

    public static VelocityContext generateObjectHandlerContext(Class<?> type,
        List<VelocityContext> propertyHandlersContexts) {
        if (type.isAnnotationPresent(HandledType.class)) {
            VelocityContext context = new VelocityContext();
            //            context.put(ObjectHandlerTemplateFields.ENITITY_CLASS_NAME, type.getSimpleName());
            //            context.put(ObjectHandlerTemplateFields.HANDLER_CLASS_NAME, String
            //                .format(ObjectHandlerTemplateFields.OBJECT_HANDLER_CLASSNAME_PATTERN,
            //                    type.getSimpleName()));
            //            context.put(ObjectHandlerTemplateFields.PACKAGE, type.getPackage().getName());
            //
            //
            //            ArrayList<String> handlersQualifiedName =
            //                new ArrayList<>(propertyHandlersContexts.size());
            //            for (VelocityContext c : propertyHandlersContexts) {
            //                //use fully qualified name
            //                handlersQualifiedName.add(c.get(PropertyTemplateFields.PACKAGE) + "." + c
            //                    .get(PropertyTemplateFields.HANDLER_TYPE));
            //            }
            //            context.put(ObjectHandlerTemplateFields.PROPERTY_HANDLERS, handlersQualifiedName);

            for (ObjectHandlerFields ohtf : ObjectHandlerFields.values()) {
                ohtf.writeToContext(context, type);
            }
            return context;
        } else {
            return null;
        }
    }

    private static void addToEmbeddedHandlers(PropertyDescriptor property) {
        //TODO
        throw new NotImplementedException("addToEmbeddedHandlers");
    }

    static VelocityContext toContext(PropertyDescriptor property) {
        if (property != null && !StringUtils.equalsIgnoreCase("class", property.getName())) {
            return toContext(property, property.getPropertyType(), property.getReadMethod(),
                property.getWriteMethod());
        } else {
            return null;
        }
    }

    static VelocityContext toContext(PropertyDescriptor property, Class<?> propertyType,
        Method readMethod, Method writeMethod) {
        if (property != null && propertyType != null && readMethod != null && writeMethod != null) {
            Class<?> declaringClass = writeMethod.getDeclaringClass();
            return toContextImpl(
                declaringClass.getSimpleName(),
                property.getName(),
                declaringClass.getPackage().getName(),
                propertyType.getSimpleName(),
                readMethod.getName(),
                writeMethod.getName()
            );
        } else {
            return null;
        }
    }

    static VelocityContext toContextImpl(String enclosingClass, String name, String packageName,
        String typeName, String getter, String setter) {
        if (StringUtils.isNoneBlank(enclosingClass, name, packageName, typeName, getter, setter)) {
            LOG.debug("{} {} {} {} {} {}", enclosingClass, name, packageName, typeName, getter,
                setter);
            VelocityContext context = new VelocityContext();
            context.put(PropertyTemplateFields.PROPERTY, name);
            context.put(PropertyTemplateFields.GETTER, getter);
            context.put(PropertyTemplateFields.SETTER, setter);
            context.put(PropertyTemplateFields.VALUE, typeName);
            context.put(PropertyTemplateFields.ENTITY, enclosingClass);
            context.put(PropertyTemplateFields.PACKAGE, packageName);
            context.put(PropertyTemplateFields.HANDLER_TYPE, String
                .format(PropertyTemplateFields.PROPERTY_HANDLER_CLASSNAME_PATTERN, name,
                    enclosingClass));
            return context;
        } else {
            LOG.debug("At least one necessary is blank.");
            return null;
        }
    }


}
