package az.code.telegrambotv2.controllers;


import az.code.telegrambotv2.models.redis.Session;
import az.code.telegrambotv2.repositories.redis.SessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class SessionController {

    private final SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable Long id){
        return ResponseEntity.ok(sessionRepository.find(id));
    }


    @PostMapping()
    public ResponseEntity<Session> save(@RequestBody Session session){
        return ResponseEntity.ok(sessionRepository.save(session));
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        sessionRepository.delete(id);
    }

}
