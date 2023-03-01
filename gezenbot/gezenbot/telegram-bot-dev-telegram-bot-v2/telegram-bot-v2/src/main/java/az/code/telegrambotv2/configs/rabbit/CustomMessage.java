package az.code.telegrambotv2.configs.rabbit;

import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CustomMessage implements Serializable {

    String chatId;

    String answer;

}

