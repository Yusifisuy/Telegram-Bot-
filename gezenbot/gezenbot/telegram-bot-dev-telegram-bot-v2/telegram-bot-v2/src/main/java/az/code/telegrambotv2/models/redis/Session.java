package az.code.telegrambotv2.models.redis;

import az.code.telegrambotv2.JpaConverterJson;
import az.code.telegrambotv2.models.entities.Question;
import jakarta.persistence.Convert;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session implements Serializable {
    @Serial
    private static final long serialVersionUID = 4838248031641137968L;

    String uuid;
    Long chatId;
//    @Convert(converter = JpaConverterJson.class)
    Map<String, String> answers;
//    List<String> answers;
    //TODO long id
//    Question currentQuestion;
    long currentQuestionId;

    String userLanguage;
    LocalDate date;
}
