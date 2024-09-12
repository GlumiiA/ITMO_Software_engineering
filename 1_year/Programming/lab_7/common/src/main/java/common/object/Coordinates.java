package src.main.java.common.object;

import src.main.java.common.exceptions.DemandException;

import java.io.Serial;
import java.io.Serializable;

public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = 13344L;
    private Long x; //Максимальное значение поля: 514, Поле не может быть null
    private Long y; //Поле не может быть null
    public Coordinates (Long x, Long y) {
        this.x = x;
        this.y = y;
    }
    public void validValue() {
        if (x == null) throw new DemandException("Поле не может быть null");
        if (x > 514) throw new DemandException("Максимальное значение поля: 514");
    }
    public void Update(Coordinates coordinates){
        this.x= coordinates.x;
        this.y= coordinates.y;
    }
    public long getX(){
        return x;
    }
    public long getY(){
        return y;
    }
    @Override
    public String toString() {
        String info;
        info = "(" + x +", "+ y + ")";
        return info;
    }

}