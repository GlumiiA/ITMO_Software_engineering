package src.main.java.common.object;

import src.main.java.common.exceptions.DemandException;

import java.io.Serial;
import java.io.Serializable;

public class House implements  Comparable<House>, Serializable {
    @Serial
    private static final long serialVersionUID = 132964L;
    private String name; //Поле не может быть null
    private Integer year; //Значение поля должно быть больше 0
    private Integer numberOfFlatsOnFloor; //Значение поля должно быть больше 0
    public House(String name, Integer year, Integer numberOfFlatsOnFloor) {
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    public void validValue() {
        if (name == null) throw new DemandException("Поле не может быть null.");
        if (year <= 0) throw new DemandException("Значение поля должно быть больше 0");
        if (numberOfFlatsOnFloor <= 0) throw new DemandException("Значение поля должно быть больше 0");
    }

    public void Update(House house) {
        this.name = house.name;
        this.year = house.year;
        this.numberOfFlatsOnFloor = house.numberOfFlatsOnFloor;
    }
    public String getName() {
        return name;
    }
    public Integer getYear() {
        return year;
    }
    public Integer getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    @Override
    public String toString() {
        String info;
        info = name + "\n"
                + "  year: " + year + "\n"
                + "  number of flats on floor: "+numberOfFlatsOnFloor;
        return info;
    }

    @Override
    public int compareTo(House o) {
        return (int) (this.numberOfFlatsOnFloor - ((House) o).numberOfFlatsOnFloor);
    }
}