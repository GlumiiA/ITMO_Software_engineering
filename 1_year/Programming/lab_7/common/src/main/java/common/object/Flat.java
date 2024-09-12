package src.main.java.common.object;


import src.main.java.common.exceptions.DemandException;
import src.main.java.manager.CollectionManager;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
public class Flat implements  Comparable<Flat>, Serializable {
    @Serial
    private static final long serialVersionUID = 13234L;
    private static int nextId = 1;
    private int id;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate;//Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float area; //Значение поля должно быть больше 0
    private long numberOfRooms; //Значение поля должно быть больше 0
    private Integer numberOfBathrooms; //Поле не может быть null, Значение поля должно быть больше 0
    private Furnish furnish; //Поле не может быть null
    private Transport transport; //Поле не может быть null
    private House house; //Поле может быть null
    private static Map<Integer, Flat> flats = new HashMap<>();
    public Flat() {}

    private static HashMap<Long, Flat> flatIdMap;

    static {
        flatIdMap = new HashMap<>();
    }

    public Flat(Integer id, String name, Coordinates coordinates, LocalDate creationDate, Float area, Long numberOfRooms, Integer numberOfBathrooms, Furnish furnish, Transport transport, House house) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.from(creationDate);
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.furnish = furnish;
        this.transport = transport;
        this.house = house;

        flats.put(this.id, this);
    }

    public static void touchNextId() {
        nextId++;
    }
    public static void touchPreviousId(){ nextId--;}
    public static void ClearId(){ nextId = 1;}
    public static Flat getById(int id) {
        return flatIdMap.get(id);
    }

    public static void updateNextId(CollectionManager collectionManager) {
        var maxId = collectionManager
                .getCollection()
                .stream().filter(obj -> Objects.nonNull(obj))
                .map(Flat::getId)
                .mapToInt(Integer::intValue).max().orElse(0);
    }

    public boolean validValue(){
        if(name.isEmpty()) throw new DemandException("Поле не может быть null, cтрока не может быть пустой.");
        if(coordinates == null) throw new DemandException("Поле не может быть null.");
        if (creationDate == null)throw new DemandException("Поле не может быть null.");
        if (area <= 0)throw new DemandException("Значение поля должно быть больше 0.");
        if (numberOfRooms <= 0)throw new DemandException("Значение поля должно быть больше 0.");
        if(numberOfBathrooms == null) throw new DemandException("Поле не может быть null");
        if (numberOfBathrooms <= 0 ) throw new DemandException("Значение поля должно быть больше 0.");
        if (furnish == null)throw new DemandException("Поле не может быть null.");
        if (transport == null)throw new DemandException("Поле не может быть null.");
        if (house == null)throw new DemandException("Поле не может быть null.");
        return true;
    }
    public static Map<Integer, Flat> allFlats() {
        return flats;
    }
    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public Furnish getFurnish(){ return  furnish;}
    public Transport getTransport(){ return transport;}
    public  House getHouse(){ return house;}

    public Long getNumberOfRooms() {
        return numberOfRooms;
    }
    public String getName() {
        return name;
    }

    public float getArea(){
        return area;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }
    public LocalDate getCreationDate(){
        return creationDate;
    }

    public static Flat byId(Integer id) {
        return flats.get(id);
    }



    @Override
    public String toString(){
        String info;
        info = "id: " + id + "\n"
                + "name: " + name + "\n"
                + "coordinates: " + coordinates + "\n"
                + "creationDate: " + creationDate.toString() + "\n"
                + "area: " + area + "\n"
                + "numberOfRooms: " + numberOfRooms + "\n"
                + "numberOfBathrooms: " + numberOfBathrooms + "\n"
                + "furnish: " + furnish + "\n"
                + "transport: " + transport + "\n"
                + "house: " + house + "\n\n";
        return info;
    }
    public void update(Flat flat) {
        this.name = flat.name;
        this.coordinates = flat.coordinates;
        this.creationDate = flat.creationDate;
        this.area = flat.area;
        this.numberOfRooms= flat.numberOfRooms;
        this.numberOfBathrooms = flat.numberOfBathrooms;
        this.furnish  = flat.furnish ;
        this.transport = flat.transport;
        this.house  = flat.house ;
    }
    public int compareTo(Flat o) {
        return (int) (this.numberOfBathrooms - ((Flat) o).numberOfBathrooms);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Сравнение по ссылкам
        if (obj == null || getClass() != obj.getClass()) return false; // Проверка на null и совместимость

        Flat flat = (Flat) obj; // Приведение типа
        return id == flat.id; // Сравнение полей по id
    }

}
