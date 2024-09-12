package src.main.java.network;

import java.net.SocketAddress;

public class Record {
    private final byte[] arr;
    private final SocketAddress addr;

    public Record(byte[] arr, SocketAddress addr) {
        this.arr = arr;
        this.addr = addr;
    }
    public byte[] getArr() {
        return arr;
    }
    public SocketAddress getAddr() {
        return addr;
    }

}