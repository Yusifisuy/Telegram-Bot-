package az.code.telegrambotv2.repositories;

import az.code.telegrambotv2.models.entities.SessionsHistory;
import az.code.telegrambotv2.models.redis.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface SessionHistoryRepository extends JpaRepository<SessionsHistory,Long> {


}
