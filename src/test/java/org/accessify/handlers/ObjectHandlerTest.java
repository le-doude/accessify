package org.accessify.handlers;

import base.DummyHandledType;
import org.accessify.codegen.HandledTypesProcessor;
import org.accessify.utils.ConfigurationUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class ObjectHandlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectHandlerTest.class);

    @BeforeClass
    public static void setupHandlers(){
        try {
            if(!HandledTypesProcessor
                .fullyProcessHandledTypes(Arrays.<Class>asList(DummyHandledType.class))){
                throw new AssertionError("Could not compile ObjectHandlers");
            }
        } catch (IntrospectionException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSuccess() throws Exception {
        ObjectHandler<DummyHandledType> handler =
            HandlingFactory.get(ConfigurationUtils.FACTORY_INSTANCE_NAME)
                .handler(DummyHandledType.class);

        assertNotNull(handler);

        boolean bool = RandomUtils.nextInt(0, 500) % 2 == 0;
        boolean bool2 = RandomUtils.nextInt(0, 500) % 2 == 0;
        int integer = RandomUtils.nextInt(0, 500);
        int integer2 = RandomUtils.nextInt(0, 500);
        String string = RandomStringUtils.randomAlphanumeric(15);
        String string2 = RandomStringUtils.randomAlphanumeric(15);

        DummyHandledType example = new DummyHandledType();

        handler.set(example, "bool", bool);
        handler.set(example, "integer", integer);
        handler.set(example, "string", string);
        handler.set(example, "embedded.bool", bool2);
        handler.set(example, "embedded.integer", integer2);
        handler.set(example, "embedded.string", string2);

        assertEquals(bool, example.getBool());
        assertEquals(integer, example.getInteger().intValue());
        assertEquals(string, example.getString());
        assertNotNull(example.getEmbedded());
        assertEquals(bool2, example.getEmbedded().getBool());
        assertEquals(integer2, example.getEmbedded().getInteger().intValue());
        assertEquals(string2, example.getEmbedded().getString());
        assertEquals(null, example.getEmbedded().getEmbedded());

        assertEquals(example.getBool(), handler.get(example, "bool"));
        assertEquals(example.getInteger(), handler.get(example, "integer"));
        assertEquals(example.getString(), handler.get(example, "string"));
        assertEquals(example.getEmbedded().getBool(), handler.get(example, "embedded.bool"));
        assertEquals(example.getEmbedded().getInteger(), handler.get(example, "embedded.integer"));
        assertEquals(example.getEmbedded().getString(), handler.get(example, "embedded.string"));
        assertEquals(null, handler.get(example, "embedded.embedded"));
    }

    @Test
    public void testGetAll() throws Exception {
        ObjectHandler<DummyHandledType> handler =
            HandlingFactory.get(ConfigurationUtils.FACTORY_INSTANCE_NAME)
                .handler(DummyHandledType.class);

        assertNotNull(handler);

        boolean bool = RandomUtils.nextInt(0, 500) % 2 == 0;
        boolean bool2 = RandomUtils.nextInt(0, 500) % 2 == 0;
        int integer = RandomUtils.nextInt(0, 500);
        int integer2 = RandomUtils.nextInt(0, 500);
        String string = RandomStringUtils.randomAlphanumeric(15);
        String string2 = RandomStringUtils.randomAlphanumeric(15);

        DummyHandledType example = new DummyHandledType();
        handler.set(example, "bool", bool);
        handler.set(example, "integer", integer);
        handler.set(example, "string", string);
        handler.set(example, "embedded.bool", bool2);
        handler.set(example, "embedded.integer", integer2);
        handler.set(example, "embedded.string", string2);

        Map<String, Object> all = handler.getAll(example);

        assertNotNull(all);
        assertTrue(MapUtils.isNotEmpty(all));

        LOG.info(Arrays.toString(all.keySet().toArray()));

        assertTrue(all.containsKey("bool"));
        assertTrue(all.containsKey("integer"));
        assertTrue(all.containsKey("string"));
        assertTrue(all.containsKey("embedded.bool"));
        assertTrue(all.containsKey("embedded.integer"));
        assertTrue(all.containsKey("embedded.string"));

    }
}
