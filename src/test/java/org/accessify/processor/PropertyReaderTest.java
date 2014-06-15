package org.accessify.processor;

import base.TestEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PropertyReaderTest {


    @Test
    public void testOne() throws Exception {

        Class<TestEntity> clazz = TestEntity.class;
        List<VelocityContext> contexts = PropertyReader.generateHandlerContexts(clazz);

        assertNotNull(contexts);
        assertTrue(CollectionUtils.isNotEmpty(contexts));
        assertEquals(3, contexts.size());

        for (VelocityContext context : contexts) {
            System.out.println(context.toString());
        }

    }
}