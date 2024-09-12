package src.main.java.java.client.form;

import  src.main.java.java.client.utility.ConsoleInterface;
import  src.main.java.java.client.utility.Interrogator;
import src.main.java.common.object.Transport;

import java.util.NoSuchElementException;

public class TransportForm extends Form<Transport> {
    private final ConsoleInterface console;

    public TransportForm(ConsoleInterface console) {
        this.console = console;
    }

    @Override
    public Transport build(){
        var fileMode = Interrogator.fileMode();

        String strTransport;
        Transport transport;
        while (true) {
            try {
                console.println("Список состояния транспорта - " + Transport.names());
                console.println("Введите состояния:");
                console.prompt();

                strTransport = console.readln().trim();;
                if (fileMode) console.println(strTransport);

                transport = Transport.valueOf(strTransport.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Тип не распознан!");
            } catch (IllegalArgumentException exception) {
                console.printError("Такого типа нет в списке!");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return transport;
    }
}