package org.accessify.codegen;

import org.accessify.codegen.data.PropertyHandlerContext;
import org.accessify.codegen.fields.ObjectHandlerFields;
import org.accessify.codegen.fields.PropertyTemplateFields;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

/**
 * Created by edouard on 14/06/16.
 */
class CodeGenService {

    protected static final String VELOCITY_PROPERTIES_FILE = "velocity.properties";
    protected static final String PROPERTY_HANDLER_VELOCITY_TEMPLATE = "property_handler.vtl";
    protected static final String EMBEDDED_TYPE_PROPERTY_HANDLER_VELOCITY_TEMPLATE =
        "embedded_property_handler.vtl";
    protected static final String OBJECT_HANDLER_VELOCITY_TEMPLATE = "object_handler.vtl";

    private final Template propertyHandlerTemplate;
    private final Template embeddedTypePropertyHandlerTemplate;
    private final VelocityEngine engine;
    private final Template objectHandlerTemplate;

    public CodeGenService() throws IOException {
        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource(VELOCITY_PROPERTIES_FILE);
        properties.load(url.openStream());
        engine = new VelocityEngine(properties);
        engine.init();
        objectHandlerTemplate = engine.getTemplate(OBJECT_HANDLER_VELOCITY_TEMPLATE);
        propertyHandlerTemplate = engine.getTemplate(PROPERTY_HANDLER_VELOCITY_TEMPLATE);
        embeddedTypePropertyHandlerTemplate =
            engine.getTemplate(EMBEDDED_TYPE_PROPERTY_HANDLER_VELOCITY_TEMPLATE);
    }

    public void writePropertyHandler(PropertyHandlerContext context, Writer codeWriter)
        throws IOException {
        if (isNoneBlank(
            (String) context.getContext()
                .get(PropertyTemplateFields.HANDLED_TYPE_NAME.getElement()),
            (String) context.getContext().get(PropertyTemplateFields.GETTER.getElement()),
            (String) context.getContext().get(PropertyTemplateFields.SETTER.getElement()),
            (String) context.getContext().get(PropertyTemplateFields.HANDLER_TYPE.getElement()),
            (String) context.getContext().get(PropertyTemplateFields.PROPERTY.getElement()),
            (String) context.getContext()
                .get(PropertyTemplateFields.PROPERTY_RETURN_TYPE.getElement())
        )) {
            switch (context.getTemplate()) {
                case normal:
                    propertyHandlerTemplate.merge(context.getContext(), codeWriter);
                    break;
                case embeddedHandledType:
                    embeddedTypePropertyHandlerTemplate.merge(context.getContext(), codeWriter);
                    break;
            }
            codeWriter.flush();
        } else {
            throw new IllegalArgumentException("Missing fields in context");
        }
    }

    public void writeObjectHandler(VelocityContext context, Writer codeWriter) throws IOException {
        if (isNoneBlank(
            (String) context.get(ObjectHandlerFields.ENITITY_CLASS_NAME.getElement()),
            (String) context.get(ObjectHandlerFields.HANDLER_CLASS_NAME.getElement()),
            (String) context.get(ObjectHandlerFields.PACKAGE.getElement())
        ) && isNotEmpty(
            (java.util.Collection) context
                .get(ObjectHandlerFields.PROPERTY_HANDLERS.getElement()))) {
            objectHandlerTemplate.merge(context, codeWriter);
            codeWriter.flush();
        } else {
            throw new IllegalArgumentException("Missing fields in context");
        }
    }


}
