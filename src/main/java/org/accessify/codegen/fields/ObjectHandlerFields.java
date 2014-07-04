package org.accessify.codegen.fields;

import org.apache.commons.lang3.StringUtils;
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
                PropertyDescriptor[] descriptors =
                    Introspector.getBeanInfo(type).getPropertyDescriptors();
                ArrayList<String> properties = new ArrayList<>();
                for (PropertyDescriptor descriptor : descriptors) {
                    if (descriptor != null && !StringUtils
                        .equalsIgnoreCase("class", descriptor.getName())) {
                        properties.add(String
                            .format(PropertyTemplateFields.PROPERTY_HANDLER_CLASSNAME_PATTERN,
                                descriptor.getName(),
                                simpleName));
                    }
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

    private final String element;

    ObjectHandlerFields(java.lang.String name) {
        this.element = name;
    }

    public void writeToContext(VelocityContext context, Class type) {
        context.put(element, extractFieldValue(type));
    }

    public String getElement() {
        return element;
    }

    public abstract Object extractFieldValue(Class type);

}
