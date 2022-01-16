package noughts_and_crosses.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

public class Server implements Runnable {

    private final int portNumber;
    private final ServerSocket serverSocket;
    private final List<ServerThread> serverThreads;
    private final List<Object> data;
    
    public Server(int portNumber) throws IOException {
        this.portNumber = portNumber;
        serverSocket = new ServerSocket(portNumber);
        serverThreads = new ArrayList<>();
        data = new ArrayList<>();
    }

    private Object findTurn() {        
        for (int i = data.size()-1 ; i >= 0 ; i--) {
            if(data.get(i) instanceof int[]) {
                int arr[] = (int[]) data.get(i);
                if(arr.length == 1) return data.get(i);
            }
        }
        return new int[] {0};
    }

    public int getPortNumber() {
        return portNumber;
    }
    
    public void sendForAllClients(Object object) {
        data.add(object);
        serverThreads.forEach(serverThread -> {
            serverThread.send(object);
        });
    }
    
    public void updateIdForClients() {
        serverThreads.forEach(serverThread -> {
            serverThread.send(serverThreads.indexOf(serverThread));
            serverThread.send(findTurn());
        });
    }

    public List<ServerThread> getServers() {
        return serverThreads;
    }

    public List<Object> getData() {
        return data;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept(); 
                ServerThread serverThread = new ServerThread(this, socket);
                serverThreads.add(serverThread);
                updateIdForClients();
                serverThread.start();  
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }
}