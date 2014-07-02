package base;

import org.accessify.handlers.AbstractObjectHandler;
import org.accessify.handlers.AbstractPropertyHandler;

/**
 * Created by edouard on 14/06/17.
 */
public class DummyHandledTypeHandler extends AbstractObjectHandler<DummyHandledType> {

    public DummyHandledTypeHandler() {
        super(
            new AbstractPropertyHandler<DummyHandledType, String>("string") {
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
            },
            new AbstractPropertyHandler<DummyHandledType, Integer>("integer") {
                @Override
                public void set(DummyHandledType instance, Integer value) {
                    instance.setInteger(value);
                }

                @Override
                public Integer get(DummyHandledType instance) {
                    return instance.getInteger();
                }

                @Override
                public Class<Integer> type() {
                    return Integer.class;
                }
            },
            new AbstractPropertyHandler<DummyHandledType, Boolean>("bool") {
                @Override
                public void set(DummyHandledType instance, Boolean value) {
                    instance.setBool(value);
                }

                @Override
                public Boolean get(DummyHandledType instance) {
                    return instance.getBool();
                }

                @Override
                public Class<Boolean> type() {
                    return Boolean.class;
                }
            }
        );
    }

    @Override
    public Class<DummyHandledType> handledType() {
        return DummyHandledType.class;
    }

    @Override
    public DummyHandledType newInstance() {
        return new DummyHandledType();
    }
}
