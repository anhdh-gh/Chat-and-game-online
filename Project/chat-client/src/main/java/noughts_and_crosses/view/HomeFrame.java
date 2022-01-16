package noughts_and_crosses.view;

import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.util.logging.*;
import noughts_and_crosses.support.Ip;

public class HomeFrame extends JFrame {

    private final String fullName;
    
    public HomeFrame(String fullName) {
        initComponents();
        getContentPane().setBackground(Color.BLACK);
        this.fullName = fullName;
        
        setIconImage(new ImageIcon(getClass().getResource("/noughts_and_crosses_icon.png")).getImage());
        center.setIcon(new javax.swing.ImageIcon(getClass().getResource("/noughts_and_crosses_background.png")));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        center = new javax.swing.JLabel();
        bottom = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        nameLable = new javax.swing.JLabel();
        joinButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Noughts and crosses");

        center.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        bottom.setBackground(new java.awt.Color(0, 0, 0));

        createButton.setBackground(new java.awt.Color(255, 255, 255));
        createButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        createButton.setForeground(new java.awt.Color(0, 0, 0));
        createButton.setText("⚔ Create room ");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });
        bottom.add(createButton);

        nameLable.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        nameLable.setForeground(new java.awt.Color(255, 255, 255));
        nameLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nameLable.setText("Noughts and crosses");
        bottom.add(nameLable);

        joinButton.setBackground(new java.awt.Color(255, 255, 255));
        joinButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        joinButton.setForeground(new java.awt.Color(0, 0, 0));
        joinButton.setText("Join room ✔");
        joinButton.setPreferredSize(new java.awt.Dimension(171, 34));
        joinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinButtonActionPerformed(evt);
            }
        });
        bottom.add(joinButton);

        getContentPane().add(bottom, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(568, 539));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        try {
            String hostName = Ip.getIpv4();
            new LoginFrame(fullName, hostName, true).setVisible(true);
            dispose();
        } catch (SocketException | UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "The hostname was not found!", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void joinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        new LoginFrame(fullName, "", false).setVisible(true);
        dispose();        
    }//GEN-LAST:event_joinButtonActionPerformed

    public JButton getCreateButton() {
        return createButton;
    }

    public JButton getJoinButton() {
        return joinButton;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottom;
    private javax.swing.JLabel center;
    private javax.swing.JButton createButton;
    private javax.swing.JButton joinButton;
    private javax.swing.JLabel nameLable;
    // End of variables declaration//GEN-END:variables
}