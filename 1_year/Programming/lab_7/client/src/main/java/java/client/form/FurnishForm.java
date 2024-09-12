package src.main.java.java.client.form;

import  src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.common.object.Furnish;
import  src.main.java.java.client.utility.Interrogator;

import java.util.NoSuchElementException;

public class FurnishForm extends Form<Furnish> {
    private final ConsoleInterface console;

    public FurnishForm(ConsoleInterface console) {
        this.console = console;
    }

    @Override
    public Furnish build(){
        var fileMode = Interrogator.fileMode();

        String strFurnish;
        Furnish furnish;
        while (true) {
            try {
                console.println("Список состояния мебели - " + Furnish.names());
                console.println("Введите состояния:");
                console.prompt();

                strFurnish = console.readln().trim();;
                if (fileMode) console.println(strFurnish);

                furnish = Furnish.valueOf(strFurnish.toUpperCase());
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
        return furnish;
    }
}