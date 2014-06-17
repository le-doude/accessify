package org.accessify.processor;

import base.TestEntity;
import org.accessify.codegen.PropertyReader;
import org.accessify.utils.ContextDebugUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

public class PropertyReaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyReaderTest.class);

    @Test
    public void testOne() throws Exception {

        Class<TestEntity> clazz = TestEntity.class;
        List<VelocityContext> contexts = PropertyReader.generatePropertyHandlersContexts(clazz);

        assertNotNull(contexts);
        assertTrue(CollectionUtils.isNotEmpty(contexts));
        assertEquals(3, contexts.size());

        for (VelocityContext context : contexts) {
            assertTrue(context.internalContainsKey(PropertyReader.VALUE));
            assertTrue(context.internalContainsKey(PropertyReader.GETTER));
            assertTrue(context.internalContainsKey(PropertyReader.SETTER));
            assertTrue(context.internalContainsKey(PropertyReader.PROPERTY));
            assertTrue(context.internalContainsKey(PropertyReader.HANDLER_TYPE));
            assertTrue(context.internalContainsKey(PropertyReader.PACKAGE));
            assertTrue(context.internalContainsKey(PropertyReader.ENTITY));
            LOG.debug(ContextDebugUtil.toString(context));
        }

    }
}