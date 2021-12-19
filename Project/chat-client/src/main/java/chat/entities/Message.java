package chat.entities;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 3L;

    private int id;

    private String content;

    private Date createDate;

    private User userSend; // Ai là người gửi

    private Room room; // Message của phòng nào

    public Message(String content, User userSend, Room room) {
        this.content = content;
        this.userSend = userSend;
        this.room = room;
    }
}