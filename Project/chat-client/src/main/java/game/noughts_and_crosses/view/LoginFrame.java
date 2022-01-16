package game.noughts_and_crosses.view;

import game.noughts_and_crosses.exception.LoginException;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import game.noughts_and_crosses.server.Server;

public class LoginFrame extends JFrame implements Runnable {

    private final boolean isCreator;
    private String fullName;
    private String hostName;
    private int portNumber;

    public LoginFrame(String fullName, String hostName, boolean isCreator) {
        this.isCreator = isCreator;
        this.fullName = fullName;
        this.hostName = "";
        this.portNumber = 0;
        initComponents();
        if(this.isCreator == true) {
            this.joinBt.setText("Create ▶");
            this.setTitle("Create a playroom");
        }
        getContentPane().setBackground(Color.BLACK);  
        if(!hostName.isEmpty()) {
            hostNameTextField.setText(hostName);
            hostNameTextField.setEditable(true);
        }
        fullNameTextFiled.setText(this.fullName);
        checkLoginIsEmpty();
        
        setIconImage(new ImageIcon(getClass().getResource("/noughts_and_crosses_icon.png")).getImage());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        top = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        fullNameTextFiled = new javax.swing.JTextField();
        center = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        hostNameTextField = new javax.swing.JTextField();
        center1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        portNumberTextField = new javax.swing.JTextField();
        bottom = new javax.swing.JPanel();
        backBt = new javax.swing.JButton();
        joinBt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Join the playroom");
        setMinimumSize(new java.awt.Dimension(500, 300));
        getContentPane().setLayout(new java.awt.GridLayout(4, 1));

        top.setBackground(new java.awt.Color(0, 0, 0));
        top.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        top.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nickname:");
        jLabel3.setPreferredSize(new java.awt.Dimension(175, 23));
        top.add(jLabel3, java.awt.BorderLayout.LINE_START);

        fullNameTextFiled.setEditable(false);
        fullNameTextFiled.setBackground(new java.awt.Color(0, 0, 0));
        fullNameTextFiled.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        fullNameTextFiled.setForeground(new java.awt.Color(255, 255, 255));
        fullNameTextFiled.setCaretColor(new java.awt.Color(255, 255, 255));
        fullNameTextFiled.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fullNameTextFiledKeyPressed(evt);
            }
        });
        top.add(fullNameTextFiled, java.awt.BorderLayout.CENTER);

        getContentPane().add(top);

        center.setBackground(new java.awt.Color(0, 0, 0));
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center.setPreferredSize(new java.awt.Dimension(190, 43));
        center.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Enter hostname:");
        jLabel1.setPreferredSize(new java.awt.Dimension(175, 23));
        center.add(jLabel1, java.awt.BorderLayout.LINE_START);

        hostNameTextField.setBackground(new java.awt.Color(0, 0, 0));
        hostNameTextField.setColumns(20);
        hostNameTextField.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        hostNameTextField.setForeground(new java.awt.Color(255, 255, 255));
        hostNameTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        hostNameTextField.setMinimumSize(new java.awt.Dimension(284, 23));
        hostNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hostNameTextFieldKeyPressed(evt);
            }
        });
        center.add(hostNameTextField, java.awt.BorderLayout.CENTER);

        getContentPane().add(center);

        center1.setBackground(new java.awt.Color(0, 0, 0));
        center1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        center1.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter the port number:");
        jLabel2.setPreferredSize(new java.awt.Dimension(175, 23));
        center1.add(jLabel2, java.awt.BorderLayout.LINE_START);

        portNumberTextField.setBackground(new java.awt.Color(0, 0, 0));
        portNumberTextField.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        portNumberTextField.setForeground(new java.awt.Color(255, 255, 255));
        portNumberTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        portNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                portNumberTextFieldKeyPressed(evt);
            }
        });
        center1.add(portNumberTextField, java.awt.BorderLayout.CENTER);

        getContentPane().add(center1);

        bottom.setBackground(new java.awt.Color(0, 0, 0));
        bottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.setLayout(new java.awt.BorderLayout());

        backBt.setBackground(new java.awt.Color(255, 255, 255));
        backBt.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        backBt.setForeground(new java.awt.Color(0, 0, 0));
        backBt.setText("◀ Back");
        backBt.setPreferredSize(new java.awt.Dimension(100, 23));
        backBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtActionPerformed(evt);
            }
        });
        bottom.add(backBt, java.awt.BorderLayout.LINE_START);

        joinBt.setBackground(new java.awt.Color(255, 255, 255));
        joinBt.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        joinBt.setForeground(new java.awt.Color(0, 0, 0));
        joinBt.setText("Join ▶");
        joinBt.setPreferredSize(new java.awt.Dimension(100, 23));
        joinBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinBtActionPerformed(evt);
            }
        });
        bottom.add(joinBt, java.awt.BorderLayout.LINE_END);

        getContentPane().add(bottom);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void checkLogin() throws LoginException {
        fullName = fullNameTextFiled.getText().trim();
        if(fullName.isEmpty()) {
            fullNameTextFiled.requestFocus();
            throw new LoginException("Full name is not empty!");
        }        
        
        hostName = hostNameTextField.getText().trim();
        if(hostName.isEmpty()) {
            hostNameTextField.requestFocus();
            throw new LoginException("Host name is not empty!");
        }
        
        String portNumberStr = portNumberTextField.getText().trim();        
        if(portNumberStr.isEmpty()) {
            portNumberTextField.requestFocus();
            throw new LoginException("Port number is not empty!");
        }          
      
        try {
            portNumber = Integer.parseInt(portNumberStr);
        } catch(NumberFormatException e) {
            portNumberTextField.requestFocus();
            throw new LoginException("Port number must be an integer!");
        }
        
        if(portNumber < 1024) {
            portNumberTextField.requestFocus();
            throw new LoginException("Port number must be greater than 1023!");
        }        
        
        if(portNumber > 65535) {
            portNumberTextField.requestFocus();
            throw new LoginException("Port number must be less than 65536!");            
        }
    }
    
    private boolean checkLoginIsEmpty() {
        if(fullNameTextFiled.getText().trim().isEmpty()) {
            fullNameTextFiled.requestFocus();
            return false;
        }
            
        else if(hostNameTextField.getText().trim().isEmpty()) {
            hostNameTextField.requestFocus();
            return false;
        }
            
        else if(portNumberTextField.getText().trim().isEmpty()) {
            portNumberTextField.requestFocus();
            return false;
        }
        return true;
    }
    
    private void backBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtActionPerformed
        new HomeFrame(fullNameTextFiled.getText().trim()).setVisible(true);
        dispose();
    }//GEN-LAST:event_backBtActionPerformed
 
    private void fullNameTextFiledKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fullNameTextFiledKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) 
            if(checkLoginIsEmpty()) joinBt.doClick();
    }//GEN-LAST:event_fullNameTextFiledKeyPressed

    private void hostNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hostNameTextFieldKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) 
            if(checkLoginIsEmpty()) joinBt.doClick();
    }//GEN-LAST:event_hostNameTextFieldKeyPressed

    private void portNumberTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portNumberTextFieldKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) 
            if(checkLoginIsEmpty()) joinBt.doClick();
    }//GEN-LAST:event_portNumberTextFieldKeyPressed

    private void joinBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinBtActionPerformed
        new Thread(this).start();
    }//GEN-LAST:event_joinBtActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBt;
    private javax.swing.JPanel bottom;
    private javax.swing.JPanel center;
    private javax.swing.JPanel center1;
    private javax.swing.JTextField fullNameTextFiled;
    private javax.swing.JTextField hostNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton joinBt;
    private javax.swing.JTextField portNumberTextField;
    private javax.swing.JPanel top;
    // End of variables declaration//GEN-END:variables

    private boolean inputChanges() {
        if(!fullName.equals(fullNameTextFiled.getText().trim()))
            return true;
        if(!hostName.equals(hostNameTextField.getText().trim()))
            return true;  
        return !Integer.toString(portNumber).equals(portNumberTextField.getText().trim());
    }
    
    @Override
    public void run() {
        try {
            checkLogin();
            if(isCreator == true) {
                Server server = new Server(portNumber); 
                new Thread(server).start();         
            }
            Socket socket = new Socket(hostName, portNumber);   
            ClientFrame client =  new ClientFrame(fullName, hostName, portNumber, socket);
            client.setVisible(true);
            new Thread(client).start();
            dispose();
        } catch(LoginException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch(BindException e) {
            portNumberTextField.requestFocus();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            if(inputChanges() == false && this.isVisible()) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, e);                
            }
        }        
    }
}