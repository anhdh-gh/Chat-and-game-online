package chat.repository;

import chat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUsersByUsername(String username);
    User findUsersByEmail(String email);
    User findUsersByNickname(String nickname);
}