
package net.ecom.vo;

import java.io.Serializable;

public class GlobalConfigVO implements Serializable {
    String key = null;
    String value = null;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
