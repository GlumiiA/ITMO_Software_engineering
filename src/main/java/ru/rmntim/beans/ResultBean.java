package ru.rmntim.beans;

import jakarta.enterprise.context.SessionScoped;

import jakarta.inject.Named;
//import lombok.Getter;
//import lombok.Setter;
//import ru.rmntim.ResultsManager;

import java.io.Serializable;
import java.util.ArrayList;



@SessionScoped
@Named("resultBean")
public class ResultBean implements Serializable {
    ArrayList<PointBean> points = new ArrayList<>();
    public  void addPoint(PointBean point) {
        for (int i = 0; i < points.size(); i++) {
            System.out.println(points.get(i));
        }

        points.add(point);
    }

    public void setPoints(ArrayList<PointBean> points) {
        this.points = points;
    }

    public ArrayList<PointBean> getPoints() {
        return points;
    }

    public void clearPoints() {
        if (points != null) {
            points.clear();
        }
    }

}

