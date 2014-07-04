package base;

import org.accessify.annotations.HandledType;
import org.accessify.annotations.Property;

/**
 * Created by edouard on 14/06/11.
 */
@HandledType
public class DummyHandledType {

    private String string;

    private Integer integer;

    private Boolean bool;

    private DummyHandledType embedded;

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
    public Boolean getBool() {
        return bool;
    }

    @Property.Setter(name = "bool")
    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    public DummyHandledType getEmbedded() {
        return embedded;
    }

    public void setEmbedded(DummyHandledType embedded) {
        this.embedded = embedded;
    }
}
