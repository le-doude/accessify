package org.accessify.codegen.fields;

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
    ENTITY("entity") {
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
                    PACKAGE.extractFieldValue(property),
                    ENTITY.extractFieldValue(property));
        }
    },
    VALUE("value") {
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


    private final String name;

    PropertyTemplateFields(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }

    protected abstract String extractFieldValue(PropertyDescriptor property);

}
