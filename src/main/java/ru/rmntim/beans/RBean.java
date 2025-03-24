package ru.rmntim.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SlideEndEvent;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Named("rBean")
@SessionScoped
public class RBean implements Serializable {

    private int r = 4;;

    public int getRValue(){
        return r;
    }

    public void setRValue(int r){
        this.r = r;
    }
}
