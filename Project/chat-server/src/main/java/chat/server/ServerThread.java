package chat.server;

import chat.common.ServiceResult;
import chat.entities.Message;
import chat.entities.Room;
import chat.entities.User;
import chat.enumeration.RequestType;
import chat.enumeration.Status;
import chat.service.Service;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            this.sendSuccess(data, "Refresh thành công",RequestType.Refresh);
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
                // Quá trình trao đổi sẽ ở đây
                ServiceResult serviceResult = (ServiceResult) this.receive();
                RequestType requestType = serviceResult.getRequestType();

                switch (requestType) {
                    case User_Register: {
                        // Nhận User của người đăng ký
                        User userRegister = (User) serviceResult.getData();

                        // Kiểm tra username đã tồn tại
                        if (service.checkUsernameExist(userRegister.getUsername()))
                            this.sendInvalid("Username đã tồn tại", RequestType.User_Register);

                        // Kiểm tra nickname đã tồn tại
                        else if (service.checkNicknameExist(userRegister.getNickname()))
                            this.sendInvalid("Nickname đã tồn tại", RequestType.User_Register);

                        // Kiểm tra email đã tồn tại
                        else if (service.checkEmailExist(userRegister.getEmail()))
                            this.sendInvalid("Email đã tồn tại", RequestType.User_Register);

                        // Validate thành công
                        else {
                            // Thực hiện đăng ký
                            User newUser = service.saveUser(userRegister);

                            if(newUser == null)
                                this.sendError("Đăng ký không thành công", RequestType.User_Register);
                            else {
                                // Thêm vào phòng chat ALL
                                service.addMemberToRoomChatAll(newUser);

                                // Refresh all cho client;
                                server.refreshAll();

                                // Gửi lại cho user đăng ký
                                this.sendSuccess(newUser, "Đăng ký thành công", RequestType.User_Register);
                            }
                        }

                        break;
                    }

                    case Refresh: {
                        this.refresh();
                        break;
                    }

                    case User_Login: {
                        // Nhận User của người login
                        User userData = (User) serviceResult.getData();

                        User userLogin = service.getUserByUsername(userData.getUsername());

                        // Kiểm tra username có tồn tại hay không
                        if(userLogin == null)
                            this.sendInvalid("Username không tồn tại", RequestType.User_Login);

                        // Kiểm tra password
                        else if(!userLogin.getPassword().equals(userData.getPassword()))
                            this.sendInvalid("Password không đúng", RequestType.User_Login);

                        else {
                            // Kiểm tra user đã đăng nhập ở đâu đó chưa
                            if(server.checkUserLoggedIn(userLogin.getId()))
                                this.sendInvalid("Bạn đang đăng nhập ở 1 site khác", RequestType.User_Login);

                            // Login thành công
                            else {
                                // Lưu id của người dùng đã đăng nhập
                                this.currentUserID = userLogin.getId();

                                // Refresh client này
                                this.refresh();

                                // Trả về currentUser
                                this.sendSuccess(userLogin, "Đăng nhập thành công", RequestType.User_Login);
                            }
                        }

                        break;
                    }

                    case User_ForgotPassword: {
                        // Nhận User của người đã đăng ký
                        User userData = (User) serviceResult.getData();

                        User userForgotPassword = service.getUserByUsername(userData.getUsername());

                        // Kiểm tra username có tồn tại hay không
                        if(userForgotPassword == null)
                            this.sendInvalid("Username không tồn tại", RequestType.User_ForgotPassword);

                        // Kiểm tra email có đúng hay không
                        else if(!userForgotPassword.getEmail().equals(userData.getEmail()))
                            this.sendInvalid("Password không đúng", RequestType.User_ForgotPassword);

                        // Validate thành công
                        else this.sendSuccess(userForgotPassword, "Password của bạn là: " + userForgotPassword.getPassword(), RequestType.User_ForgotPassword);

                        break;
                    }

                    case User_LogOut: {
                        this.currentUserID = 0;
                        this.sendSuccess("Đăng xuất thành công", RequestType.User_LogOut);
                        break;
                    }

                    case User_ChangePassword: {
                        User user = service.getUserByID(currentUserID);

                        if(user == null)
                            this.sendError("Có lỗi xảy ra", RequestType.User_ChangePassword);
                        else {
                            // Nhận data
                            Map<String, Object> data = (Map<String, Object>) serviceResult.getData();

                            // Lấy mật khẩu cũ
                            String oldPassword = (String) data.get("oldPassword");

                            // Kiểm tra mật khẩu cũ
                            if(!user.getPassword().equals(oldPassword))
                                this.sendInvalid("Mật khẩu cũ không đúng", RequestType.User_ChangePassword);
                            else {
                                // Lấy mật khẩu mới
                                String newPassword = (String) data.get("newPassword");

                                // Thực hiện thay đổi mật khẩu
                                user.setPassword(newPassword);

                                // Thực hiện lưu mật khẩu mới
                                User userChange = service.saveUser(user);

                                // Refresh all client;
                                server.refreshAll();

                                // Gửi lại cho client đó currentUser
                                this.sendSuccess(userChange, "Thay đổi mật khẩu thành công", RequestType.User_ChangePassword);
                            }
                        }

                        break;
                    }

                    case User_ChangeNickname: {
                        User user = service.getUserByID(currentUserID);

                        if(user == null)
                            this.sendError("Có lỗi xảy ra", RequestType.User_ChangeNickname);
                        else {
                            // Nhận data
                            Map<String, Object> data = (Map<String, Object>) serviceResult.getData();

                            // Lấy mật khẩu
                            String password = (String) data.get("password");

                            // Kiểm tra mật khẩu
                            if(!user.getPassword().equals(password))
                                this.sendInvalid("Mật khẩu không đúng", RequestType.User_ChangeNickname);
                            else {
                                // Lấy nickname mới
                                String newNickname = (String) data.get("newNickname");

                                // Kiểm tra nickname có trùng không
                                if(!newNickname.equals(user.getNickname()) && service.checkNicknameExist(newNickname))
                                    this.sendInvalid("Nickname đã tồn tại", RequestType.User_ChangeNickname);

                                // Validate thành công
                                else {
                                    // Thực hiện thay đổi nickname
                                    user.setNickname(newNickname);

                                    // Thực hiện lưu nickname mới
                                    User userChange = service.saveUser(user);

                                    // Refresh all client;
                                    server.refreshAll();

                                    // Gửi lại cho client đó currentUser
                                    this.sendSuccess(userChange, "Thay đổi nickname thành công", RequestType.User_ChangeNickname);
                                }
                            }
                        }

                        break;
                    }

                    case Room_Create: {
                        Room roomData = (Room) serviceResult.getData();

                        // Kiểm tra xem phòng này đã được tạo chưa (Các thành viên giống y hệt 1 phòng nào đấy của currentUser)
                        User currentUser = service.getUserByID(currentUserID);
                        if(currentUser == null)
                            this.sendError("Có lỗi xảy ra", RequestType.Room_Create);
                        else {
                            Room newRoom = service.findRoomEqualsMembers(currentUser.getRoomsJoined(), roomData.getMembers());
                            if(newRoom != null)
                                this.sendSuccess(newRoom, "Phòng này đã được tạo trước đó", RequestType.Room_Create);
                            // Phòng này chưa được tạo
                            else {
                                // Thực hiện tạo room
                                Room saveRoom = (Room) service.saveRoom(roomData);

                                // Nếu tạo không thành công
                                if(saveRoom == null)
                                    this.sendError("Tạo phòng thất bại", RequestType.Room_Create);

                                    // Nếu tạo thành công
                                else {
                                    // Gửi lại cho tất cả client khác
                                    server.refreshAll();

                                    // Gửi lại cho client đó room vừa tạo
                                    this.sendSuccess(saveRoom, "Tạo phòng thành công", RequestType.Room_Create);
                                }
                            }
                        }

                        break;
                    }

                    case Room_Update: {
                        Room roomData = (Room) serviceResult.getData();

                        // Lấy lại đối tượng Room cần update
                        Room roomUpdate = service.getRoomByID(roomData.getId());

                        if(roomUpdate == null)
                            this.sendError("Có lỗi xảy ra", RequestType.Room_Update);
                        else {
                            // Thực hiện update Room
                            roomUpdate.setName(roomData.getName());
                            roomUpdate.setMembers(roomData.getMembers());

                            // Thực hiện lưu
                            Room roomSave = service.saveRoom(roomUpdate);
                            if(roomSave == null)
                                this.sendError("Update room không thành công", RequestType.Room_Update);
                            else {
                                // Refresh tất cả mọi người
                                server.refreshAll();

                                // Return roomSace cho clien đã thực hiện update
                                this.sendSuccess(roomSave, "Cập nhật room thành công", RequestType.Room_Update);
                            }
                        }

                        break;
                    }

                    case User_ExitRoom: {
                        Room roomData = (Room) serviceResult.getData();

                        Room exitRoom = service.getRoomByID(roomData.getId());
                        if(exitRoom == null)
                            this.sendError("Có lỗi xảy ra", RequestType.User_ExitRoom);
                        else {
                            // Remove currentUser ra khỏi room
                            exitRoom.getMembers().removeIf(member -> member.getId() == currentUserID);

                            // Nếu không còn thành viên nào thì xóa phòng
                            if(exitRoom.getMembers().size() == 0) {
                                // Thực hiện xóa phòng
                                Room deleteRoom = service.deleteRoom(exitRoom);
                                if(deleteRoom == null) {
                                    // Gửi lại client đó thông báo
                                    this.sendSuccess("Thoát khỏi phòng thành công", RequestType.User_ExitRoom);
                                    // Refresh all
                                    server.refreshAll();
                                }
                                else
                                    this.sendError("Có lỗi xảy ra", RequestType.User_ExitRoom);
                            }

                            // Nếu còn thành viên trong phòng
                            else {
                                // Check xem currentUser có đúng là chủ phòng không, nếu có thì set lại chủ phòng
                                if(currentUserID == exitRoom.getOwner().getId()) {
                                    exitRoom.setOwner(exitRoom.getMembers().get(0));
                                }

                                // Thực hiện lưu vào database
                                service.saveRoom(exitRoom);

                                // Gửi lại client đó thông báo
                                this.sendSuccess("Thoát khỏi phòng thành công", RequestType.User_ExitRoom);

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
                            this.sendError("Có lỗi xảy ra", RequestType.Delete_Room);
                        else {
                            // Thực hiện xóa phòng
                            Room deletedRoom = service.deleteRoom(deleteRoom);
                            if(deletedRoom != null)
                                this.sendError("Có lỗi xảy ra", RequestType.Delete_Room);
                            else {
                                // Gửi lại client đó thông báo
                                this.sendSuccess("Xóa phòng thành công", RequestType.Delete_Room);
                                // Refresh all
                                server.refreshAll();
                            }
                        }
                        break;
                    }

                    case Message_Send: {
                        Message messageSend = (Message) serviceResult.getData();

                        // Thực hiện lưu message
                        Message messageSave = service.saveMessage(messageSend);

                        if(messageSave == null)
                            this.sendError("Gửi tin nhắn không thành công", RequestType.Message_Send);
                        else {
                            server.refreshAll();
                        }

                        break;
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, e);

                // Khi client/server bị lỗi nó sẽ nhảy vào đây

                // Remove serverThread bị lỗi khỏi list và kết thúc thread này
                server.getServerThreads().remove(this);
                System.out.println("Đã đóng kết nối với: " + socket.getInetAddress());

                try {
                    // Đóng kết nối ở server
                    this.socket.close();
                    this.sendObject.close();
                    this.receiveObject.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Thoát khỏi while
                break;
            }
        }
    }
}