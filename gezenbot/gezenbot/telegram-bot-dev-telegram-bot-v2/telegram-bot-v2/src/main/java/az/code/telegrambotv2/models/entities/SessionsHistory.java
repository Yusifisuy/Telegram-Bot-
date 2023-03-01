package az.code.telegrambotv2.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

import java.util.UUID;

@Entity
//@Table(name = "session")
@Table(name = "session_history")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionsHistory {
    @Id
    private UUID id;

    @JsonProperty("answers")
    private String answers;

    @Column(name = "create_time")
    private Timestamp timestamp;

@Column(name = "language_id")
private String languageId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
