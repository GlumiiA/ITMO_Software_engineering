package src.main.java.java.client.utility;

import java.util.Scanner;

/**
 * Отвечает за режим ввода пользовательских данных
 * @author maxbarsukov
 */
public class Interrogator {
    private static Scanner userScanner;
    public static boolean fileMode = false;

    public static Scanner getUserScanner() {
        return userScanner;
    }

    public static void setUserScanner(Scanner userScanner) {
        Interrogator.userScanner = userScanner;
    }

    public static boolean fileMode() {
        return fileMode;
    }
}
