package game.noughts_and_crosses.component;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import game.noughts_and_crosses.support.Exchangers;

public class Controller extends JPanel {

    private final Socket socket;
    private float id;
    
    public Controller(Socket socket) {
        this.socket = socket;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backBt = new javax.swing.JButton();
        backBt1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Controller", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        backBt.setBackground(new java.awt.Color(255, 255, 255));
        backBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        backBt.setForeground(new java.awt.Color(0, 0, 0));
        backBt.setText("◀ Back");
        backBt.setPreferredSize(new java.awt.Dimension(121, 29));
        backBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtActionPerformed(evt);
            }
        });
        add(backBt);

        backBt1.setBackground(new java.awt.Color(255, 255, 255));
        backBt1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        backBt1.setForeground(new java.awt.Color(0, 0, 0));
        backBt1.setText("New game ✔");
        backBt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBt1ActionPerformed(evt);
            }
        });
        add(backBt1);
    }// </editor-fold>//GEN-END:initComponents

    private void send(Object object) {
        try {
            Exchangers.send(socket, object);
        } catch (IOException ex) {
            Logger.getLogger(ChessboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void backBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtActionPerformed
        send(id);
    }//GEN-LAST:event_backBtActionPerformed

    private void backBt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBt1ActionPerformed
        if(id == 0 || id == 1) {
            send(true);
            if(id == 0) send(new int[] {1});
            else if(id == 1) send(new int[] {0});
        }
    }//GEN-LAST:event_backBt1ActionPerformed

    public void setId(int id) {
        this.id = (float) id;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBt;
    private javax.swing.JButton backBt1;
    // End of variables declaration//GEN-END:variables
}
