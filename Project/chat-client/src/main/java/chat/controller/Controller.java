package chat.controller;

import chat.client.Client;
import chat.common.ServiceResult;
import chat.entities.FileInfo;
import chat.entities.Message;
import chat.entities.Room;
import chat.entities.User;
import chat.enumeration.RequestType;
import chat.enumeration.Status;
import chat.enumeration.TypeRoom;
import chat.view.ChangeNickNameView;
import chat.view.ChangePasswordView;
import chat.view.CreateEditRoomView;
import chat.view.ForgotPasswordView;
import chat.view.HomeView;
import chat.view.LoginView;
import chat.view.RoomChatView;
import chat.view.RegisterView;
import chat.view.View;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.util.Pair;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Controller extends Thread {
    
    @Autowired
    private Client client;
    
    private View currentView;
    private User currentUser; // Phải là một đối tượng user đầy đủ 
    private List<User> users; // Tât cả user có trong hệ thống 

    public void send(Object object) {
        try {
            this.client.send(object);
        } catch (IOException ex) {
            System.out.println("ERROR: Gửi Object bị lỗi");
            currentView.showError("Lỗi kết nối với server");
            currentView.deleteView();
            this.client.closeAll();
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object receive() {
        try {
            return this.client.receive();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("ERROR: Nhận Object bị lỗi");
            currentView.showError("Lỗi kết nối với server");
            currentView.deleteView();
            this.client.closeAll();
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }   
    
    private void showView(View view) {
        if(currentView != null) currentView.deleteView();
        this.currentView = view;
        this.refresh();
        currentView.displayView();
    }
    
    public void processRegister(User user) {
        this.send(new ServiceResult(user, RequestType.User_Register));
    }
    
    public void processLogin(User user) {
        this.send(new ServiceResult(user, RequestType.User_Login));
    }
    
    public void processForgotPassword(User user) {
        this.send(new ServiceResult(user, RequestType.User_ForgotPassword));
    }
    
    public void processLogOut() {
        this.send(new ServiceResult(RequestType.User_LogOut));
    }
    
    public void processChangePassword(String oldPassword, String newPassword) {
        Map<String, Object> data = new HashMap<>();
        data.put("oldPassword", oldPassword);
        data.put("newPassword", newPassword);
        this.send(new ServiceResult(data, RequestType.User_ChangePassword));
    }
    
    public void processChangeNickname(String newNickname, String password) {
        Map<String, Object> data = new HashMap<>();
        data.put("newNickname", newNickname);
        data.put("password", password);
        this.send(new ServiceResult(data, RequestType.User_ChangeNickname));        
    }
    
    public void processCreateRoom(Room room) {
        this.send(new ServiceResult(room, RequestType.Room_Create));
    }
    
    public void processUpdateRoom(Room room) {
        this.send(new ServiceResult(room, RequestType.Room_Update));
    }
    
    public void processExitRoom(Room room) {
        this.send(new ServiceResult(room, RequestType.User_ExitRoom));
    }
    
    public void processDeleteRoom(Room room) {
        this.send(new ServiceResult(room, RequestType.Delete_Room));
    }
    
    public void processSendMessage(Message message) {
        this.send(new ServiceResult(message, RequestType.Message_Send));
    }
    
    // Show LoginView
    public void showLoginView() {
        this.showView(new LoginView(this));
    }
    
    // Show LoginView có user
    public void showLoginView(User user) {
        this.showView(new LoginView(this, user));
    }
    
    // Show HomeView
    public void showHomeView() {
        this.showView(new HomeView(this));
    }    
    
    // Show ForgotPasswordView
    public void showForgotPassword() {
        this.showView(new ForgotPasswordView(this));
    }
    
    // Show RegisterView
    public void showRegisterView() {
        this.showView(new RegisterView(this));
    }
    
    public Room getRoomByID(int id) {
        return this.currentUser
            .getRoomsJoined()
            .stream()
            .filter(room -> room.getId() == id)
            .findAny()
            .orElse(null);        
    }
    
    // Show ChangeMickNameView
    public void showChangeMickNameView() {
        this.showView(new ChangeNickNameView(this));
    }
    
    // Show ChangePasswordView
    public void showChangePasswordView() {
        this.showView(new ChangePasswordView(this));
    }
    
    // Show CreateRoomView
    public void showCreateRoomView() {
        this.showView(new CreateEditRoomView(this, "CREATE"));
    }
    
    // Show EditRoomView
    public void showEditRoomView(Room room) {
        this.showView(new CreateEditRoomView(this, "EDIT", room));
    }
    
    // Show RoomChatView
    public void showRoomChatView(Room room) {
        this.showView(new RoomChatView(this, room));
    }
    
    // Chuyển đổi lại name Room
    private void convertRoomName() {
        if(currentUser != null)
            this.currentUser.getRoomsJoined().forEach(room -> {
                if(room.getTypeRoom().equals(TypeRoom.Double)) {
                    User friend = room.getMembers().stream().filter(member -> member.getId() != currentUser.getId()).findAny().orElse(null);
                    if(friend != null)
                        room.setName(friend.getNickname());
                }
            });
    }
    
    // Send file
    public void sendFile(Room room, File[] files) {
        List<FileInfo> fileInfos = Arrays.asList(files).stream().map(file -> {
            BufferedInputStream bis = null;
            byte[] fileBytes = new byte[(int) file.length()];
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(fileBytes, 0, fileBytes.length);
            } catch (FileNotFoundException ex) {} catch (IOException ex) {}
            return new FileInfo(file.getName(), fileBytes); 
        }).collect(Collectors.toList());
        this.send(new ServiceResult(new Pair<>(room, fileInfos), RequestType.File_Send));
    }
    
    // Refresh
    private void refresh() {
        // Chuyển đổi lại name của room
        this.convertRoomName();
        // Render lại view hiện tại
        this.currentView.renderView();       
    }
    
    // Lắng nghe server thay đổi và cập nhật lại dữ liệu
    @Override
    public void run() {
        while(true) {
            // Quá trình trao đổi sẽ ở đây
            ServiceResult serviceResult = (ServiceResult) this.receive();
            Status status = serviceResult.getStatus();
            RequestType requestType = serviceResult.getRequestType();   
            String message = serviceResult.getMessage();
            
            switch(requestType) {
                
                case Refresh: {
                    Map<String, Object> data = (HashMap<String, Object>) serviceResult.getData();
                    this.currentUser = (User) data.get("currentUser");
                    this.users = (List<User>) data.get("users");
                    break;
                }
                
                case User_Register: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message);
                    else {
                        User user = (User) serviceResult.getData();
                        this.showLoginView(user);
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case User_Login: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message);
                    else {
                        this.currentUser = (User) serviceResult.getData();
                        this.showHomeView();
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case User_ForgotPassword: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message);
                    else {
                        User user = (User) serviceResult.getData();
                        this.showLoginView(user);
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case User_LogOut: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message);
                    else {
                        this.showLoginView(this.currentUser);
                        currentView.showSuccess(message);
                        this.currentUser = null;
                        this.users = null;
                    }
                    break;
                }
                
                case User_ChangePassword: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message);
                    else {
                        this.currentUser = (User) serviceResult.getData();
                        this.showHomeView();
                        currentView.showSuccess(message);
                    }                    
                    break;
                }
                
                case User_ChangeNickname: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message);  
                    else {
                        this.currentUser = (User) serviceResult.getData();
                        this.showHomeView();
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case Room_Create: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    else {
                        Room room = (Room) serviceResult.getData();
                        // Hiển thị phòng chat
                        this.showRoomChatView(room);
                        // Hiển thị thông báo
                        currentView.showSuccess(message);
                    }
                    
                    break;
                }
                
                case Room_Update: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    else {
                        Room room = (Room) serviceResult.getData();
                        this.showRoomChatView(room);
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case User_ExitRoom: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    else {
                        this.showHomeView();
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case Delete_Room: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    else {
                        this.showHomeView();
                        currentView.showSuccess(message);
                    }
                    break;
                }
                
                case Message_Send: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    break;
                }
                
                case File_Send: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    else if(status == Status.Success)
                        currentView.showSuccess(message);
                    
                    break;
                }
                
                case File_Receive: {
                    if(status == Status.Invalid || status == Status.Error)
                        currentView.showError(message); 
                    else if(status == Status.Success) {
                        int choose = this.currentView.showConfirm(message);
                        if(choose == 0) {
                            String directory = this.currentView.showSelectDirectory("Save folder");
                            AtomicInteger countSuccess = new AtomicInteger(); 
                            if(directory != null) {
                                List<FileInfo> fileInfos = (List<FileInfo>) serviceResult.getData();
                                fileInfos.forEach(fileInfo -> {
                                    File fileSave = new File(directory + "\\" + fileInfo.getFilename());
                                    
                                    // Lưu file
                                    BufferedOutputStream bos = null;
                                    try {
                                        
                                        bos =  new BufferedOutputStream(new FileOutputStream(fileSave));
                                        bos.write(fileInfo.getDataBytes());
                                        bos.flush();
                                        countSuccess.getAndIncrement();
                                    } catch (FileNotFoundException ex) {} catch (IOException ex) {
                                    } finally {
                                        try {
                                            if (bos != null)
                                            bos.close();
                                        } catch (IOException ex) {}
                                    }
                                });
                                
                                this.currentView.showSuccess("Lưu thành công " + countSuccess.get() + " files");
                            }
                        }
                    }
                    break;
                }
            }
            
            this.refresh();
        }
    }    
}