package org.accessify.codegen;

import org.accessify.annotations.HandledType;
import org.accessify.annotations.Property;
import org.accessify.codegen.data.PropertyHandlerContext;
import org.accessify.codegen.data.TemplateType;
import org.accessify.codegen.fields.ObjectHandlerFields;
import org.accessify.codegen.fields.PropertyTemplateFields;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

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
            HashSet<String> propertyNames = new HashSet<>();
            VelocityContext temp;
            LOG.debug("Found {} properties", properties.length);
            for (PropertyDescriptor property : properties) {
                if (addContextToList(contexts, property)) {
                    propertyNames.add(property.getName());
                }
            }
            //ADD custom properties created through Getter and Setter annotations
            ArrayList<PropertyDescriptor> propertiesList = processCustomProperties(clazz);
            for (PropertyDescriptor property : propertiesList) {
                if (!propertyNames.contains(property.getName())) {
                    addContextToList(contexts, property);
                }
            }
            return contexts;
        } else {
            throw new IllegalArgumentException("Needs to be marked as @HandledType");
        }
    }

    private static boolean addContextToList(ArrayList<PropertyHandlerContext> contexts,
        PropertyDescriptor property) {
        VelocityContext temp;
        if (!property.getPropertyType().isAnnotationPresent(HandledType.class)) {
            temp = toContext(property);
            if (temp != null) {
                return contexts.add(PropertyHandlerContext.make().setContext(temp)
                    .setTemplate(TemplateType.normal));
            }
        } else {
            temp = toContext(property);
            if (temp != null) {
                return contexts.add(PropertyHandlerContext.make().setContext(temp)
                    .setTemplate(TemplateType.embeddedHandledType));
            }
        }
        return false;
    }

    private static ArrayList<PropertyDescriptor> processCustomProperties(Class<?> clazz)
        throws IntrospectionException {
        Reflections r = new Reflections(clazz, new MethodAnnotationsScanner());
        Set<Method> getters = r.getMethodsAnnotatedWith(Property.Getter.class);
        Set<Method> setters = r.getMethodsAnnotatedWith(Property.Setter.class);

        HashMap<String, TempCustomProperty> propertiesMap = new HashMap<>();
        Property.Setter annotation;
        TempCustomProperty property;
        for (Method setter : setters) {
            annotation = setter.getAnnotation(Property.Setter.class);
            propertiesMap.put(annotation.name(),
                new TempCustomProperty().setName(annotation.name()).setSetter(setter));
        }
        for (Method getter : getters) {
            annotation = getter.getAnnotation(Property.Setter.class);
            property = propertiesMap.get(annotation.name());
            if (property != null) {
                propertiesMap.put(annotation.name(), property.setGetter(getter));
            } else {
                propertiesMap.put(annotation.name(),
                    new TempCustomProperty().setName(annotation.name()).setGetter(getter));
            }
        }
        ArrayList<PropertyDescriptor> p = new ArrayList<>(propertiesMap.size());
        for (TempCustomProperty tcp : propertiesMap.values()) {
            p.add(tcp.toDescriptor());
        }
        return p;
    }

    public static VelocityContext generateObjectHandlerContext(Class<?> type) {
        if (type.isAnnotationPresent(HandledType.class)) {
            VelocityContext context = new VelocityContext();
            for (ObjectHandlerFields ohtf : ObjectHandlerFields.values()) {
                ohtf.writeToContext(context, type);
            }
            return context;
        } else {
            throw new IllegalArgumentException("Needs to be marked as @HandledType");
        }
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

    private static class TempCustomProperty {

        private String name;
        private Method getter;
        private Method setter;

        public boolean isComplete() {
            return StringUtils.isNotEmpty(name) && getter != null && setter != null;
        }

        public String getName() {
            return name;
        }

        public TempCustomProperty setName(String name) {
            this.name = name;
            return this;
        }

        public Method getGetter() {
            return getter;
        }

        public TempCustomProperty setGetter(Method getter) {
            assert ArrayUtils.isEmpty(getter.getParameterTypes());
            assert getter.getReturnType() != null;
            this.getter = getter;
            return this;
        }

        public Method getSetter() {
            return setter;
        }

        public TempCustomProperty setSetter(Method setter) {
            assert ArrayUtils.isNotEmpty(setter.getParameterTypes());
            assert setter.getParameterTypes().length == 1;
            this.setter = setter;
            return this;
        }

        public PropertyDescriptor toDescriptor() throws IntrospectionException {
            if (isComplete()) {
                return new PropertyDescriptor(getName(), getGetter(), getSetter());
            } else {
                throw new IllegalArgumentException(
                    String.format("%s is not a complete custom property %s missing",
                        this.getName(),
                        (getter == null && setter == null ? "GETTER and SETTER" :
                            (getter == null ? "GETTER" : "SETTER")
                        )
                    )
                );
            }
        }
    }
}
