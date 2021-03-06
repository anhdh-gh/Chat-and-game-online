package chat.server;

import chat.common.ServiceResult;
import chat.entities.FileInfo;
import chat.entities.Message;
import chat.entities.Room;
import chat.entities.User;
import chat.enumeration.RequestType;
import chat.enumeration.Status;
import chat.service.Service;
import javafx.util.Pair;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class ServerThread extends Thread {
    private final Socket socket;
    private final Server server;
    private final ObjectOutputStream sendObject;
    private final ObjectInputStream receiveObject;

    private int currentUserID;

    private Service service;


    public ServerThread(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.sendObject = new ObjectOutputStream(socket.getOutputStream());
        this.receiveObject = new ObjectInputStream(socket.getInputStream());

        this.service = server.getService();
    }

    private void send(Object object) throws IOException {
        sendObject.writeObject(object);
        sendObject.flush();
    }

    private Object receive() throws IOException, ClassNotFoundException {
        return receiveObject.readObject();
    }

    private void sendSuccess(Object data, String message, RequestType requestType) throws IOException {
        this.send(new ServiceResult(data, Status.Success, message, requestType));
    }

    private void sendSuccess(String message, RequestType requestType) throws IOException {
        this.send(new ServiceResult(Status.Success, message, requestType));
    }

    private void sendInvalid(String message, RequestType requestType) throws IOException {
        this.send(new ServiceResult(Status.Invalid, message, requestType));
    }

    private void sendError(String message, RequestType requestType) throws IOException {
        this.send(new ServiceResult(Status.Error, message, requestType));
    }

    public void refresh() throws IOException {
        if(currentUserID > 0) {
            Map<String, Object> data = new HashMap<>();
            User currentUser = service.getUserByID(currentUserID);
            List<User> users = service.getALlUser();
            data.put("currentUser", currentUser);
            data.put("users", users);
            this.sendSuccess(data, "Refresh th??nh c??ng",RequestType.Refresh);
        }
    }

    @Override
    public String toString() {
        return "ServerThread{" +
                "socket=" + socket.getInetAddress() +
                ", currentUserID=" + currentUserID +
                '}';
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Qu?? tr??nh trao ?????i s??? ??? ????y
                ServiceResult serviceResult = (ServiceResult) this.receive();
                RequestType requestType = serviceResult.getRequestType();

                switch (requestType) {
                    case User_Register: {
                        // Nh???n User c???a ng?????i ????ng k??
                        User userRegister = (User) serviceResult.getData();

                        // Ki???m tra username ???? t???n t???i
                        if (service.checkUsernameExist(userRegister.getUsername()))
                            this.sendInvalid("Username ???? t???n t???i", RequestType.User_Register);

                        // Ki???m tra nickname ???? t???n t???i
                        else if (service.checkNicknameExist(userRegister.getNickname()))
                            this.sendInvalid("Nickname ???? t???n t???i", RequestType.User_Register);

                        // Ki???m tra email ???? t???n t???i
                        else if (service.checkEmailExist(userRegister.getEmail()))
                            this.sendInvalid("Email ???? t???n t???i", RequestType.User_Register);

                        // Validate th??nh c??ng
                        else {
                            // Th???c hi???n ????ng k??
                            User newUser = service.saveUser(userRegister);

                            if(newUser == null)
                                this.sendError("????ng k?? kh??ng th??nh c??ng", RequestType.User_Register);
                            else {
                                // Th??m v??o ph??ng chat ALL
                                service.addMemberToRoomChatAll(newUser);

                                // Refresh all cho client;
                                server.refreshAll();

                                // G???i l???i cho user ????ng k??
                                this.sendSuccess(newUser, "????ng k?? th??nh c??ng", RequestType.User_Register);
                            }
                        }

                        break;
                    }

                    case Refresh: {
                        this.refresh();
                        break;
                    }

                    case User_Login: {
                        // Nh???n User c???a ng?????i login
                        User userData = (User) serviceResult.getData();

                        User userLogin = service.getUserByUsername(userData.getUsername());

                        // Ki???m tra username c?? t???n t???i hay kh??ng
                        if(userLogin == null)
                            this.sendInvalid("Username kh??ng t???n t???i", RequestType.User_Login);

                        // Ki???m tra password
                        else if(!userLogin.getPassword().equals(userData.getPassword()))
                            this.sendInvalid("Password kh??ng ????ng", RequestType.User_Login);

                        else {
                            // Ki???m tra user ???? ????ng nh???p ??? ????u ???? ch??a
                            if(server.checkUserLoggedIn(userLogin.getId()))
                                this.sendInvalid("B???n ??ang ????ng nh???p ??? 1 site kh??c", RequestType.User_Login);

                            // Login th??nh c??ng
                            else {
                                // L??u id c???a ng?????i d??ng ???? ????ng nh???p
                                this.currentUserID = userLogin.getId();

                                // Refresh client n??y
                                this.refresh();

                                // Tr??? v??? currentUser
                                this.sendSuccess(userLogin, "????ng nh???p th??nh c??ng", RequestType.User_Login);
                            }
                        }

                        break;
                    }

                    case User_ForgotPassword: {
                        // Nh???n User c???a ng?????i ???? ????ng k??
                        User userData = (User) serviceResult.getData();

                        User userForgotPassword = service.getUserByUsername(userData.getUsername());

                        // Ki???m tra username c?? t???n t???i hay kh??ng
                        if(userForgotPassword == null)
                            this.sendInvalid("Username kh??ng t???n t???i", RequestType.User_ForgotPassword);

                        // Ki???m tra email c?? ????ng hay kh??ng
                        else if(!userForgotPassword.getEmail().equals(userData.getEmail()))
                            this.sendInvalid("Email kh??ng ????ng", RequestType.User_ForgotPassword);

                        // Validate th??nh c??ng
                        else this.sendSuccess(userForgotPassword, "Password c???a b???n l??: " + userForgotPassword.getPassword(), RequestType.User_ForgotPassword);

                        break;
                    }

                    case User_LogOut: {
                        this.currentUserID = 0;
                        this.sendSuccess("????ng xu???t th??nh c??ng", RequestType.User_LogOut);
                        break;
                    }

                    case User_ChangePassword: {
                        User user = service.getUserByID(currentUserID);

                        if(user == null)
                            this.sendError("C?? l???i x???y ra", RequestType.User_ChangePassword);
                        else {
                            // Nh???n data
                            Map<String, Object> data = (Map<String, Object>) serviceResult.getData();

                            // L???y m???t kh???u c??
                            String oldPassword = (String) data.get("oldPassword");

                            // Ki???m tra m???t kh???u c??
                            if(!user.getPassword().equals(oldPassword))
                                this.sendInvalid("M???t kh???u c?? kh??ng ????ng", RequestType.User_ChangePassword);
                            else {
                                // L???y m???t kh???u m???i
                                String newPassword = (String) data.get("newPassword");

                                // Th???c hi???n thay ?????i m???t kh???u
                                user.setPassword(newPassword);

                                // Th???c hi???n l??u m???t kh???u m???i
                                User userChange = service.saveUser(user);

                                // Refresh all client;
                                server.refreshAll();

                                // G???i l???i cho client ???? currentUser
                                this.sendSuccess(userChange, "Thay ?????i m???t kh???u th??nh c??ng", RequestType.User_ChangePassword);
                            }
                        }

                        break;
                    }

                    case User_ChangeNickname: {
                        User user = service.getUserByID(currentUserID);

                        if(user == null)
                            this.sendError("C?? l???i x???y ra", RequestType.User_ChangeNickname);
                        else {
                            // Nh???n data
                            Map<String, Object> data = (Map<String, Object>) serviceResult.getData();

                            // L???y m???t kh???u
                            String password = (String) data.get("password");

                            // Ki???m tra m???t kh???u
                            if(!user.getPassword().equals(password))
                                this.sendInvalid("M???t kh???u kh??ng ????ng", RequestType.User_ChangeNickname);
                            else {
                                // L???y nickname m???i
                                String newNickname = (String) data.get("newNickname");

                                // Ki???m tra nickname c?? tr??ng kh??ng
                                if(!newNickname.equals(user.getNickname()) && service.checkNicknameExist(newNickname))
                                    this.sendInvalid("Nickname ???? t???n t???i", RequestType.User_ChangeNickname);

                                // Validate th??nh c??ng
                                else {
                                    // Th???c hi???n thay ?????i nickname
                                    user.setNickname(newNickname);

                                    // Th???c hi???n l??u nickname m???i
                                    User userChange = service.saveUser(user);

                                    // Refresh all client;
                                    server.refreshAll();

                                    // G???i l???i cho client ???? currentUser
                                    this.sendSuccess(userChange, "Thay ?????i nickname th??nh c??ng", RequestType.User_ChangeNickname);
                                }
                            }
                        }

                        break;
                    }

                    case Room_Create: {
                        Room roomData = (Room) serviceResult.getData();

                        // Ki???m tra xem ph??ng n??y ???? ???????c t???o ch??a (C??c th??nh vi??n gi???ng y h???t 1 ph??ng n??o ?????y c???a currentUser)
                        User currentUser = service.getUserByID(currentUserID);
                        if(currentUser == null)
                            this.sendError("C?? l???i x???y ra", RequestType.Room_Create);
                        else {
                            Room newRoom = service.findRoomEqualsMembers(currentUser.getRoomsJoined(), roomData.getMembers());
                            if(newRoom != null)
                                this.sendSuccess(newRoom, "Ph??ng n??y ???? ???????c t???o tr?????c ????", RequestType.Room_Create);
                            // Ph??ng n??y ch??a ???????c t???o
                            else {
                                // Th???c hi???n t???o room
                                Room saveRoom = (Room) service.saveRoom(roomData);

                                // N???u t???o kh??ng th??nh c??ng
                                if(saveRoom == null)
                                    this.sendError("T???o ph??ng th???t b???i", RequestType.Room_Create);

                                    // N???u t???o th??nh c??ng
                                else {
                                    // G???i l???i cho t???t c??? client kh??c
                                    server.refreshAll();

                                    // G???i l???i cho client ???? room v???a t???o
                                    this.sendSuccess(saveRoom, "T???o ph??ng th??nh c??ng", RequestType.Room_Create);
                                }
                            }
                        }

                        break;
                    }

                    case Room_Update: {
                        Room roomData = (Room) serviceResult.getData();

                        // L???y l???i ?????i t?????ng Room c???n update
                        Room roomUpdate = service.getRoomByID(roomData.getId());

                        if(roomUpdate == null)
                            this.sendError("C?? l???i x???y ra", RequestType.Room_Update);
                        else {
                            // Th???c hi???n update Room
                            roomUpdate.setName(roomData.getName());
                            roomUpdate.setMembers(roomData.getMembers());

                            // Th???c hi???n l??u
                            Room roomSave = service.saveRoom(roomUpdate);
                            if(roomSave == null)
                                this.sendError("Update room kh??ng th??nh c??ng", RequestType.Room_Update);
                            else {
                                // Refresh t???t c??? m???i ng?????i
                                server.refreshAll();

                                // Return roomSace cho clien ???? th???c hi???n update
                                this.sendSuccess(roomSave, "C???p nh???t room th??nh c??ng", RequestType.Room_Update);
                            }
                        }

                        break;
                    }

                    case User_ExitRoom: {
                        Room roomData = (Room) serviceResult.getData();

                        Room exitRoom = service.getRoomByID(roomData.getId());
                        if(exitRoom == null)
                            this.sendError("C?? l???i x???y ra", RequestType.User_ExitRoom);
                        else {
                            // Remove currentUser ra kh???i room
                            exitRoom.getMembers().removeIf(member -> member.getId() == currentUserID);

                            // N???u kh??ng c??n th??nh vi??n n??o th?? x??a ph??ng
                            if(exitRoom.getMembers().size() == 0) {
                                // Th???c hi???n x??a ph??ng
                                Room deleteRoom = service.deleteRoom(exitRoom);
                                if(deleteRoom == null) {
                                    // G???i l???i client ???? th??ng b??o
                                    this.sendSuccess("Tho??t kh???i ph??ng th??nh c??ng", RequestType.User_ExitRoom);
                                    // Refresh all
                                    server.refreshAll();
                                }
                                else
                                    this.sendError("C?? l???i x???y ra", RequestType.User_ExitRoom);
                            }

                            // N???u c??n th??nh vi??n trong ph??ng
                            else {
                                // Check xem currentUser c?? ????ng l?? ch??? ph??ng kh??ng, n???u c?? th?? set l???i ch??? ph??ng
                                if(currentUserID == exitRoom.getOwner().getId()) {
                                    exitRoom.setOwner(exitRoom.getMembers().get(0));
                                }

                                // Th???c hi???n l??u v??o database
                                service.saveRoom(exitRoom);

                                // G???i l???i client ???? th??ng b??o
                                this.sendSuccess("Tho??t kh???i ph??ng th??nh c??ng", RequestType.User_ExitRoom);

                                // Refresh all
                                server.refreshAll();
                            }
                        }

                        break;
                    }

                    case Delete_Room: {
                        Room roomData = (Room) serviceResult.getData();

                        Room deleteRoom = service.getRoomByID(roomData.getId());
                        if(deleteRoom == null)
                            this.sendError("C?? l???i x???y ra", RequestType.Delete_Room);
                        else {
                            // Th???c hi???n x??a ph??ng
                            Room deletedRoom = service.deleteRoom(deleteRoom);
                            if(deletedRoom != null)
                                this.sendError("C?? l???i x???y ra", RequestType.Delete_Room);
                            else {
                                // G???i l???i client ???? th??ng b??o
                                this.sendSuccess("X??a ph??ng th??nh c??ng", RequestType.Delete_Room);
                                // Refresh all
                                server.refreshAll();
                            }
                        }
                        break;
                    }

                    case Message_Send: {
                        Message messageSend = (Message) serviceResult.getData();

                        // Th???c hi???n l??u message
                        Message messageSave = service.saveMessage(messageSend);

                        if(messageSave == null)
                            this.sendError("G???i tin nh???n kh??ng th??nh c??ng", RequestType.Message_Send);
                        else {
                            server.refreshAll();
                        }

                        break;
                    }

                    case File_Send: {
                        Pair<Room, List<FileInfo>> data = (Pair<Room, List<FileInfo>>) serviceResult.getData();

                        // L???y ra t???t c??? nh???ng ng?????i trong ph??ng theo room v?? l???c ra nh???ng ng?????i ??ang online
                        Room room = data.getKey();
                        List<User> members = this.service.getRoomByID(room.getId()).getMembers();

                        // G???i file
                        List<FileInfo> fileInfos = data.getValue();
                        User user = this.service.getUserByID(this.currentUserID);

                        AtomicInteger countSuccess = new AtomicInteger();
                        this.server
                            .getServerThreads()
                            .stream()
                            .filter(serverThread -> members.stream().anyMatch(member -> member.getId() == serverThread.getCurrentUserID() && member.getId() != this.currentUserID))
                            .forEach(serverThread -> {
                                try {
                                    serverThread.sendSuccess(fileInfos, user.getNickname() + " ???? g???i " + fileInfos.size() + " files cho b???n, b???n c?? mu???n l??u kh??ng?", RequestType.File_Receive);
                                    countSuccess.getAndIncrement();
                                } catch (IOException e) {}
                            });

                        this.sendSuccess("???? g???i th??nh c??ng cho " + countSuccess.get() + " th??nh vi??n", RequestType.File_Send);
                        break;
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, e);

                // Khi client/server b??? l???i n?? s??? nh???y v??o ????y

                // Remove serverThread b??? l???i kh???i list v?? k???t th??c thread n??y
                server.getServerThreads().remove(this);
                System.out.println("???? ????ng k???t n???i v???i: " + socket.getInetAddress());

                try {
                    // ????ng k???t n???i ??? server
                    this.socket.close();
                    this.sendObject.close();
                    this.receiveObject.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Tho??t kh???i while
                break;
            }
        }
    }
}