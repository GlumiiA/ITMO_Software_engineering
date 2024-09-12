package src.main.java.manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserManager {
    private String username;
    private String password;
    Scanner credentials = null;
    public void  Credentials(){
        try {
            credentials = new Scanner(new FileReader("credentials.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Не найден credentials.txt с данными для входа в базу данных.");
            System.exit(-1);
        }
        try {
            username = credentials.nextLine().trim();
            password = credentials.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.err.println("Не найдены данные для входа в файле. Завершение работы.");
            System.exit(-1);
        }
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
