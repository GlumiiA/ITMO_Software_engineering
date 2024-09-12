package src.main.java.common.exceptions;

public class DemandException extends RuntimeException {
    public DemandException(String demand){
        super(demand);
    }
}