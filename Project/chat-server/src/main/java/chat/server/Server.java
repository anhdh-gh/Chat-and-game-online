package chat.server;

import chat.service.Service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@Component
public class Server extends Thread {

    private final ServerSocket serverSocket;
    private final List<ServerThread> serverThreads; // Tất cả các socket đang kết nối tới server

    @Autowired
    private Service service;

    public Server() throws IOException {
        serverSocket = new ServerSocket(2709);
        serverThreads = new ArrayList<>();
    }

    public void refreshAll() throws IOException {
        for (ServerThread st : serverThreads) {
            st.refresh();
        }
    }

    public boolean checkUserLoggedIn(int id) {
        return serverThreads.stream().anyMatch(st -> st.getCurrentUserID() == id);
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                // Đã kết nối được với client

                // Chuyển xuống cho serverThread
                ServerThread serverThread = new ServerThread(this, socket);

                // Thêm 1 client vào list
                serverThreads.add(serverThread);

                // Chạy serverThread để kết nối với client đó
                serverThread.start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}