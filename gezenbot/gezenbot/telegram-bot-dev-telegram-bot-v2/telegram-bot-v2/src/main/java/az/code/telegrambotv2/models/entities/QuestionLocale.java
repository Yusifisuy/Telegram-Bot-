package az.code.telegrambotv2.models.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "question_locale")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionLocale {
    @Id
    long id;

    @Column(name = "question")
    String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    @ToString.Exclude
    Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
            @ToString.Exclude
    Question question;

    @OneToMany(mappedBy = "questionLocale", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Option>options;
}

