package chat.service;

import chat.entities.Message;
import chat.entities.Room;
import chat.entities.User;
import chat.enumeration.TypeRoom;
import chat.repository.MessageRepository;
import chat.repository.RoomRepository;
import chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private MessageRepository messageRepo;

    public boolean checkUsernameExist(String username) {
        User user = userRepo.findUsersByUsername(username);
        return user != null;
    }

    public boolean checkNicknameExist(String nickname) {
        User user = userRepo.findUsersByNickname(nickname);
        return user != null;
    }

    public boolean checkEmailExist(String email) {
        User user = userRepo.findUsersByEmail(email);
        return user != null;
    }

    public User saveUser(User user) {
        User newUser = userRepo.save(user);
        if(newUser == null) return null;

        newUser = this.getUserByID(newUser.getId());
        return newUser;
    }

    public Room saveRoom(Room room) {
        Room newRoom = roomRepo.save(room);
        if(newRoom == null) return null;

        newRoom = this.getRoomByID(newRoom.getId());
        return newRoom;
    }

    public Message saveMessage(Message message) {
        return messageRepo.save(message);
    }

    public User convertUserBase(User userFull) {
        if(userFull == null) return null;
        return new User(
            userFull.getId(),
            userFull.getUsername(),
            userFull.getPassword(),
            userFull.getNickname(),
            userFull.getEmail()
        );
    }

    public Message convertMessageBase(Message messageFull) {
        if(messageFull == null) return null;
        return new Message(
            messageFull.getId(),
            messageFull.getContent(),
            messageFull.getCreateDate(),
            this.convertUserBase(messageFull.getUserSend()),
            this.convertRoomBase(messageFull.getRoom())
        );
    }

    public Room convertRoomBase(Room roomFull) {
        if(roomFull == null) return null;
        return new Room(
            roomFull.getId(),
            roomFull.getName(),
            roomFull.getTypeRoom()
        );
    }

    public Room convertRoomFromBase(Room roomFull) {
        if(roomFull == null) return null;
        Room room = this.convertRoomBase(roomFull);
        room.setOwner(this.convertUserBase(roomFull.getOwner()));
        room.setMembers(roomFull.getMembers().stream().map(user -> this.convertUserBase(user)).collect(Collectors.toList()));
        room.setMessages(roomFull.getMessages().stream().map(message -> this.convertMessageBase(message)).collect(Collectors.toList()));
        return room;
    }

    public User convertUserFromBase(User userFull) {
        if(userFull == null) return null;
        User user = this.convertUserBase(userFull);
        user.setRoomsJoined(userFull.getRoomsJoined().stream().map(room -> this.convertRoomBase(room)).collect(Collectors.toList()));
        return user;
    }

    public User convertUserFull(User userFull) {
        if(userFull == null) return null;
        User user = this.convertUserFromBase(userFull);
        user.setRoomsJoined(userFull.getRoomsJoined().stream().map(room -> this.convertRoomFromBase(room)).collect(Collectors.toList()));
        return user;
    }

    public Room convertRoomFull(Room roomFull) {
        if(roomFull == null) return null;
        Room room = this.convertRoomFromBase(roomFull);
        room.setOwner(this.convertUserFromBase(roomFull.getOwner()));
        room.setMembers(roomFull.getMembers().stream().map(user -> this.convertUserFromBase(user)).collect(Collectors.toList()));
        room.setMessages(roomFull.getMessages().stream().map(message -> this.convertMessageBase(message)).collect(Collectors.toList()));
        return room;
    }

    public List<User> getALlUser() {
        return userRepo.findAll().stream().map(user -> this.convertUserFull(user)).collect(Collectors.toList());
    }

    public Room findRoomEqualsMembers(List<Room> rooms, List<User> members) {
        for(Room room: rooms) {
            if(room.getMembers().size() != members.size())
                continue;
            int found = 0;
            for(User memberRoom: room.getMembers()) {
                if(members.stream().noneMatch(member -> member.getId() == memberRoom.getId()))
                    break;
                else found++;
            }
            if(found > 0 && found == members.size()) return room;
        }
        return null;
    }

    public User getUserByID(int id) {
        Optional<User> userOpt = userRepo.findById(id);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            return this.convertUserFull(user);
        }
        return null;
    }

    public User getUserByUsername(String username) {
        User user = userRepo.findUsersByUsername(username);
        if(user == null) return null;
        return this.convertUserFull(user);
    }

    public Room getRoomByID(int id) {
        Optional<Room> optRoom = roomRepo.findById(id);
        if(optRoom.isPresent()) {
            Room room = optRoom.get();
            return this.convertRoomFull(room);
        }
        return null;
    }

    public Room deleteRoom(Room room) {
        roomRepo.delete(room);
        return getRoomByID(room.getId());
    }

    public void addMemberToRoomChatAll(User member) {
        Room room = roomRepo.findRoomByTypeRoom(TypeRoom.All);

        // Nếu không có thì tạo mới
        if(room == null) {
            roomRepo.save(new Room("Chat All", new ArrayList<>(Arrays.asList(member)), TypeRoom.All));
            return;
        }

        // Nếu có thì add vào
        room.getMembers().add(member);

        // Thực hiện lưu
        roomRepo.save(room);
    }
}