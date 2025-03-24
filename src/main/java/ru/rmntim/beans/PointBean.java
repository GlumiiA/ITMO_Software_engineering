package ru.rmntim.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import static ru.rmntim.Checker.CheckHit;

@Getter
@Setter
@Named("pointBean")
@Table(name="points")
@SessionScoped
public class PointBean implements Serializable {
    private boolean is_hit;
    private double x = 0.0;
    private double y;
    private int r = 4;
    private Long executeTime;
    private Date curTime;

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getR() {
        return r;
    }
    public boolean getIs_hit() {return is_hit;}

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setR(int r) {
        this.r = r;
    }
    public void setIs_hit() {
        this.is_hit = CheckHit(x,y,r);
    }
    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }
}
