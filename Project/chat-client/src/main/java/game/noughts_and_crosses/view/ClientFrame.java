package game.noughts_and_crosses.view;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import game.noughts_and_crosses.component.ChessboardPanel;
import game.noughts_and_crosses.component.Controller;
import game.noughts_and_crosses.component.MessagePanel;
import game.noughts_and_crosses.support.Exchangers;

public class ClientFrame extends JFrame implements Runnable {

    private int id;
    private final String name;
    private final String hostName;
    private final int portNumber;
    private final Socket socket;
    private final MessagePanel messagePanel;
    private ChessboardPanel chessboardPanel;
    private final Controller controller;
    
    public ClientFrame(String name, String hostName, int portNumber, Socket socket) throws IOException {
        this.name = name;
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.socket = socket;
        messagePanel = new MessagePanel(this.socket, this.name);
        chessboardPanel = new ChessboardPanel(this.socket);
        controller = new Controller(this.socket);
        initComponents();
        
        // Thêm phần chessboard vào center của frame
        getContentPane().add(chessboardPanel);           
        // Thêm phần messenger vào left của frame
        left.add(messagePanel);
        // Thêm phần controlor vào left của frame
        left.add(controller, BorderLayout.SOUTH);
        
        setIconImage(new ImageIcon(getClass().getResource("/noughts_and_crosses_icon.png")).getImage());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        left = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Host name: " + hostName + " - Port number: " + portNumber);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(500, 500));

        left.setLayout(new java.awt.BorderLayout());
        getContentPane().add(left, java.awt.BorderLayout.LINE_START);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void run() {
        while(true) {
            try { 
                Object object = Exchangers.recive(socket);
                
                // Dữ liệu là message
                if(object instanceof String) {
                    messagePanel.append((String) object);
                    System.out.println("String");
                }
                    
                // Dữ liệu là int[]
                else if(object instanceof int[]) {
                    int[] arr = (int[]) object;
                    if(arr.length == 1) {
                        if(arr[0] == id && !chessboardPanel.getTurn().contains("win") && !chessboardPanel.getTurn().contains("Draw")) {
                            chessboardPanel.setOpenTurn(true);
                            System.out.println("luot = " + arr[0]);
                        }                    
                        else chessboardPanel.setOpenTurn(false);
                    }
                    else if(arr.length == 2) {
                        chessboardPanel.click(arr[0], arr[1]);
                        System.out.println(arr[0] + "  -   " + arr[1]);
                    }
                }
                
                // Dữ liệu là một số nguyên
                else if(object instanceof Integer) {
                    id = (Integer) object;
                    chessboardPanel.setId(id);
                    controller.setId(id);
                    System.out.println("set id = " + id);
                    this.setTitle("Host name: " + hostName + " - Port number: " + portNumber + " - Id: " + id + " - Name: " + name);
                }
                
                // Dữ liệu là một boolean
                else if(object instanceof Boolean) {
                    getContentPane().remove(chessboardPanel);
                    chessboardPanel = new ChessboardPanel(this.socket);
                    getContentPane().add(chessboardPanel);
                    chessboardPanel.setId(id);
                    validate();
                    System.out.println("da new game");
                }

                // Dữ liệu là một số thực float 
                else if(object instanceof Float) {
                    float f = (Float) object;
                    if(f == id || f == 0) {
                        socket.close();
                        System.out.println("da đóng backbt");
                        throw new IOException();
                    }
                }
                
                // Cập nhận lại frame
                validate();
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(name + " mat ket noi voi server");
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                // Khi mat ket noi voi server
                // <=> Xảy ra ngoại lệ <=> Đóng frame
                dispose();
                new HomeFrame(name).setVisible(true);
                break;
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel left;
    // End of variables declaration//GEN-END:variables
}