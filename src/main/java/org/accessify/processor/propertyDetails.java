package org.accessify.processor;

import org.apache.velocity.VelocityContext;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

/**
 * Created by edouard on 14/06/12.
 */
class propertyDetails {

    private String packageName;
    private String handlerClassName;
    private String entity;
    private String value;
    private String setter;
    private String getter;
    private String property;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getHandlerClassName() {
        return handlerClassName;
    }

    public void setHandlerClassName(String handlerClassName) {
        this.handlerClassName = handlerClassName;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public VelocityContext toContext() {
        VelocityContext context = new VelocityContext();
        context.put("package", packageName);
        context.put("handler_type", handlerClassName);
        context.put("entity", entity);
        context.put("value", value);
        context.put("setter", setter);
        context.put("getter", getter);
        context.put("property", property);
        return context;
    }

    public boolean isComplete(){
        return isNoneBlank(packageName, handlerClassName, entity, value, setter, getter, property);
    }


}
