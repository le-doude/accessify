package org.accessify.processor;

import org.accessify.annotations.HandledType;
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
public class PropertyReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReader.class);

    protected static final String PACKAGE = "package";
    protected static final String ENTITY = "entity";
    protected static final String HANDLER_TYPE = "handler_type";
    protected static final String VALUE = "value";
    protected static final String SETTER = "setter";
    protected static final String GETTER = "getter";
    protected static final String PROPERTY = "property";

    public static List<VelocityContext> generateHandlerContexts(Class<?> clazz) throws IntrospectionException {
        if (clazz.isAnnotationPresent(HandledType.class)) {
            System.out.println("Found the annotation");
            PropertyDescriptor[] properties = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            ArrayList<VelocityContext> contexts = new ArrayList<>(properties.length);
            VelocityContext temp;
            LOGGER.debug("Found {} properties", properties.length);
            for (PropertyDescriptor property : properties) {
                temp = toContext(property);
                if (temp != null) {
                    contexts.add(temp);
                }
            }
            return contexts;
        } else {
            return Collections.emptyList();
        }
    }

    public static VelocityContext toContext(PropertyDescriptor property) {
        if (property != null && !StringUtils.equalsIgnoreCase("class", property.getName())) {
            return toContext(property, property.getPropertyType(), property.getReadMethod(), property.getWriteMethod());
        } else {
            return null;
        }
    }

    private static VelocityContext toContext(PropertyDescriptor property, Class<?> propertyType, Method readMethod, Method writeMethod) {
        if (property != null && propertyType != null && readMethod != null && writeMethod != null) {
            Class<?> declaringClass = writeMethod.getDeclaringClass();
            return toContextImpl(
                    declaringClass.getName(),
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

    private static VelocityContext toContextImpl(String enclosingClass, String name, String packageName, String typeName, String getter, String setter) {
        if (StringUtils.isNoneBlank(enclosingClass, name, packageName, typeName, getter, setter)) {
            LOGGER.debug("{} {} {} {} {} {}", enclosingClass, name, packageName, typeName, getter, setter);
            VelocityContext context = new VelocityContext();
            context.put(PROPERTY, name);
            context.put(GETTER, getter);
            context.put(SETTER, setter);
            context.put(VALUE, typeName);
            context.put(ENTITY, enclosingClass);
            context.put(PACKAGE, packageName);
            context.put(HANDLER_TYPE, String.format("%s_%sPropertyHandler", name, enclosingClass));
            return context;
        } else {
            System.out.println("At least one necessary is blank.");
            LOGGER.debug("At least one necessary is blank.");
            return null;
        }
    }


}
