package base;

import org.accessify.handlers.AbstractPropertyHandler;
import org.accessify.handlers.BasePOJOPropertiesHandler;

/**
 * Created by edouard on 14/06/17.
 */
public class TestTypeHandler extends BasePOJOPropertiesHandler<TestEntity> {

    public TestTypeHandler() {
        super(
                new AbstractPropertyHandler<TestEntity, String>("string") {
                    @Override
                    public void set(TestEntity instance, String value) {
                        instance.setString(value);
                    }

                    @Override
                    public String get(TestEntity instance) {
                        return instance.getString();
                    }

                    @Override
                    public Class<String> type() {
                        return String.class;
                    }
                },
                new AbstractPropertyHandler<TestEntity, Integer>("integer") {
                    @Override
                    public void set(TestEntity instance, Integer value) {
                        instance.setInteger(value);
                    }

                    @Override
                    public Integer get(TestEntity instance) {
                        return instance.getInteger();
                    }

                    @Override
                    public Class<Integer> type() {
                        return Integer.class;
                    }
                }
        );
    }
}
