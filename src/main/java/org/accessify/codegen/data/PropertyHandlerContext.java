package org.accessify.codegen.data;

import org.apache.velocity.VelocityContext;

/**
 * Created by edouard on 14/07/04.
 */
public class PropertyHandlerContext {


    public static PropertyHandlerContext make(){
        return new PropertyHandlerContext();
    }

    private VelocityContext context;

    private TemplateType template;

    public VelocityContext context() {
        return this.context;
    }

    public TemplateType template() {
        return this.template;
    }

    public PropertyHandlerContext setContext(final VelocityContext context) {
        this.context = context;
        return this;
    }

    public PropertyHandlerContext setTemplate(final TemplateType template) {
        this.template = template;
        return this;
    }

    public VelocityContext getContext() {
        return context;
    }

    public TemplateType getTemplate() {
        return template;
    }
}
