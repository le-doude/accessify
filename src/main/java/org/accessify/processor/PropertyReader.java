package org.accessify.processor;

import org.accessify.annotations.HandledType;
import org.accessify.annotations.Property;
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
public class PropertyReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReader.class);

    public static List<VelocityContext> generateHandlerContexts(Class<?> clazz) throws IntrospectionException {
//        if (clazz.isAnnotationPresent(HandledType.class)) {
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
//        }else {
//            System.out.println("did notFound the annotation");
//            return Collections.emptyList();
//        }
    }

    public static VelocityContext toContext(PropertyDescriptor property) {
        if (property != null) {

            try {
                return toContextImpl(
                        property.getWriteMethod().getDeclaringClass().getName(),
                        property.getName(),
                        property.getPropertyType().getPackage().getName(),
                        property.getPropertyType().getSimpleName(),
                        property.getReadMethod().getName(),
                        property.getWriteMethod().getName()
                );
            } catch (Exception e) {
                System.out.println(property.getName());
                return null;
            }
        }
        else {
            return null;
        }
    }

    private static VelocityContext toContextImpl(String enclosingClass, String name, String packageName, String typeName, String getter, String setter) {
        if (StringUtils.isNoneBlank(enclosingClass, name, packageName, typeName, getter, setter)) {
            LOGGER.debug("{} {} {} {} {} {}", enclosingClass, name, packageName, typeName, getter, setter);
            VelocityContext context = new VelocityContext();
            context.put("package", packageName);
            context.put("entity", enclosingClass);
            context.put("handler_type", String.format("%s_%sPropertyHandler", name, enclosingClass));
            context.put("value", typeName);
            context.put("setter", setter);
            context.put("getter", getter);
            context.put("property", name);
            return context;
        } else {
            System.out.println("At least one necessary is blank.");
            LOGGER.debug("At least one necessary is blank.");
            return null;
        }
    }


}
