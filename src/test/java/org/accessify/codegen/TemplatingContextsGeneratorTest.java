package org.accessify.codegen;

import base.DummyHandledType;
import base.DummyHandledTypeOneProperty;
import base.DummyNonHandledType;
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

    private static final Logger LOG = LoggerFactory.getLogger(TemplatingContextsGeneratorTest.class);

    @Test
    public void testSingleProperty() throws Exception {
        VelocityContext context = TemplatingContextsGenerator.toContext(
                new PropertyDescriptor("onlyOne", DummyHandledTypeOneProperty.class)
        );
        assertNotNull(context);
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.VALUE));
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.GETTER));
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.SETTER));
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.PROPERTY));
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.HANDLER_TYPE));
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.PACKAGE));
        assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.ENTITY));
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
        List<VelocityContext> contexts = TemplatingContextsGenerator.generatePropertyHandlersContexts(clazz);
        assertNotNull(contexts);
        assertTrue(isNotEmpty(contexts));
        assertEquals(3, contexts.size());
        for (VelocityContext context : contexts) {
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.VALUE));
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.GETTER));
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.SETTER));
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.PROPERTY));
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.HANDLER_TYPE));
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.PACKAGE));
            assertTrue(context.internalContainsKey(TemplatingContextsGenerator.PropertyContextConstants.ENTITY));
            LOG.debug(ContextDebugUtil.toString(context));
        }
    }

    @Test
    public void testPropertyHandlersEmpty() throws Exception {
        Class<DummyNonHandledType> clazz = DummyNonHandledType.class;
        List<VelocityContext> contexts = TemplatingContextsGenerator.generatePropertyHandlersContexts(clazz);
        assertNotNull(contexts);
        assertTrue(isEmpty(contexts));
    }

    @Test
    public void testObjectHandler() throws Exception {
        Class<DummyHandledType> type = DummyHandledType.class;
        VelocityContext context = TemplatingContextsGenerator.generateObjectHandlerContext(type, TemplatingContextsGenerator.generatePropertyHandlersContexts(type));
        assertNotNull(context);
        assertTrue(context.containsKey(TemplatingContextsGenerator.TypeContextConstants.PACKAGE));
        assertTrue(context.containsKey(TemplatingContextsGenerator.TypeContextConstants.PROPERTY_HANDLERS));
        assertTrue(context.containsKey(TemplatingContextsGenerator.TypeContextConstants.HANDLER_CLASS_NAME));
        assertTrue(context.containsKey(TemplatingContextsGenerator.TypeContextConstants.ENITITY_CLASS_NAME));
        LOG.debug(ContextDebugUtil.toString(context));
    }

    @Test
    public void testName() throws Exception {
        Class<DummyNonHandledType> type = DummyNonHandledType.class;
        VelocityContext context = TemplatingContextsGenerator.generateObjectHandlerContext(type, TemplatingContextsGenerator.generatePropertyHandlersContexts(type));
        assertNull(context);
    }
}