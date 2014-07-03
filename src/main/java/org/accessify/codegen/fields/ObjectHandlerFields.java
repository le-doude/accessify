package org.accessify.codegen.fields;

import org.apache.velocity.VelocityContext;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;

/**
 * Created by edouard on 14/06/18.
 */
public enum ObjectHandlerFields {

    PACKAGE("package") {
        @Override public Object extractFieldValue(Class type) {
            return type.getPackage().getName();
        }
    },
    PROPERTY_HANDLERS("propertyHandlers") {
        @Override public Object extractFieldValue(Class type) {
            try {
                String simpleName = type.getSimpleName();
                String packName = type.getPackage().getName();
                PropertyDescriptor[] descriptors =
                    Introspector.getBeanInfo(type).getPropertyDescriptors();
                ArrayList<String> properties = new ArrayList<>();
                for (PropertyDescriptor descriptor : descriptors) {
                    properties.add(packName + "." + String
                        .format(PropertyTemplateFields.PROPERTY_HANDLER_CLASSNAME_PATTERN,
                            descriptor.getName(),
                            simpleName));
                }
                return properties;
            } catch (IntrospectionException ignored) {
            }
            return null;
        }
    },
    HANDLER_CLASS_NAME("handlerClassName") {
        @Override public Object extractFieldValue(Class type) {
            return String.format(
                ObjectHandlerFields.OBJECT_HANDLER_CLASSNAME_PATTERN,
                type.getSimpleName());
        }
    },
    ENITITY_CLASS_NAME("enitityClassName") {
        @Override public Object extractFieldValue(Class type) {
            return type.getSimpleName();
        }
    };

    static final String OBJECT_HANDLER_CLASSNAME_PATTERN = "%sObjectHandler";

    private final String name;

    ObjectHandlerFields(java.lang.String name) {
        this.name = name;
    }

    public void writeToContext(VelocityContext context, Class type) {
        context.put(name, extractFieldValue(type));
    }

    public String getName() {
        return name;
    }

    public abstract Object extractFieldValue(Class type);

}
