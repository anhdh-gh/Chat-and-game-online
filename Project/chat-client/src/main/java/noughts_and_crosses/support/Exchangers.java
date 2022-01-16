package noughts_and_crosses.support;

import java.io.*;
import java.net.*;

public class Exchangers {
    
    private Exchangers() {}
    
    public static void send(Socket socket, Object object) throws IOException {
        ObjectOutputStream sendObject = new ObjectOutputStream(socket.getOutputStream());
        sendObject.writeObject(object);
    }
    
    public static Object recive(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream reciveObject = new ObjectInputStream(socket.getInputStream());
        return reciveObject.readObject();
    }
}