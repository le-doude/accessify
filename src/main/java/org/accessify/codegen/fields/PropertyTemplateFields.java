package org.accessify.codegen.fields;

import org.apache.velocity.VelocityContext;

import java.beans.PropertyDescriptor;

/**
 * Created by edouard on 14/06/18.
 */
public enum PropertyTemplateFields {

    PACKAGE("package") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return (property.getWriteMethod() != null ?
                property.getWriteMethod().getDeclaringClass().getPackage().getName() :
                null);
        }
    },
    HANDLED_TYPE_NAME("handledTypeName") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return (property.getWriteMethod() != null ?
                property.getWriteMethod().getDeclaringClass().getSimpleName() :
                null);
        }
    },
    HANDLER_TYPE("handlerClassName") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return String
                .format(PropertyTemplateFields.PROPERTY_HANDLER_CLASSNAME_PATTERN,
                    PROPERTY.extractFieldValue(property),
                    HANDLED_TYPE_NAME.extractFieldValue(property));
        }
    },
    PROPERTY_RETURN_TYPE("propertyReturnType") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return property.getPropertyType().getCanonicalName();
        }
    },
    SETTER("setter") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return property.getWriteMethod().getName();
        }
    },
    GETTER("getter") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return property.getReadMethod().getName();
        }
    },
    PROPERTY("property") {
        @Override protected String extractFieldValue(PropertyDescriptor property) {
            return property.getName();
        }
    };
    public static final String PROPERTY_HANDLER_CLASSNAME_PATTERN = "%s_%sPropertyHandler";


    private final String element;

    PropertyTemplateFields(String name) {
        this.element = name;
    }

    public String getElement() {
        return element;
    }

    protected abstract String extractFieldValue(PropertyDescriptor property);

    public void writeToContext(VelocityContext context, PropertyDescriptor property){
        context.put(this.getElement(), extractFieldValue(property));
    }

}
