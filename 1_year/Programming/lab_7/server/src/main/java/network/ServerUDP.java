package src.main.java.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class ServerUDP {
    DatagramChannel dc;
    int port;
    SocketAddress addr;
    int timeout;
    byte[] bytes = new byte[5096];


    public ServerUDP(int port) {
        this.port = port;
    }

    public static final Logger logger = LoggerFactory.getLogger(ServerUDP.class);

    public boolean init() {
        try {
//            socket = new InetSocketAddress(InetAddress.getLocalHost(), port);
//          logger.info(socket.toString());
            addr = new InetSocketAddress(port);
            dc = DatagramChannel.open();
            dc.bind(addr);
            dc.configureBlocking(false);
            return true;
        } catch (SocketException e) {
            return false;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sendData(Record record) {
        try {
            ByteBuffer buf = ByteBuffer.wrap(record.getArr());
            dc.send(buf, record.getAddr());
            return true;
        } catch (IOException e) {
            logger.info(String.valueOf(e));
            return false;
        }
    }
//    public void sendData(byte[] data) {
//        try {
////            socket = new InetSocketAddress(InetAddress.getLocalHost(), port);
////            dc.send(buf, socket);
//            ByteBuffer buf = ByteBuffer.wrap(data);
//            dc.send(buf, addr);
////            DatagramPacket dpToSend = new DatagramPacket(data, data.length, socket, port);
////            ds.send(dpToSend);
//            logger.info("Ответ отправлен");
//
//        } catch (IOException ignored) {
//        }
//    }

    //        public void sendData(byte[] data) {
//        try {
//            DatagramPacket dp = new DatagramPacket(data, data.length);
//            logger.info(getSocketAddress().toString());
//            dc.send(dp);
//            logger.info("Ответ отправлен");
//        } catch (IOException ignored) {
//        }
//    }
    public boolean checkArray(byte[] array) {
        for (var e : array) {
            if (e != 0) return true;
        }
        return false;
    }

    public Record receiveData(int len) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(len);
            addr = dc.receive(buf); // var addr
            if (addr != null) {
                logger.info("Получен запрос от клиента!");
                if (checkArray(buf.array())) {
                    return new Record(buf.array(), addr);
                } else return null;
            }
            return null;
        } catch (IOException e) {
            logger.error("Не удалось получить данные.", e);
            return null;
        }
    }
//    public byte[] receiveData(int len) {
//        byte[] arr = new byte[10000];
//        try {
//            int i = 0;
//            while (true) {
//                int chunkSize = len - 1;
//                ByteBuffer buf = ByteBuffer.allocate(len);
//                addr = dc.receive(buf);
//                if (addr != null) {
//                    logger.info("Получен запрос от клиента!");
//                    byte[] chunkData = Arrays.copyOfRange(buf.array(), 1, chunkSize + 1);
//                    System.arraycopy(chunkData, 0, arr, i * chunkSize, chunkData.length);
//                    i += 1;
//                    if (buf.array()[0] == 1) {
//                        break;
//                    }
//                }
//            }
//            return arr;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static byte[] serializer(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.close();
            byte[] objBytes = bos.toByteArray();
            logger.info("Ответ успешно сериализован!");
            logger.info(Arrays.toString(objBytes));
            return objBytes;
        } catch (IOException e) {
            return null;
        }
    }

    public Request deserialize(byte[] bytes) {
        if (bytes == null) return null;
        InputStream is = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            logger.info("Команда успешно десериализована!");
            return (Request) ois.readObject();
        } catch (IOException e) {
            logger.error(String.valueOf(e));
            logger.error("Не удалось десереализовать объект");
            return null;
        } catch (ClassNotFoundException e) {
            logger.error("Не удалось десереализовать объект");
            return null;
        } catch (NullPointerException e) {
            logger.error(String.valueOf(e));
            return null;
        }
    }
}