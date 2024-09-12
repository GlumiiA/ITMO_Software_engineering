package src.main.java.java.client.form;


/**
 * Абстрактный класс формы для ввода пользовательских данных.
 * @param <T> создаваемый объект
 */
public abstract class Form<T> {
    public abstract T build();
}