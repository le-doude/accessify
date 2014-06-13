package org.accessify.processor;

import org.accessify.annotations.HandledType;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

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

    public static List<VelocityContext> generateHandlerContexts(Class<?> clazz) throws IntrospectionException {
        if (clazz.isAnnotationPresent(HandledType.class)) {
            PropertyDescriptor[] properties = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            ArrayList<VelocityContext> contexts = new ArrayList<>(properties.length);
            VelocityContext temp;
            for (PropertyDescriptor property : properties) {
                temp = toContext(property);
                if (temp != null) {
                    contexts.add(temp);
                }
            }
            return contexts;
        }else {
            return Collections.emptyList();
        }
    }

    public static VelocityContext toContext(PropertyDescriptor property) {
        return toContextImpl(property.getWriteMethod().getDeclaringClass().getName(), property.getName(), property.getPropertyType().getPackage().getName(), property.getPropertyType().getSimpleName(), property.getReadMethod().getName(), property.getWriteMethod().getName());
    }

    private static VelocityContext toContextImpl(String enclosingClass, String name, String packageName, String typeName, String getter, String setter) {
        if (StringUtils.isNoneBlank(enclosingClass, name, packageName, typeName, getter, setter)) {
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
            return null;
        }
    }


}
