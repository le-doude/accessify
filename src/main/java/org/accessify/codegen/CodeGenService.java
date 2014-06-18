package org.accessify.codegen;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

/**
 * Created by edouard on 14/06/16.
 */
class CodeGenService {

    protected static final String VELOCITY_PROPERTIES_FILE = "velocity.properties";
    protected static final String PROPERTY_HANDLER_VELOCITY_TEMPLATE = "property_handler.vtl";
    protected static final String OBJECT_HANDLER_VELOCITY_TEMPLATE = "object_handler.vtl";

    private final Template propertyHandlerTemplate;
    private final VelocityEngine engine;
    private final Template objectHandlerTemplate;

    public CodeGenService() throws IOException {
        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource(VELOCITY_PROPERTIES_FILE);
        properties.load(url.openStream());
        engine = new VelocityEngine(properties);
        engine.init();
        propertyHandlerTemplate = engine.getTemplate(PROPERTY_HANDLER_VELOCITY_TEMPLATE);
        objectHandlerTemplate = engine.getTemplate(OBJECT_HANDLER_VELOCITY_TEMPLATE);
    }

    public void writePropertyHandler(VelocityContext context, Writer codeWriter) throws IOException {
        if (StringUtils.isNoneBlank(
                (String) context.get(PropertyTemplateFields.ENTITY),
                (String) context.get(PropertyTemplateFields.GETTER),
                (String) context.get(PropertyTemplateFields.SETTER),
                (String) context.get(PropertyTemplateFields.HANDLER_TYPE),
                (String) context.get(PropertyTemplateFields.PROPERTY),
                (String) context.get(PropertyTemplateFields.VALUE)
        )) {
            propertyHandlerTemplate.merge(context, codeWriter);
            codeWriter.flush();
        } else {
            throw new IllegalArgumentException("Missing fields in context");
        }
    }

    public void writeObjectHandler(VelocityContext context, Writer codeWriter) throws IOException {
        if(StringUtils.isNoneBlank(
                (String) context.get(ObjectHandlerTemplateFields.ENITITY_CLASS_NAME),
                (String) context.get(ObjectHandlerTemplateFields.HANDLER_CLASS_NAME),
                (String) context.get(ObjectHandlerTemplateFields.PACKAGE)
        ) && CollectionUtils.isNotEmpty((java.util.Collection) context.get(ObjectHandlerTemplateFields.PROPERTY_HANDLERS))){
            objectHandlerTemplate.merge(context, codeWriter);
            codeWriter.flush();
        }else{
            throw new IllegalArgumentException("Missing fields in context");
        }
    }


}
