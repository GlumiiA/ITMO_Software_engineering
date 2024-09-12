package src.main.java.manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.object.Flat;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CollectionManager  {
    public static final Logger logger = LoggerFactory.getLogger(CollectionManager.class);
    private int nextId;
    private LinkedHashSet<Flat> collection = new LinkedHashSet<>();
    LocalDateTime lastInitTime;
    LocalDateTime lastSaveTime;
    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();
    private ConcurrentSkipListMap<Integer, Flat> flats = new ConcurrentSkipListMap();
    private DatabaseHandler dbHandler;
    private Set<Integer> usedIds = new HashSet<>();

    public CollectionManager(DatabaseHandler dbHandler) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dbHandler = dbHandler;
        loadCollection();
    }

    public void init(){
        collection = dbHandler.loadCollectionFromDB();
    }
    /**
     * Загружает коллекцию из базы данных.
     */

    public LinkedHashSet<Flat> getCollection() {
        return collection;
    }
    public void sort() {
        collection = new LinkedHashSet<Flat>(collection.stream().
                sorted().collect(Collectors.toList()));
    }
    public boolean loadCollection() {
        // загружать id через бд
        collection.clear();
        collection = dbHandler.loadCollectionFromDB();
        for (Flat e : collection)
            if (getById(e.getId()) != null) {
                collection.clear();
                return false;
            } else {
                flats.put(e.getId(), e);
            }
        sort();
        return true;
    }
    private int generateUniqueId() {
        int id = 1;
        while (usedIds.contains(id)) {
            id++; // Увеличиваем ID, пока не найдем уникальный
        }
        return id;
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Имя типа коллекции.
     */
    public String collectionType() {
        return collection.getClass().getName();
    }

    /**
     * @return Размер коллекции.
     */
    public int collectionSize() {
        return collection.size();
    }

    /**
     * @return Первый элемент коллекции (null если коллекция пустая).
     */
    public Flat getFirst() {
        if (collection.isEmpty()) return null;
        return collection.iterator().next();
    }

    /**
     * @return Последний элемент коллекции (null если коллекция пустая).
     */
    public Flat getLast() {
        if (collection.isEmpty()) return null;
        Flat lastElement = null;
        Iterator<Flat> iterator = collection.iterator();  // Создаем итератор один раз

        while (iterator.hasNext()) {  // Используем этот итератор для обхода
            lastElement = iterator.next();  // Присваиваем текущий элемент переменной
        }

        return lastElement;  // Возвращаем последний элемент
    }

    /**
     * @param id ID элемента.
     * @return Элемент по его ID или null, если не найдено.
     */
    public Flat getById(int id) {
        return flats.get(id);
    }
    /**
     * @param number number of bathrooms элемента.
     * @return Проверяет, существует ли элемент с таким NumberOfBathrooms
     */
    public boolean checkExistNumberOfBathrooms(Integer number) {
        for (Flat element : collection) {
            if (element.getNumberOfBathrooms() == number) return true;
        }
        return false;
    }
    public void addToCollection(Flat element) {
        collectionLocker.writeLock().lock();
        collection.add(element);
        flats.put(element.getId(), element);
        collectionLocker.writeLock().unlock();
    }

    /**
     * Удаляет элемент из коллекции.
     */
    public void removeFromCollection(Flat element) {
        collectionLocker.writeLock().lock();
        try{
            logger.info("Удаление элемента:");
            logger.info("до: " + String.valueOf(collection));
            boolean removed = collection.remove(element);
            logger.info("после" + String.valueOf(collection));
            Flat.touchPreviousId();
            if (removed) {
                logger.info("Элемент  успешно удален.");
            } else {
                logger.info("Элемент не был удален.");
            }
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Очищает коллекцию.
     */
    public void clearCollection(List<Integer> clearList) {
        for (int el : clearList){
            Flat flatToRemove = getById(el);
            removeFromCollection(flatToRemove);
        }
    }

    /**
     * Сохраняет коллекцию в файл
     */
    public void saveCollection() {
        lastSaveTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        for (Flat flat : collection) {
            info.append(flat + "\n");
        }
        return info.toString().trim();
    }

    public int generateId() {
        return nextId++;
    }
}
