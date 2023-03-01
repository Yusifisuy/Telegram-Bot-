package az.code.telegrambotv2.repositories;

import az.code.telegrambotv2.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
