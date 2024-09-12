package src.main.java.java.client;


import src.main.java.common.utility.CommandTypes;
import src.main.java.java.client.network.ClientUDP;
import src.main.java.java.client.utility.AuthManager;
import src.main.java.java.client.utility.Console;
import src.main.java.java.client.utility.Runner;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class App {
    public static void main(String[] args) throws UnknownHostException, SocketException {
//        Interrogator.setUserScanner(new Scanner(System.in));
        Scanner scanner = new Scanner(System.in);
        Console console = new Console();
        AuthManager authManager = new AuthManager(scanner, console);
        ClientUDP clientUDP = new ClientUDP(5874);
        Map<CommandTypes,String[]> commands = new HashMap<>(){{
                put(CommandTypes.Help, new String[]{"help", "вывести справку по доступным командам"});
                put(CommandTypes.Add, new String[]{"add", "добавить новый элемент в коллекцию"});
                put(CommandTypes.Info, new String[]{"info", "вывести информацию о коллекции"});
                put(CommandTypes.Show, new String[]{"show", "вывести все элементы коллекции"});
                put(CommandTypes.Clear, new String[]{"clear", "очистить коллекцию"});
                put(CommandTypes.Remove, new String[]{"remove", "удалить элемент из коллекции по его id"});
                put(CommandTypes.Update, new String[]{"update", "обновить значение элемента коллекции, id которого равен заданному"});
                put(CommandTypes.AddIfMax, new String[]{"addIfMax", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции"});
                put(CommandTypes.AddIfMin, new String[]{"addIfMin", "добавить новый элемент в коллекцию, если его значение не превышает значение наименьшего элемента этой коллекции"});
                put(CommandTypes.RemoveLower, new String[]{"removeLower", "удалить из коллекции все элементы, меньше, чем заданный"});
                put(CommandTypes.FilterName, new String[]{"filterName", "вывести элементы, значение поля name которых начинается с заданной подстроки"});
                put(CommandTypes.PrintFiledNumberOfBathrooms, new String[]{"printFiledNumberOfBathrooms", "вывести значения поля numberOfBathrooms всех элементов в порядке убывания"});
                put(CommandTypes.PrintUniqueNumberOfBathrooms, new String[]{"printUniqueNumberOfBathrooms", "вывести уникальные значения поля numberOfBathrooms всех элементов в коллекции"});
                put(CommandTypes.Exit, new String[]{"exit", "завершить программу"});
                put(CommandTypes.ExecuteScript, new String[]{"executeScript", "выполнить скрипт"});

            }};
        console.println("Для дальнейшей работы необходимо авторизоваться(login/register)!");
        Runnable runnable1=()->{new Runner(clientUDP,console, commands).interactiveMode();};
        new Thread(runnable1).start();
    }
}