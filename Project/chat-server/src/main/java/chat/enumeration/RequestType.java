package chat.enumeration;

import java.io.Serializable;

public enum RequestType implements Serializable {

    // Client gửi lên User_Register, cùng với newUser cần đăng ký
    // Server nhận User và return (User, User_Register) vừa mới đăng ký cho tất cả mọi người.
    // Client nhận dc (User, User_Register) và hiển thị thông báo
    User_Register,

    // Sẽ cập nhật lại currentUser và users (all user) ở client
    // Server sẽ gửi lại currentUser và users (all user)
    Refresh,

    // Client gửi User(username, password) lên cho server
    // Server:
    // - Kiểm tra User(username, password) đúng không?
    // - Nếu sai thì trả về lỗi.
    // - Nếu đúng:
    //   + Server lưu currentUserID
    //   + Server trả về cho client đó tất cả người dùng (User_GetAll)
    //   + Server trả về currentUser
    // Client hiển thị kết quả tương ứng
    User_Login,

    // Client gửi User (Username, email) cho Server
    // Server kiểm tra User (Username, email) có đúng không?
    // - Nếu sau: Trả về lỗi
    // - Nếu đúng: Gửi lại password cho client
    User_ForgotPassword,

    // Client gửi yêu cầu đăng xuất lên server
    // Server xóa currentUserID, và thông báo là đăng xuất thành công
    // Client hiển thị thông báo thành công
    User_LogOut,

    // Client gửi yêu cầu thay đổi mật khẩu lên server
    // Server thực hiện thay dổi mật khẩu:
    // - Nếu không thành công thì trả về lỗi
    // - Nếu thành công thì trả về currentUser
    // Client thực hiện cập nhật currentUser và hiển thị thông báo
    User_ChangePassword,

    // Client gửi yêu cầu thay đổi nickname lên server
    // Server thực hiện thay đổi nickname
    // - Nếu không thành công thì trả về lỗi
    // - Nếu thành công thì trả về currentUser
    // Client thực hiện cập nhật currentUser và hiển thị thông báo
    User_ChangeNickname,

    // Client gửi yêu cầu tạo phòng lên server
    // Server tạo phòng và trả về kết quả:
    // - Nếu lỗi, trả về kết quả lỗi
    // - Nếu không thì tạo và trả về room vừa tạo
    // Client hiển thị thông báo và nếu thành công thì vào phòng multiple chat luôn
    Room_Create,

    // Client (là chủ phòng) gửi lên serer đối tượng room cần update
    // Server update và gửi thông báo về cho client
    // Client hiển thị thông báo và cập nhật giao diện
    Room_Update,

    // Client (là thành viên của room) gửi đối tượng room cần exit lên server
    // Server thực hiện xóa user đó ra khỏi room và trả về kết quả
    // Client hiển thị thông báo và quay về trang home
    User_ExitRoom,

    // Client (là chủ phòng với phòng Group hoặc là thành viên với phòng Double) gửi yêu cầu xóa phòng
    // Server thực hiện xóa phòng và chả về kết quả
    // Client hiển thị kết quả
    Delete_Room,

    // Client gửi 1 đối tượng Message lên
    // Server thực hiện lưu message và gửi lại hết cho client trong phòng
    // CLient thực hiện render lại list message
    Message_Send,
}