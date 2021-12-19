package chat.view;

import chat.controller.Controller;
import chat.entities.Room;
import chat.entities.User;
import chat.enumeration.TypeRoom;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

public class CreateEditRoomView extends View {

    private String type;
    private Room room; 

    public CreateEditRoomView(Controller controller, String type) { // Type = "EDIT" or "CREATE"
        super(controller);
        initComponents();
        this.type = type;
        this.room = new Room(new ArrayList<>(Arrays.asList(controller.getCurrentUser())), controller.getCurrentUser());
        
        this.renderView();
    }
    
    public CreateEditRoomView(Controller controller, String type, Room room) {
        super(controller);
        initComponents();
        this.type = type;
        this.room = room;
        this.roomNameTextField.setText(room.getName());        
        this.renderView();
    }
    
    @Override
    public void renderView() {
        this.setTitle(this.controller.getCurrentUser().getNickname());
        // Hàm này cần phải viết để khởi tạo dữ liệu cho view
        // và cập nhật view khi có dữ liệu mới thay đổi
        
        // Cập nhật lại view
        switch(type) {
            case "CREATE": {
                this.headerLabel.setText("Create room");
                this.btnAction.setText("Create");
                break;
            }
            
            case "EDIT": {
                this.headerLabel.setText("Edit room");
                this.btnAction.setText("Edit");
                
                // Cập nhật lại room (trong trường hợp Edit)
                this.room = controller.getRoomByID(room.getId());
                if(this.room == null) {
                    controller.showHomeView();
                    controller.getCurrentWiew().showError("Bạn không còn là thành viên của phòng");
                    return;
                }
                break;
            }
        }
        
        // Hiển thị lại user trong hệ thống
        DefaultTableModel modelUsers = (DefaultTableModel) tableUser.getModel();
        if(modelUsers.getRowCount() > 0) modelUsers.setRowCount(0);
        
        this.controller.getUsers().forEach(user -> {
            if(user.getId() != controller.getCurrentUser().getId() && this.room.getMembers().stream().noneMatch(member -> member.getId() == user.getId()))
                modelUsers.addRow(user.tObject());
        });        
        
        // Hiển thị lại members
        DefaultTableModel modelMembers = (DefaultTableModel) tableMember.getModel();
        if(modelMembers.getRowCount() > 0) modelMembers.setRowCount(0);
        
        this.room.getMembers().forEach(member -> {
            if(member.getId() != controller.getCurrentUser().getId())
                modelMembers.addRow(member.tObject());
        });        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        roomNameTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableUser = new javax.swing.JTable();
        btnAddMember = new javax.swing.JButton();
        btnRemoveMember = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMember = new javax.swing.JTable();
        backBt = new javax.swing.JButton();
        btnAction = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Create Room");

        headerLabel.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabel.setText("Create room");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Room name:");

        roomNameTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
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
        tableUser.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableUser.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableUser);

        btnAddMember.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnAddMember.setText(">>>");
        btnAddMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMemberActionPerformed(evt);
            }
        });

        btnRemoveMember.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRemoveMember.setText("<<<");
        btnRemoveMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveMemberActionPerformed(evt);
            }
        });

        tableMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Member"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
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

        backBt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        backBt.setText("Back");
        backBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtActionPerformed(evt);
            }
        });

        btnAction.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnAction.setText("Create");
        btnAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(backBt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAction))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roomNameTextField))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(headerLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAddMember)
                                    .addComponent(btnRemoveMember))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roomNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(btnAddMember)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemoveMember))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(backBt)
                            .addComponent(btnAction))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void backBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtActionPerformed
        switch(this.type) {
            case "CREATE":
                this.controller.showHomeView();
                break;
            case "EDIT":
                this.controller.showRoomChatView(room);
                break;
        }
    }//GEN-LAST:event_backBtActionPerformed

    private void btnActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionActionPerformed
        switch(this.type) {
            case "CREATE": {
                // Nếu thành viên nhỏ hơn 2 thì báo lỗi
                if (room.getMembers().size() < 2) {
                    this.showError("Số lượng thành viên tối thiểu là 2");
                    return;
                }          

                // Nếu thành viên == 2
                if(room.getMembers().size() == 2) {
                    room.setTypeRoom(TypeRoom.Double); // Là chat đôi
                    controller.processCreateRoom(room);
                    return;
                }             
                
                // Nếu thành viên > 2
                String name = roomNameTextField.getText().trim();
                if (name.isEmpty()) {
                    this.showError("Tên phòng không được trống!");
                    return;
                }
                room.setName(name);
                room.setTypeRoom(TypeRoom.Group); // Là chat nhóm
                controller.processCreateRoom(room);
                break;
            }
                
            case "EDIT": {
                String name = roomNameTextField.getText().trim();
                if (name.isEmpty()) {
                    this.showError("Tên phòng không được trống!");
                    return;
                }
                
                room.setName(name);
                controller.processUpdateRoom(room);
                break;
            }
        }        
    }//GEN-LAST:event_btnActionActionPerformed

    private void btnAddMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMemberActionPerformed
        int selectedUser = tableUser.getSelectedRow();
        if (selectedUser < 0) {
            this.showError("Chưa chọn!");
        } 
        else {
            int id = (int) tableUser.getValueAt(selectedUser, 0);
            for (User user : controller.getUsers()) {
                if(user.getId() == id) {
                    this.room.getMembers().add(user);
                    break;
                }
            }
            
            this.renderView();
        }
    }//GEN-LAST:event_btnAddMemberActionPerformed

    private void btnRemoveMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveMemberActionPerformed
        int selectedUser = tableMember.getSelectedRow();
        if (selectedUser < 0) {
            this.showError("Chưa chọn!");
        }
        else {
            int id = (int) tableMember.getValueAt(selectedUser, 0);
            this.room.getMembers().removeIf(member -> member.getId() == id);
            
            this.renderView();
        }
    }//GEN-LAST:event_btnRemoveMemberActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBt;
    private javax.swing.JButton btnAction;
    private javax.swing.JButton btnAddMember;
    private javax.swing.JButton btnRemoveMember;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField roomNameTextField;
    private javax.swing.JTable tableMember;
    private javax.swing.JTable tableUser;
    // End of variables declaration//GEN-END:variables
}