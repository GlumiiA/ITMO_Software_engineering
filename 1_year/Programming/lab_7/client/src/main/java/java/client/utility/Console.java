package src.main.java.java.client.utility;

import java.util.Scanner;


public class Console implements ConsoleInterface {
    private static final String P = "> ";
    private static Scanner fileScanner = null;
    private static final Scanner defScanner = new Scanner(System.in);

    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public void printError(Object obj) {
        System.err.println("Ошибка: " + obj);
    }

    @Override
    public String read() {
        return null;
    }

    @Override
    public String readln() {
        return (fileScanner != null ? fileScanner : defScanner).nextLine();
    }

    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    @Override
    public boolean hasNextLine() {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }

    public void prompt() {
        print(P);
    }

    public String getPrompt() {
        return P;
    }
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    public void selectConsoleScanner() {
        fileScanner = null;
    }
    public char[] readPassword(){
        return fileScanner.nextLine().toCharArray();
    }
}