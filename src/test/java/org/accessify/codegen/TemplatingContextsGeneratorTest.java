package org.accessify.codegen;

import base.DummyHandledType;
import base.DummyHandledTypeOneProperty;
import base.DummyNonHandledType;
import org.accessify.codegen.data.PropertyHandlerContext;
import org.accessify.codegen.fields.ObjectHandlerFields;
import org.accessify.codegen.fields.PropertyTemplateFields;
import org.accessify.utils.ContextDebugUtil;
import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.junit.Assert.*;

public class TemplatingContextsGeneratorTest {

    private static final Logger LOG =
        LoggerFactory.getLogger(TemplatingContextsGeneratorTest.class);

    @Test
    public void testSingleProperty() throws Exception {
        VelocityContext context = TemplatingContextsGenerator.toContext(
            new PropertyDescriptor("onlyOne", DummyHandledTypeOneProperty.class)
        );
        assertNotNull(context);
        assertTrue(context.internalContainsKey(PropertyTemplateFields.PROPERTY_RETURN_TYPE));
        assertTrue(context.internalContainsKey(PropertyTemplateFields.GETTER));
        assertTrue(context.internalContainsKey(PropertyTemplateFields.SETTER));
        assertTrue(context.internalContainsKey(PropertyTemplateFields.PROPERTY));
        assertTrue(context.internalContainsKey(PropertyTemplateFields.HANDLER_TYPE));
        assertTrue(context.internalContainsKey(PropertyTemplateFields.PACKAGE));
        assertTrue(context.internalContainsKey(PropertyTemplateFields.HANDLED_TYPE_NAME));
        LOG.debug(ContextDebugUtil.toString(context));
    }

    @Test
    public void testClassPropertyNotIncluded() throws Exception {
        VelocityContext context = TemplatingContextsGenerator.toContext(
            new PropertyDescriptor("class", null, null)
        );
        assertNull(context);
    }

    @Test
    public void testPropertyHandlers() throws Exception {
        Class<DummyHandledType> clazz = DummyHandledType.class;
        List<PropertyHandlerContext> contexts =
            TemplatingContextsGenerator.generatePropertyHandlersContexts(clazz);
        assertNotNull(contexts);
        assertTrue(isNotEmpty(contexts));
        assertEquals(3, contexts.size());
        for (PropertyHandlerContext context : contexts) {
            assertTrue(context.getContext()
                .internalContainsKey(PropertyTemplateFields.PROPERTY_RETURN_TYPE));
            assertTrue(context.getContext().internalContainsKey(PropertyTemplateFields.GETTER));
            assertTrue(context.getContext().internalContainsKey(PropertyTemplateFields.SETTER));
            assertTrue(context.getContext().internalContainsKey(PropertyTemplateFields.PROPERTY));
            assertTrue(context.getContext().internalContainsKey(PropertyTemplateFields.HANDLER_TYPE));
            assertTrue(context.getContext().internalContainsKey(PropertyTemplateFields.PACKAGE));
            assertTrue(context.getContext().internalContainsKey(
                PropertyTemplateFields.HANDLED_TYPE_NAME));
            LOG.debug(ContextDebugUtil.toString(context.getContext()));
        }
    }

    @Test
    public void testPropertyHandlersEmpty() throws Exception {
        Class<DummyNonHandledType> clazz = DummyNonHandledType.class;
        List<PropertyHandlerContext> contexts =
            TemplatingContextsGenerator.generatePropertyHandlersContexts(clazz);
        assertNotNull(contexts);
        assertTrue(isEmpty(contexts));
    }

    @Test
    public void testObjectHandler() throws Exception {
        Class<DummyHandledType> type = DummyHandledType.class;
        VelocityContext context = TemplatingContextsGenerator.generateObjectHandlerContext(type);
        assertNotNull(context);
        assertTrue(context.containsKey(ObjectHandlerFields.PACKAGE));
        assertTrue(context.containsKey(ObjectHandlerFields.PROPERTY_HANDLERS));
        assertTrue(context.containsKey(ObjectHandlerFields.HANDLER_CLASS_NAME));
        assertTrue(context.containsKey(ObjectHandlerFields.ENITITY_CLASS_NAME));
        LOG.debug(ContextDebugUtil.toString(context));
    }

    @Test
    public void testName() throws Exception {
        Class<DummyNonHandledType> type = DummyNonHandledType.class;
        VelocityContext context = TemplatingContextsGenerator.generateObjectHandlerContext(type);
        assertNull(context);
    }
}
