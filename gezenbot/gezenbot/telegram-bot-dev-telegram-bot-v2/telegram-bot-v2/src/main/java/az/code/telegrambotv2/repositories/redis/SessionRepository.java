//package az.code.telegrambotv2.repositories.redis;
//
//import az.code.telegrambotv2.models.redis.Session;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
////@Repository
//@Component
//public class SessionRepository {
//    private final String hashReference= "redis";
//
////    private final RedisTemplate redisTemplate;
//
//    private RedisTemplate template;
//
//    public SessionRepository(@Qualifier("template") RedisTemplate template){
//        this.template = template;
//    }
//
//
//    public Session save(Session session){
//        template.opsForHash().put(hashReference, session.getChatId(), session);
//        return session;
//    }
//
//    public Session find(long chatId){
//        return (Session) template.opsForHash().get(hashReference, chatId);
//    }
//
//    public String delete(long chatId){
//        template.opsForHash().delete(hashReference, chatId);
//        return "Session removed!";
//    }
//}

package az.code.telegrambotv2.repositories.redis;

import az.code.telegrambotv2.models.redis.Session;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

//@Repository
@Component

public class SessionRepository{
    private final String hashReference= "redis";

    private final RedisTemplate template;

//    @Resource(name = "template")
//    private HashOperations<String, Long, Session> hashOperations;

    public SessionRepository(@Qualifier("redis") RedisTemplate template){
        this.template = template;
    }

    public Session save(Session session) {
        System.out.println("TEST IN REDIS " + session.getUserLanguage());
        template.opsForHash().put(hashReference, session.getChatId(), session);
//        hashOperations.putIfAbsent(hashReference,red.getChatId(),red);
        return session;
    }

    public Session find(long clientId){
        return (Session) template.opsForHash().get(hashReference,clientId);
    }

    public String delete(long clientId){
        template.opsForHash().delete(hashReference,clientId);
        return "Session removed !!";
    }



    // Nezrinin versiyasi
//
//    public Session save(Session session) {
//
//        hashOperations.putIfAbsent(hashReference,session.getChatId(),session);
//        return session;
//    }
//
//    public Session getRedis(Long chatId) {
//        return  hashOperations.get(hashReference,chatId);
//    }
//
//    public Session saveRedisWithChatId(Session red,Long id) {
////        red.get
//        red.setChatId(id);
//        hashOperations.put(hashReference,red.getChatId(),red);
//        return red;
//    }
//
//    public Session find(long chatId){
//        return hashOperations.get(hashReference, chatId);
////        return (Session) template.opsForHash().get(hashReference, chatId);
//    }
//
//    public void updateRedis(Session red) {
//        hashOperations.put(hashReference, red.getChatId(), red);
//    }
//
//    public Map<Long, Session> getAllRedis() {//Long,Answer
//        return hashOperations.entries(hashReference);
//    }
//
//    public void delete(Long id) {
//        hashOperations.delete(hashReference,id);
//    }
//
//
//
//    public void saveAllRedis(Map<Long, Session> map) {
//        hashOperations.putAll(hashReference,map);
//    }


}