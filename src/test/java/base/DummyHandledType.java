package base;

import org.accessify.annotations.HandledType;

/**
 * Created by edouard on 14/06/11.
 */
@HandledType
public class DummyHandledType {

    private String string;

    private Integer integer;

    private Boolean bool;

    private DummyHandledType embedded;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Boolean getBool() {
        return bool;
    }

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
