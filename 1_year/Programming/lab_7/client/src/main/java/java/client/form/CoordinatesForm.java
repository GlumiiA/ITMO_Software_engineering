package src.main.java.java.client.form;


import  src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.common.object.Coordinates;
import  src.main.java.java.client.utility.Interrogator;

import java.util.NoSuchElementException;

public class CoordinatesForm extends Form<Coordinates> {
    private final ConsoleInterface console;

    public CoordinatesForm(ConsoleInterface console) {
        this.console = console;
    }

    @Override
    public Coordinates build(){
        Coordinates coordinates;
        coordinates = new Coordinates(askX(), askY());
        return coordinates;
    }
    public Long askX() {
        var fileMode = Interrogator.fileMode();
        long x;
        while (true) {
            try {
                console.println("Введите координату X:");
                console.prompt();
                var strX = console.readln().trim();;
                if (fileMode) console.println(strX);
                try {
                    x = Long.parseLong(strX);
                    if (x <= 514) break;
                } catch (NumberFormatException e) {
                    console.printError("Координата X должна быть представлена числом, которое не больше 514!");
                }
            } catch (NoSuchElementException exception) {
                console.printError("Координата X не распознана!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return (long) x;
    }
    public Long askY(){
        var fileMode = Interrogator.fileMode();
        long y;
        while (true) {
            try {
                console.println("Введите координату Y:");
                console.prompt();
                var strY = console.readln().trim();;
                if (fileMode) console.println(strY);

                y = Long.parseLong(strY);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Координата Y не распознана!");
            } catch (NumberFormatException exception) {
                console.printError("Координата Y должна быть представлена числом!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }
}