package base;

import org.accessify.annotations.HandledType;
import org.accessify.annotations.Property;

/**
 * Created by edouard on 14/06/11.
 */
@HandledType
public class TestEntity {

    private String string;

    private Integer integer;

    private Boolean bool;

    @Property.Getter(name = "string")
    public String getString() {
        return string;
    }

    @Property.Setter(name = "string")
    public void setString(String string) {
        this.string = string;
    }

    @Property.Getter(name = "integer")
    public Integer getInteger() {
        return integer;
    }

    @Property.Setter(name = "integer")
    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    @Property.Getter(name = "bool")
    public Boolean isBool() {
        return bool;
    }

    @Property.Setter(name = "bool")
    public void setBool(Boolean bool) {
        this.bool = bool;
    }
}
