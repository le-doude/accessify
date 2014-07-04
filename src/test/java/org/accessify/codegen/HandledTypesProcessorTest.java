package org.accessify.codegen;

import base.DummyHandledType;
import org.accessify.handlers.HandlingFactory;
import org.accessify.handlers.ObjectHandler;
import org.accessify.utils.ConfigurationUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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

        boolean bool = RandomUtils.nextInt(0, 500) % 2 == 0;
        int integer = RandomUtils.nextInt(0, 500);
        String string = RandomStringUtils.randomAlphanumeric(15);

        DummyHandledType example = new DummyHandledType();
        handler.set(example, "bool", bool);
        handler.set(example, "integer", integer);
        handler.set(example, "string", string);

        assertEquals(bool, example.getBool());
        assertEquals(integer, example.getInteger().intValue());
        assertEquals(string, example.getString());

        assertEquals(example.getBool(), handler.get(example, "bool"));
        assertEquals(example.getInteger(), handler.get(example, "integer"));
        assertEquals(example.getString(), handler.get(example, "string"));

    }
}
