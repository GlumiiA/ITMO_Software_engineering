package ru.rmntim;

import ru.rmntim.beans.PointBean;

public class Checker {
    double x;
    double y;
    int r;
    public Checker(PointBean point) {
        this.x = point.getX();
        this.y = point.getY();
        this.r = point.getR();
    }
    public static boolean CheckHit(double x, double y, int r){
        if ((x<=0 && y<=0) && (y<-r-x)){
            return false;
        }
        if ((x<=0 && y>=0) && (2*y > r || x<-r)){
            return false;
        }
        if ((x>=0 && y<=0) && ((x*x + y*y) > r*r)){
            return false;
        }
        if (x>0 && y>0){
            return false;
        }
        return true;
    }
}