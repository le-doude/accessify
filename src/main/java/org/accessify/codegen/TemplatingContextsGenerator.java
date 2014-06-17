package org.accessify.codegen;

import org.accessify.annotations.HandledType;
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
public class TemplatingContextsGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TemplatingContextsGenerator.class);

    static class PropertyContextConstants {

        static final String PACKAGE = "package";
        static final String ENTITY = "entity";
        static final String HANDLER_TYPE = "handlerClassName";
        static final String VALUE = "value";
        static final String SETTER = "setter";
        static final String GETTER = "getter";
        static final String PROPERTY = "property";
        static final String PROPERTY_HANDLER_CLASSNAME_PATTERN = "%s_%sPropertyHandler";
    }

    static class TypeContextConstants {
        static final String PACKAGE = "package";
        static final String PROPERTY_HANDLERS = "propertyHandlers";
        static final String HANDLER_CLASS_NAME = "handlerClassName";
        static final String ENITITY_CLASS_NAME = "enitityClassName";
    }

    public static List<VelocityContext> generatePropertyHandlersContexts(Class<?> clazz) throws IntrospectionException {
        if (clazz.isAnnotationPresent(HandledType.class)) {
            PropertyDescriptor[] properties = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
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

    public static VelocityContext generateObjectHandlerContext(Class<?> type, List<VelocityContext> propertyHandlersContexts) {
        if (type.isAnnotationPresent(HandledType.class)) {
            //TODO
            throw new NotImplementedException();
//            try {
//                Constructor<?> constructor = type.getConstructor();
//
//            } catch (NoSuchMethodException e) {
//                throw new NoArgConstructorRequired(e);
//            }
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
            return toContext(property, property.getPropertyType(), property.getReadMethod(), property.getWriteMethod());
        } else {
            return null;
        }
    }

    static VelocityContext toContext(PropertyDescriptor property, Class<?> propertyType, Method readMethod, Method writeMethod) {
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

    static VelocityContext toContextImpl(String enclosingClass, String name, String packageName, String typeName, String getter, String setter) {
        if (StringUtils.isNoneBlank(enclosingClass, name, packageName, typeName, getter, setter)) {
            LOG.debug("{} {} {} {} {} {}", enclosingClass, name, packageName, typeName, getter, setter);
            VelocityContext context = new VelocityContext();
            context.put(PropertyContextConstants.PROPERTY, name);
            context.put(PropertyContextConstants.GETTER, getter);
            context.put(PropertyContextConstants.SETTER, setter);
            context.put(PropertyContextConstants.VALUE, typeName);
            context.put(PropertyContextConstants.ENTITY, enclosingClass);
            context.put(PropertyContextConstants.PACKAGE, packageName);
            context.put(PropertyContextConstants.HANDLER_TYPE, String.format(PropertyContextConstants.PROPERTY_HANDLER_CLASSNAME_PATTERN, name, enclosingClass));
            return context;
        } else {
            LOG.debug("At least one necessary is blank.");
            return null;
        }
    }


}
