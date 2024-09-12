package src.main.java.common.object;

import java.io.Serial;
import java.io.Serializable;

public enum Transport implements Serializable {
    FEW,
    NONE,
    LITTLE,
    NORMAL;
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Transport transport : values()) {
            nameList.append(transport.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
    @Serial
    private static final long serialVersionUID = 132934214L;}