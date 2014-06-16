package org.accessify.processor;

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
public class CodeTemplateService {

    protected static final String VELOCITY_PROPERTIES_FILE = "velocity.properties";
    protected static final String PROPERTY_HANDLER_VELOCITY_TEMPLATE = "property_handler.vtl";

    private final Template propertyHandlerTemplate;
    private final VelocityEngine engine;

    public CodeTemplateService() throws IOException {
        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource(VELOCITY_PROPERTIES_FILE);
        properties.load(url.openStream());
        engine = new VelocityEngine(properties);
        engine.init();
        propertyHandlerTemplate = engine.getTemplate(PROPERTY_HANDLER_VELOCITY_TEMPLATE);
    }

    public void writePropertyHandler(VelocityContext context, Writer codeWriter) throws IOException {
        propertyHandlerTemplate.merge(context, codeWriter);
        codeWriter.flush();
    }
}
