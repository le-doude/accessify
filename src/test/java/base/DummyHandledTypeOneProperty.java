package base;

import org.accessify.annotations.HandledType;

/**
 * Created by edouard on 14/06/11.
 */
@HandledType
public class DummyHandledTypeOneProperty {

    private String onlyOne;

    public String getOnlyOne() {
        return onlyOne;
    }

    public void setOnlyOne(String onlyOne) {
        this.onlyOne = onlyOne;
    }
}
