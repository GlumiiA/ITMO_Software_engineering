package src.main.java.java.client.utility;


import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.object.Flat;
import src.main.java.common.utility.CommandTypes;
import src.main.java.java.client.command.ExecuteScript;
import src.main.java.java.client.command.Exit;
import src.main.java.java.client.command.Help;
import src.main.java.java.client.form.FlatForm;
import src.main.java.java.client.network.ClientUDP;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс исполнения программы
 */
public class Runner {
    private String login;
    private String password;
    private Console console;
    private Map<CommandTypes, String[]> commands;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;
    private ClientUDP clientUDP;
    private Flat flat;
    private boolean loggined = false;

    public Runner(ClientUDP clientUDP, Console console, Map<CommandTypes, String[]> commands) {
        this.console = console;
        this.clientUDP = clientUDP;
        this.commands = commands;
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            console.println("Введите имя команды:");
            Response commandStatus;

            String[] userCommand = {"", ""};

            while (true) {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                console.println(userCommand[0]);
                commandStatus = launchCommand(userCommand);
                if (commandStatus.getMessage() == "exit") break;
                console.println(commandStatus.getMessage());
            }
        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }

    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
                if (lengthRecursion < 0) {
                    console.selectConsoleScanner();
                    console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                    while (lengthRecursion < 0 || lengthRecursion > 500) {
                        try {
                            console.print("> ");
                            lengthRecursion = Integer.parseInt(console.readln().trim());
                            if (lengthRecursion < 0 || lengthRecursion > 500) {
                                console.println("длина не распознана");
                            }
                        } catch (NumberFormatException e) {
                            console.println("длина не распознана");
                        }
                    }
                    console.selectFileScanner(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 500)
                    return false;
            }
        }
        return true;
    }

    /**
     * Режим запуска скрипта
     */
    private Response scriptMode(String argument) {
        String[] userCommand = {"", ""};
        StringBuilder executionOutput = new StringBuilder();

        if (!new File(argument).exists()) return new Response(false, "Файл не существет!");
        if (!Files.isReadable((Paths.get(argument))))
            return new Response(false, "Отсутствуют права доступа к чтению файла.");
        if (!Files.isExecutable((Paths.get(argument))))
            return new Response(false, "Отсутствуют права доступа к выполнению файла");

        scriptStack.add(argument);
        try (Scanner scriptScanner = new Scanner(new File(argument))) {

            Response commandStatus;

            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);
            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                executionOutput.append(console.getPrompt() + String.join(" ", userCommand) + "\n");
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    needLaunch = checkRecursion(userCommand[1], scriptScanner);
                }
                commandStatus = needLaunch ? launchCommand(userCommand) : new Response("Превышена максимальная глубина рекурсии");
                if (userCommand[0].equals("execute_script")) console.selectFileScanner(scriptScanner);
                executionOutput.append(commandStatus.getMessage() + "\n");
            } while (commandStatus.getExitCode() && !commandStatus.getMessage().equals("exit") && console.isCanReadln());

            console.selectConsoleScanner();
            if (!commandStatus.getExitCode() && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                executionOutput.append("Проверьте скрипт на корректность введенных данных!\n");
            }

            return new Response(commandStatus.getExitCode(), executionOutput.toString());
        } catch (FileNotFoundException exception) {
            return new Response(false, "Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            return new Response(false, "Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return new Response("");
    }

    private Response launchCommand(String[] userCommand) {
        Response response;
        if (userCommand[0].equals("")) return new Response("");
        var command = CommandTypes.getByString(userCommand[0]);
        if (loggined) {
            if (!commands.containsKey(command)) {
                command = null;
            }
            if (command == null)
                return new Response(false, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");

            switch (userCommand[0]) {

                case "executeScript" -> {
                    Response tmp = new ExecuteScript(console).apply(userCommand);
                    if (!tmp.getExitCode()) return tmp;
                    Response tmp2 = scriptMode(userCommand[1]);
                    return new Response(tmp2.getExitCode(), tmp.getMessage() + "\n" + tmp2.getMessage().trim());
                }
                default -> {
                    byte[] bytes = new byte[userCommand.length];
                    if (command == CommandTypes.Help) {
                        console.println(new Help(console, commands).apply(userCommand).getMessage());
                    } else if (command == CommandTypes.Exit) {
                        bytes = ClientUDP.serializer(new Request(CommandTypes.Save, ""));
                        clientUDP.sendData(bytes);
                        loggined = true;
                        return new Exit(console).apply(userCommand);
                    } else if (command == CommandTypes.Remove | command == CommandTypes.RemoveLower) {

                        bytes = ClientUDP.serializer(new Request(command, Integer.parseInt(userCommand[1])));
                    } else if (command == CommandTypes.Add | command == CommandTypes.AddIfMin | command == CommandTypes.AddIfMax) {
                        flat = (new FlatForm(console)).build();
                        Request request = new Request(command, flat);
                        bytes = ClientUDP.serializer(request);
                    } else if (command == CommandTypes.FilterName) {
                        console.prompt();
                        String name = console.readln().trim();
                        bytes = ClientUDP.serializer(new Request(command, name));
                    } else if (command == CommandTypes.Update) {
                        flat = (new FlatForm(console)).build();
                        bytes = ClientUDP.serializer(new Request(command, flat, Integer.parseInt(userCommand[1])));
                    } else {
                        bytes = ClientUDP.serializer(new Request(command, ""));
                    }
                    if (command != CommandTypes.Help) {
                        clientUDP.sendData(bytes);
                        var data = clientUDP.receiveData(10000);
                        response = ClientUDP.deserialize(data);
                        return response;
                    } else return new Response(false, "");
                }
            }
        } else if (command == CommandTypes.Login) {
            if (!userCommand[1].isEmpty())
                return new Response(false, "Неправильное количество аргументов! \nИспользование: 'login'");
            console.println("*Авторизация*");
            console.print("Логин:");
            login = console.readln().trim();
            console.print("Пароль:");
            password = console.readln().trim();
//                    MessageDigest md = MessageDigest.getInstance("SHA-224");
//                    var info = md.digest(password.getBytes());
//                    StringBuilder builder = new StringBuilder();
//                    for (var b : info) {
//                        builder.append(String.format("%02X", b));
//                    }
//                    password = builder.toString();
            var bytes = ClientUDP.serializer(new Request(command, login, password, login + ";" + password));
            clientUDP.sendData(bytes);
            var data = clientUDP.receiveData(10000);
            response = clientUDP.deserialize(data);
            if (response.getExitCode()) {
                loggined = true;
            }
            return response;
        } else if (command == CommandTypes.Register) {
            if (!userCommand[1].isEmpty())
                return new Response(false, "Неправильное количество аргументов! \n Использование: 'register'");
            console.println("*Регистрация*");
            console.print("Логин:");
            login = console.readln().trim();
            console.print("Пароль:");
            password = console.readln().trim();
//                StringBuilder builder = new StringBuilder();
//                for (var b : info) {
//                    builder.append(String.format("%02X", b));
////                }
//                password = builder.toString();
            var bytes = ClientUDP.serializer(new Request(command, login, password, login + ";" + password));
            clientUDP.sendData(bytes);
            var data = clientUDP.receiveData(10000);
            response = clientUDP.deserialize(data);
            if (response.getExitCode()) {
                loggined = true;
            }
            return response;

        } else {
            return new Response(false, "Вы не авторизованы! Авторизуйтесь или зарегистрируйтесь(login)");
        }
    }
}