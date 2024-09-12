package src.main.java.java.client.utility;

import src.main.java.common.exceptions.MustBeNotEmptyException;
import src.main.java.common.exceptions.NotDeclaredValueException;
import src.main.java.common.network.Request;
import src.main.java.common.object.User;
import src.main.java.common.utility.CommandTypes;

import java.util.Scanner;

public class AuthManager {
    private final String loginCommand = "sign_in";
    private final String registerCommand = "sign_up";
    private Scanner scanner;
    private Console console;
    User user = null;

    public AuthManager(Scanner scanner, Console console) {
        this.scanner = scanner;
        this.console = console;
    }
    public String askLogin() {
        String login;
        while (true) {
            try {
                console.println("Введите логин:");
                console.print("> ");
                login = scanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException e) {
                console.println("Логин не может быть пустым!");
            }
        }
        return login;
    }

    public String askPassword() {
        String password;
        while (true) {
            try {
                console.println("Введите пароль:");
                console.print("> ");
                char [] c = console.readPassword();
                password = String.copyValueOf(c);
                if (password.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException e) {
                console.println("Логин не может быть пустым!");
            }
        }
        return password;
    }
    public boolean askQuestion(String question) {
        String finalQuestion = question + " (Да/Нет):";
        String answer;
        while (true) {
            try {
                console.println(finalQuestion);
                console.print("> ");
                answer = scanner.nextLine().trim();
                if (!answer.toLowerCase().equals("да") && !answer.equals("нет")) throw new NotDeclaredValueException();
                break;
            } catch (NotDeclaredValueException e) {
                console.println("Ответ должен быть либо 'Да', либо 'Нет'!");
            }
        }
        return answer.equals("Да");
    }
    public Request handle() {
        boolean command = askQuestion("У вас уже есть учетная запись?");
        if (command) {
            console.println("Создание новой учётной записи");
            User user = new User(askLogin(), askPassword());
        } else {
            User user = new User(askLogin(), askPassword());
        }
        return new Request(CommandTypes.AuthUser, user, command);
    }

    public User getUser() {
        return user;
    }
}