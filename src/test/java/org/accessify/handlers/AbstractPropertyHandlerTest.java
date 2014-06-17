package org.accessify.handlers;

import base.DummyHandledType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractPropertyHandlerTest {

    protected static final String PROPERTYNAME = "string";
    final AbstractPropertyHandler<DummyHandledType, String> handler = new AbstractPropertyHandler<DummyHandledType, String>(PROPERTYNAME) {
        @Override
        public void set(DummyHandledType instance, String value) {
            instance.setString(value);
        }

        @Override
        public String get(DummyHandledType instance) {
            return instance.getString();
        }

        @Override
        public Class<String> type() {
            return String.class;
        }
    };

    @Test
    public void testGetting() throws Exception {
        DummyHandledType dummy = new DummyHandledType();
        dummy.setString("VALUE1");
        assertEquals(dummy.getString(), this.handler.get(dummy));
    }

    @Test
    public void testSetting() throws Exception {
        DummyHandledType dummy = new DummyHandledType();
        dummy.setString("VALUE1");
        this.handler.set(dummy, "VALUE2");
        assertEquals("VALUE2", dummy.getString());
    }

    @Test
    public void testBoth() throws Exception {
        DummyHandledType dummy = new DummyHandledType();
        dummy.setString("VALUE1");
        this.handler.set(dummy, "VALUE2");
        assertEquals("VALUE2", this.handler.get(dummy));
        assertEquals("VALUE2", dummy.getString());
    }
}