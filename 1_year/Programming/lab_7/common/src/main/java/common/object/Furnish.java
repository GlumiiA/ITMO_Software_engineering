package src.main.java.common.object;

import java.io.Serializable;

public enum Furnish implements Serializable {
    DESIGNER,
    NONE,
    FINE,
    BAD,
    LITTLE;
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Furnish furnish : values()) {
            nameList.append(furnish.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}