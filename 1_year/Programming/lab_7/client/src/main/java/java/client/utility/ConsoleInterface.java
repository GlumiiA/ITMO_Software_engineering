package src.main.java.java.client.utility;

import java.util.Scanner;

public interface ConsoleInterface {
    void print(Object obj);
    void println(Object obj);
    String read();
    String readln();
    void printError(Object obj);
    void selectFileScanner(Scanner obj);

    void selectConsoleScanner();

    void printTable(Object obj1, Object obj2);
    /**
     * Проверяет следующую строку,на наличие символов
     */
    boolean hasNextLine();
    void prompt();

    String getPrompt();
    boolean isCanReadln();
}
