package chat.view;

import chat.controller.Controller;
import chat.entities.Room;
import java.awt.EventQueue;
import javax.swing.table.*;
import noughts_and_crosses.view.HomeFrame;

public class HomeView extends View {

    public HomeView(Controller controller) {
        super(controller);
        initComponents();
        this.renderView();
    } 

    @Override
    public void renderView() {
        this.setTitle(this.controller.getCurrentUser().getNickname());
        // Hàm này cần phải viết để khởi tạo dữ liệu cho view
        // và cập nhật view khi có dữ liệu mới thay đổi

        // Hiển thị phòng chat
        DefaultTableModel tbl = (DefaultTableModel) tableRooms.getModel();
        if(tbl.getRowCount() > 0) tbl.setRowCount(0);        
        controller.getCurrentUser().getRoomsJoined().forEach(room -> tbl.addRow(room.toObject()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRooms = new javax.swing.JTable();
        logOutBt = new javax.swing.JButton();
        createRoomBt = new javax.swing.JButton();
        changeNickNameBt = new javax.swing.JButton();
        joinBt = new javax.swing.JButton();
        changePasswordBt = new javax.swing.JButton();
        playGameBt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat app");

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chat app");

        tableRooms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Room name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableRooms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableRooms.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableRooms);

        logOutBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        logOutBt.setText("Log out");
        logOutBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtActionPerformed(evt);
            }
        });

        createRoomBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        createRoomBt.setText("Create room");
        createRoomBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createRoomBtActionPerformed(evt);
            }
        });

        changeNickNameBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        changeNickNameBt.setText("Change nick name");
        changeNickNameBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeNickNameBtActionPerformed(evt);
            }
        });

        joinBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        joinBt.setText("Join");
        joinBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinBtActionPerformed(evt);
            }
        });

        changePasswordBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        changePasswordBt.setText("Change password");
        changePasswordBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordBtActionPerformed(evt);
            }
        });

        playGameBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        playGameBt.setText("Play game");
        playGameBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playGameBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(createRoomBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(changeNickNameBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(changePasswordBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(joinBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logOutBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(playGameBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(joinBt)
                        .addGap(18, 18, 18)
                        .addComponent(createRoomBt)
                        .addGap(18, 18, 18)
                        .addComponent(changeNickNameBt)
                        .addGap(18, 18, 18)
                        .addComponent(changePasswordBt)
                        .addGap(18, 18, 18)
                        .addComponent(playGameBt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logOutBt))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logOutBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtActionPerformed
        controller.processLogOut();
    }//GEN-LAST:event_logOutBtActionPerformed

    private void changeNickNameBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeNickNameBtActionPerformed
        this.controller.showChangeMickNameView();
    }//GEN-LAST:event_changeNickNameBtActionPerformed

    private void changePasswordBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordBtActionPerformed
        this.controller.showChangePasswordView();
    }//GEN-LAST:event_changePasswordBtActionPerformed

    private void createRoomBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createRoomBtActionPerformed
        this.controller.showCreateRoomView();
    }//GEN-LAST:event_createRoomBtActionPerformed

    private void joinBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinBtActionPerformed
        int index = tableRooms.getSelectedRow();
        if (index >= 0) {
            int idRoom = (int) tableRooms.getValueAt(index, 0);
            Room roomSelected = controller.getRoomByID(idRoom);
            if(roomSelected == null)
                this.showError("Không tìm thấy room");
            else 
                this.controller.showRoomChatView(roomSelected);
        } 
        else this.showError("Bạn chưa chọn phòng");
    }//GEN-LAST:event_joinBtActionPerformed

    private void playGameBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playGameBtActionPerformed
        EventQueue.invokeLater(() -> {
            new HomeFrame(this.controller.getCurrentUser().getNickname()).setVisible(true);
        });
    }//GEN-LAST:event_playGameBtActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeNickNameBt;
    private javax.swing.JButton changePasswordBt;
    private javax.swing.JButton createRoomBt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton joinBt;
    private javax.swing.JButton logOutBt;
    private javax.swing.JButton playGameBt;
    private javax.swing.JTable tableRooms;
    // End of variables declaration//GEN-END:variables
}