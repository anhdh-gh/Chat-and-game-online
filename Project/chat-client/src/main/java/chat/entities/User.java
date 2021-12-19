package chat.entities;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    @ToString.Exclude // Không sử dụng trường này trong toString()
    @EqualsAndHashCode.Exclude // Không sử dụng trường này trong Equals và HashCode
    private List<Room> roomsJoined; // Các phòng đã tạo và tham gia

    public User(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }
    
    public User(User user) {
        this(
            user.getId(), 
            user.getUsername(), 
            user.getPassword(), 
            user.getNickname(), 
            user.getEmail(), 
            user.getRoomsJoined()
        );
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Object[] tObject() {
        return new Object[] {
            this.id, 
            this.nickname
        };
    }
}