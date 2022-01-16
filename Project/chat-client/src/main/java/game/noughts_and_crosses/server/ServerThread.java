package game.noughts_and_crosses.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import game.noughts_and_crosses.support.Exchangers;

public class ServerThread extends Thread {

    private final Server server;
    private final Socket socket;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        synchronize();
    }
    
    // Đồng bộ trong trường hợp là client vào sau
    private void synchronize() {
        server.getData().forEach(datum -> {
            send(datum);
        });
    }
    
    public void send(Object object) {
        try {
            Exchangers.send(socket, object);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                Object object = Exchangers.recive(socket);
                server.sendForAllClients(object);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Mat ket noi voi client " + server.getServers().indexOf(this));
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                
                // Kiểm tra nếu client0 mất kết nối. Thì các đóng serverSocket
                if(server.getServers().indexOf(this) == 0) {
                    try {
                        server.getServerSocket().close();
                        break;
                    } catch (IOException ex1) {
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
                
                // Remove serverThread bị lỗi mất kết nối khỏi list và kết thúc thread này 
                server.getServers().remove(this);
                
                // Update id cho cac clients
                server.updateIdForClients();
                break;
            }
        }
    }
}