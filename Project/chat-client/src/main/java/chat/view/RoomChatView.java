package chat.view;

import chat.controller.Controller;
import chat.entities.Message;
import chat.entities.Room;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

public class RoomChatView extends View {
    
    private Room room;
    
    public RoomChatView(Controller controller, Room room) {
        super(controller);
        initComponents();
        
        this.room = room;
        this.renderView();
    }
    
    @Override
    public void renderView() {
        this.setTitle(this.controller.getCurrentUser().getNickname());
        // Hàm này cần phải viết để khởi tạo dữ liệu cho view
        // và cập nhật view khi có dữ liệu mới thay đổi
        
        // Cập nhật lại room
        this.room = controller.getRoomByID(room.getId());
        if(this.room == null) {
            controller.showHomeView();
            controller.getCurrentView().showError("Bạn không còn là thành viên của phòng");
            return;
        }
        
        // Hiển thị tên phòng
        this.roomNameLabel.setText(this.room.getName());        
        
        // Hiển thị bảng members
        DefaultTableModel modelMembers = (DefaultTableModel) tableMember.getModel();
        if(modelMembers.getRowCount() > 0) modelMembers.setRowCount(0);
        this.room.getMembers().forEach(member -> modelMembers.addRow(member.tObject()));

        // Hiển thị message
        chatTextArea.setText("");
        this.room.getMessages().forEach(message -> {
            chatTextArea.append("<< " + message.getUserSend().getNickname() + " (" + message.getCreateDate() + ")" +" >>: \n");
            chatTextArea.append(message.getContent() + "\n");
            chatTextArea.append("\n");
        });
        chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
   
        // Thực hiện setAction
        this.btnDeleteRoom.setEnabled(false);
        this.btnEditRoom.setEnabled(false);
        this.btnExitRoom.setEnabled(false);    
        
        switch(this.room.getTypeRoom()) {
            case All:
                break;
            case Double: 
                this.btnDeleteRoom.setEnabled(true);
                break;
            case Group:
                // Members nào cũng có quyền exit, edit
                this.btnExitRoom.setEnabled(true);
                this.btnEditRoom.setEnabled(true);
                
                // Nếu là chủ phòng mới enable nút delete
                if(this.room.getOwner().getId() == this.controller.getCurrentUser().getId()) {
                    this.btnDeleteRoom.setEnabled(true);
                    this.btnEditRoom.setEnabled(true);
                }                
                break;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roomNameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMember = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        logOutBt = new javax.swing.JButton();
        messageTextField = new javax.swing.JTextField();
        sendMessageBtn = new javax.swing.JButton();
        btnEditRoom = new javax.swing.JButton();
        btnDeleteRoom = new javax.swing.JButton();
        btnExitRoom = new javax.swing.JButton();
        backBt = new javax.swing.JButton();
        chooseFile = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tên room");

        roomNameLabel.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        roomNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roomNameLabel.setText("Room name");

        tableMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Member"
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
        tableMember.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableMember.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableMember);

        chatTextArea.setEditable(false);
        chatTextArea.setColumns(20);
        chatTextArea.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        chatTextArea.setLineWrap(true);
        chatTextArea.setRows(5);
        jScrollPane2.setViewportView(chatTextArea);

        logOutBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        logOutBt.setText("Log out");
        logOutBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtActionPerformed(evt);
            }
        });

        messageTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        messageTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageTextFieldKeyPressed(evt);
            }
        });

        sendMessageBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        sendMessageBtn.setText("Send");
        sendMessageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageBtnActionPerformed(evt);
            }
        });

        btnEditRoom.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnEditRoom.setText("Edit room");
        btnEditRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRoomActionPerformed(evt);
            }
        });

        btnDeleteRoom.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnDeleteRoom.setText("Delete room");
        btnDeleteRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRoomActionPerformed(evt);
            }
        });

        btnExitRoom.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnExitRoom.setText("Exit");
        btnExitRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitRoomActionPerformed(evt);
            }
        });

        backBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        backBt.setText("Back");
        backBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtActionPerformed(evt);
            }
        });

        chooseFile.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        chooseFile.setText("Choose file");
        chooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backBt)
                        .addGap(281, 281, 281)
                        .addComponent(chooseFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logOutBt))
                    .addComponent(roomNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEditRoom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExitRoom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteRoom))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(messageTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendMessageBtn))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roomNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendMessageBtn)
                    .addComponent(btnEditRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExitRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logOutBt)
                    .addComponent(backBt)
                    .addComponent(chooseFile))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtActionPerformed
        this.controller.showHomeView();
    }//GEN-LAST:event_backBtActionPerformed

    private void logOutBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtActionPerformed
        this.controller.processLogOut();
    }//GEN-LAST:event_logOutBtActionPerformed

    private void btnEditRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRoomActionPerformed
        this.controller.showEditRoomView(room);
    }//GEN-LAST:event_btnEditRoomActionPerformed

    private void btnExitRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitRoomActionPerformed
        int chooosen = this.showConfirm("Bạn có thực sự muốn rời khỏi phòng");
        if(chooosen == 0) {
            controller.processExitRoom(room);
        }
    }//GEN-LAST:event_btnExitRoomActionPerformed

    private void btnDeleteRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRoomActionPerformed
        int choosen = this.showConfirm("Bạn có thực sự muốn xóa phòng");
        if (choosen == 0) {
            controller.processDeleteRoom(room);
        }
    }//GEN-LAST:event_btnDeleteRoomActionPerformed

    private void sendMessageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageBtnActionPerformed
        String messageSendContent = this.messageTextField.getText().trim();
        if(!messageSendContent.isEmpty()) { 
            controller.processSendMessage(new Message(messageSendContent, controller.getCurrentUser(), room));
            this.messageTextField.setText("");
        }
    }//GEN-LAST:event_sendMessageBtnActionPerformed

    private void messageTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
            sendMessageBtn.doClick();
    }//GEN-LAST:event_messageTextFieldKeyPressed

    private void chooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseFileActionPerformed
        File[] files = this.showSelectMultipleFiles("Send");
        if(files.length > 0)
            this.controller.sendFile(this.room, files);
    }//GEN-LAST:event_chooseFileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBt;
    private javax.swing.JButton btnDeleteRoom;
    private javax.swing.JButton btnEditRoom;
    private javax.swing.JButton btnExitRoom;
    private javax.swing.JTextArea chatTextArea;
    private javax.swing.JButton chooseFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton logOutBt;
    private javax.swing.JTextField messageTextField;
    private javax.swing.JLabel roomNameLabel;
    private javax.swing.JButton sendMessageBtn;
    private javax.swing.JTable tableMember;
    // End of variables declaration//GEN-END:variables
}