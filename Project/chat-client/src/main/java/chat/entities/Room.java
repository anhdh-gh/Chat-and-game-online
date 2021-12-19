package chat.entities;

import chat.enumeration.TypeRoom;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {

    private static final long serialVersionUID = 2L;

    private int id;

    private String name;

    private List<User> members; // Các thành viên trong phòng

    private User owner; // Ai là chủ phòng

    private List<Message> messages; // Các tin nhắn trong phòng
    
    private TypeRoom typeRoom;
    
    public Object[] toObject() {
        return new Object[] {id, name};   
    }

    public Room(List<User> members, User owner) {
        this.members = members;
        this.owner = owner;
    }

    public Room(Room room) {
        this(room.getId(), room.getName(), room.getMembers(), room.getOwner(), room.getMessages(), room.getTypeRoom());
    }
}