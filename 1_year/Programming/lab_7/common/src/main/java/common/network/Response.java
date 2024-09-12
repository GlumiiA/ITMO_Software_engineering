package src.main.java.common.network;

import src.main.java.common.object.Flat;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 12465L;
    private boolean exitCode;
    private String message;
    private Flat flatToResponse;

    private List<Flat> collectionToResponse;

    /**
     * Конструктор класса Response, принимающий сообщение и информацию об квартире для ответа.
     */
    public Response(String message, Flat flatToResponse) {
        this.message = message;
        this.flatToResponse = flatToResponse;
    }

    /**
     * Конструктор класса Response, принимающий сообщение и коллекцию квартир для ответа.
     */
    public Response(String message, List<Flat> collectionToResponse) {
        this.message = message;
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Конструктор класса Response, принимающий информацию об квартире для ответа.
     */
    public Response(Flat flatToResponse) {
        this.flatToResponse = flatToResponse;
    }

    /**
     * Конструктор класса Response, принимающий коллекцию квартир для ответа.
     */
    public Response(List<Flat> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Метод, возвращающий сообщение для ответа.
     */
    public String getMessageToResponse() {
        return message;
    }

    /**
     * Метод, возвращающий информацию об организации для ответа.
     */
    public Flat getFlatToResponse() {
        return flatToResponse;
    }

    /**
     * Метод, возвращающий коллекцию организаций для ответа.
     */
    public List<Flat> getCollectionToResponse() {
        return collectionToResponse;
    }
    public Response(boolean code, String s) {
        exitCode = code;
        message = s;
    }

    public Response(String s) {
        this(true, s);
    }

    public boolean getExitCode() {
        return exitCode;
    }
    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.valueOf(exitCode) + ";" + message;
    }
}