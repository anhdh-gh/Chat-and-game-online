package chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class Client {
    private final Socket socket;
    private final ObjectOutputStream sendObject;
    private final ObjectInputStream receiveObject;

    public Client() throws IOException {
        // Thực hiện kết nối 
        this.socket = new Socket("26.220.153.207", 2709);
        // Lấy luồng vào ra
        sendObject = new ObjectOutputStream(socket.getOutputStream());
        receiveObject = new ObjectInputStream(socket.getInputStream());
    }
    
    public void closeAll() {
        try {
            this.socket.close();
            this.receiveObject.close();
            this.sendObject.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(Object object) throws IOException {
        sendObject.writeObject(object);
        sendObject.flush();
    }
    
    public Object receive() throws IOException, ClassNotFoundException {
        return receiveObject.readObject();
    }   
}