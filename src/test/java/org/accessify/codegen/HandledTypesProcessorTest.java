package org.accessify.codegen;

import base.DummyHandledType;
import base.DummyNonHandledType;
import org.accessify.handlers.HandlingFactory;
import org.accessify.handlers.ObjectHandler;
import org.accessify.utils.ConfigurationUtils;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HandledTypesProcessorTest {

    @Test
    public void testFullProcess() throws Exception {
        boolean b = HandledTypesProcessor
            .fullyProcessHandledTypes(Arrays.<Class>asList(DummyHandledType.class));

        assertTrue("Compilation was unsuccessful", b);

        ObjectHandler<DummyHandledType> handler =
            HandlingFactory.get(ConfigurationUtils.FACTORY_INSTANCE_NAME)
                .handler(DummyHandledType.class);

        assertNotNull(handler);
        assertEquals(DummyHandledType.class, handler.handledType());
        assertTrue(handler.newInstance() instanceof DummyHandledType);
        for (String prop : handler.properties()) {
            assertNotNull(handler.obtainHandler(prop));
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotMarkedAsHandled() throws Exception {
        boolean b = HandledTypesProcessor
            .fullyProcessHandledTypes(Arrays.<Class>asList(DummyNonHandledType.class));
        assertFalse(b);
    }
}
