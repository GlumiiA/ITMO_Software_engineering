package src.main.java.java.client.form;

import src.main.java.common.object.House;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.java.client.utility.Interrogator;

import java.util.NoSuchElementException;

/**
 * Форма дома.
 */
public class HouseForm extends Form<House> {
    private final ConsoleInterface console;

    public HouseForm(ConsoleInterface console) {
        this.console = console;
    }

    @Override
    public House build() {
        var house = new House(
                askName(),
                askYear(),
                askNumberOfFlatsOnFloor ());
        return house;
    }

    private String askName() {
        String name;
        var fileMode = Interrogator.fileMode();
        while (true) {
            try {
                console.println("Введите название дома:");
                console.prompt();

                name = console.readln().trim();;
                if (fileMode) console.println(name);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Название дома не распознано!");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }

        return name;
    }

    private int askYear() {
        int year;
        var fileMode = Interrogator.fileMode();
        while (true) {
            try {
                console.println("Введите год постройки:");
                console.prompt();

                var strYear = console.readln().trim();;
                if (fileMode) console.println(strYear);
                year = Integer.parseInt(strYear);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Год постройки не распознано!");
            } catch (NumberFormatException exception) {
                console.printError("Год постройки должно быть представлено числом!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return year;
    }

    private int askNumberOfFlatsOnFloor () {
        int numberOfFlats;
        var fileMode = Interrogator.fileMode();
        while (true) {
            try {
                console.println("Введите количество квартир:");
                console.prompt();

                var strNumberOfFlats = console.readln().trim();;
                if (fileMode) console.println(strNumberOfFlats);
                numberOfFlats = Integer.parseInt(strNumberOfFlats);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("количество квартир не распознано!");
            } catch (NumberFormatException exception) {
                console.printError("количество квартир должно быть представлено числом!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return numberOfFlats;
    }
}