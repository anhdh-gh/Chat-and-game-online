package chat.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import chat.enumeration.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "Name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_room",
        joinColumns = @JoinColumn(name = "RoomID"),
        inverseJoinColumns = @JoinColumn(name = "UserID")
    )
    private List<User> members; // Các thành viên trong phòng

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User owner; // Ai là chủ phòng

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Message> messages; // Các tin nhắn trong phòng

    @Column(name = "TypeRoom")
    @Enumerated(EnumType.STRING)
    private TypeRoom typeRoom;

    public Room(int id, String name, TypeRoom typeRoom) {
        this.id = id;
        this.name = name;
        this.typeRoom = typeRoom;
    }

    public Room(String name, List<User> members, TypeRoom typeRoom) {
        this.name = name;
        this.members = members;
        this.typeRoom = typeRoom;
    }
}