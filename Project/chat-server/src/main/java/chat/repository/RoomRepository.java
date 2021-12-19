package chat.repository;

import chat.entities.Room;
import chat.enumeration.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findRoomByTypeRoom(TypeRoom typeRoom);
}