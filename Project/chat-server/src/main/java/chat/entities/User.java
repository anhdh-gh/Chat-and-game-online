package chat.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Nickname")
    private String nickname;

    @Column(name = "Email")
    private String email;

    @ToString.Exclude // Không sử dụng trường này trong toString()
    @EqualsAndHashCode.Exclude // Không sử dụng trường này trong Equals và HashCode
    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
    private List<Room> roomsJoined; // Các phòng đã tạo và tham gia

    public User(int id, String username, String password, String nickname, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }
}