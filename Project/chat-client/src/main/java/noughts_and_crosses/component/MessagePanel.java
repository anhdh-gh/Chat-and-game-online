package noughts_and_crosses.component;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import noughts_and_crosses.support.Exchangers;
import noughts_and_crosses.view.ClientFrame;

public class MessagePanel extends JPanel {
    
    private final String name;
    private final Socket socket;
    
    public MessagePanel(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        initComponents();
        sendMessage("Joined the group!");
    }
    
    public final void sendMessage(String str) {
        this.textEntryField.setText(str);
        sendButton.doClick();        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        center = new javax.swing.JScrollPane();
        messenger = new javax.swing.JTextArea();
        bottom = new javax.swing.JPanel();
        sendButton = new javax.swing.JButton();
        textEntryField = new javax.swing.JTextField();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "Messenger", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        setLayout(new java.awt.BorderLayout());

        center.setBackground(new java.awt.Color(0, 0, 0));
        center.setBorder(null);
        center.setAutoscrolls(true);
        center.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        messenger.setEditable(false);
        messenger.setBackground(new java.awt.Color(0, 0, 0));
        messenger.setColumns(20);
        messenger.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        messenger.setForeground(new java.awt.Color(255, 255, 255));
        messenger.setRows(5);
        messenger.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        center.setViewportView(messenger);

        add(center, java.awt.BorderLayout.CENTER);

        bottom.setLayout(new java.awt.BorderLayout());

        sendButton.setBackground(new java.awt.Color(255, 255, 255));
        sendButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        sendButton.setForeground(new java.awt.Color(0, 0, 0));
        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        bottom.add(sendButton, java.awt.BorderLayout.LINE_END);

        textEntryField.setBackground(new java.awt.Color(0, 0, 0));
        textEntryField.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        textEntryField.setForeground(new java.awt.Color(255, 255, 255));
        textEntryField.setCaretColor(new java.awt.Color(255, 255, 255));
        textEntryField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textEntryFieldKeyPressed(evt);
            }
        });
        bottom.add(textEntryField, java.awt.BorderLayout.CENTER);

        add(bottom, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void textEntryFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEntryFieldKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) 
            sendButton.doClick();
    }//GEN-LAST:event_textEntryFieldKeyPressed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String str = textEntryField.getText().trim();
        if(!str.isEmpty()) {
            try {
                Exchangers.send(socket, name + ": " + str + "\n");
            } catch (IOException ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        textEntryField.setText("");        
    }//GEN-LAST:event_sendButtonActionPerformed

    public void append(String text) {
        messenger.append(text);
        messenger.setCaretPosition(messenger.getDocument().getLength());        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottom;
    private javax.swing.JScrollPane center;
    private javax.swing.JTextArea messenger;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField textEntryField;
    // End of variables declaration//GEN-END:variables
}