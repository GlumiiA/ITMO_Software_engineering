package src.main.java.java.client.network;

import src.main.java.common.network.Response;
import src.main.java.java.client.utility.Data;

import java.io.*;
import java.net.*;

public class ClientUDP {
    DatagramSocket ds;
    DatagramPacket dp;
    InetAddress host;
    int port;
    int timeout;

    public ClientUDP(int timeout) throws SocketException, UnknownHostException {
        ds = new DatagramSocket();
        host = InetAddress.getLocalHost();
//        host = InetAddress.getByName("helios.cs.ifmo.ru");
        port = 5874;
        ds.setSoTimeout(timeout);
        this.timeout = timeout;
    }

    public boolean sendData(byte[] arr) {
        try {
            dp = new DatagramPacket(arr, arr.length, host, port);
            ds.send(dp);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
//    public boolean sendData(byte[] arr) {
//        try {
//            int chunkSize = 49; // размер части файла
//            int numChunks = (int) Math.ceil((double) arr.length / chunkSize);
//            for (int i = 0; i < numChunks; i++) {
//                int start = i * chunkSize;
//                int endd = Math.min(start + chunkSize, arr.length);
//                byte[] chunkData = Arrays.copyOfRange(arr, start, endd);
//                byte[] newChunkData = new byte[chunkData.length + 1];
//                System.arraycopy(chunkData, 0, newChunkData, 1, chunkData.length);
//                if (i+1 == numChunks) {
//                    newChunkData[0] = 1;
//                } else {
//                    newChunkData[0] = 0;
//                }
//                DatagramPacket packet = new DatagramPacket(newChunkData, newChunkData.length, host, port);
//                ds.send(packet);
//            }
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }

    public static byte[] serializer(Data obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static Response deserialize(byte[] bytes) {
        if (bytes == null) return new Response(false, "Ответ от сервера не получен.");
        InputStream is = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Response) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] receiveData(int len) {
        try {
            byte[] arr = new byte[len];
            dp = new DatagramPacket(arr, len);
            ds.receive(dp);
            return arr;
        } catch (IOException e) {
            return null;
        }
    }
}