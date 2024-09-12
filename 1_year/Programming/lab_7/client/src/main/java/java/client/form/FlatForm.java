package src.main.java.java.client.form;


import src.main.java.common.exceptions.MustBeNotEmptyException;
import src.main.java.common.exceptions.NotInDeclaredLimitsException;
import src.main.java.common.object.*;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.java.client.utility.Interrogator;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * Форма Flat
 */
public class FlatForm extends Form<Flat> {
    private final ConsoleInterface console;


    public FlatForm(ConsoleInterface console) {
        this.console = console;
        Flat.touchNextId();
    }


    @Override
    public Flat build(){
        var flat = new Flat(
                1,
                askName(),
                askCoordinates(),
                LocalDate.now(),
                askArea(),
                askNumberOfRooms(),
                askNumberOfBathrooms(),
                askFurnish(),
                askTransport(),
                askHouse());
        return flat;
    }
    private String askName(){
        String name;
        var fileMode = Interrogator.fileMode();
        while (true) {
            try {
                console.println("Введите название квартиры::");
                console.prompt();

                name = console.readln().trim();
                if (fileMode) console.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Название не распознано!");
            } catch (MustBeNotEmptyException exception) {
                console.printError("Название не может быть пустым!");
            } catch (IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }

        return name;
    }

    private Coordinates askCoordinates() {
        return new CoordinatesForm(console).build();
    }

    private Float askArea() {
        var fileMode = Interrogator.fileMode();
        float area;
        while (true) {
            try {
                console.println("Введите площадь квартиры:");
                console.prompt();

                var strArea = console.readln().trim();;
                if (fileMode) console.println(strArea);

                area = Float.parseFloat(strArea);
                if (area < 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Area не распознана!");
            } catch (NotInDeclaredLimitsException exception) {
                console.printError("Area должна быть больше нуля!");
            } catch (NumberFormatException exception) {
                console.printError("Площадь дома должна быть представлена числом!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return area;
    }
    private Long askNumberOfRooms(){
        var fileMode = Interrogator.fileMode();
        long numberOfRooms;
        while (true) {
            try {
                console.println("Введите количество комнат:");
                console.prompt();

                var strNumberOfRooms = console.readln().trim();;
                if (fileMode) console.println(strNumberOfRooms);
                numberOfRooms = Long.parseLong(strNumberOfRooms);
                if (numberOfRooms < 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Номер комнаты не распознана!");
            } catch (NotInDeclaredLimitsException exception) {
                console.printError("Номер комнаты должен быть больше нуля!");
            } catch (NumberFormatException exception) {
                console.printError("Номер комнаты должна быть представлена числом!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return numberOfRooms;
    }
    private Integer askNumberOfBathrooms(){
        var fileMode = Interrogator.fileMode();
        int numberOfBathrooms;
        while (true) {
            try {
                console.println("Введите номер ванных комнат:");
                console.prompt();

                var strNumberOfBathrooms = console.readln().trim();;
                if (fileMode) console.println(strNumberOfBathrooms);
                numberOfBathrooms = Integer.parseInt(strNumberOfBathrooms);
                if (numberOfBathrooms < 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Номер ванной комнаты не распознана!");
            } catch (NotInDeclaredLimitsException exception) {
                console.printError("Номер ванной комнаты должен быть больше нуля!");
            } catch (NumberFormatException exception) {
                console.printError("Номер ванной комнаты должна быть представлена числом!");
            } catch (NullPointerException | IllegalStateException exception) {
                console.printError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return numberOfBathrooms;
    }

    private Furnish askFurnish(){
        return new FurnishForm(console).build();
    }

    private Transport askTransport(){
        return new TransportForm(console).build();
    }
    private House askHouse(){
        return new HouseForm(console).build();
    }
}