package az.code.telegrambotv2.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;


@Entity
@Table(name = "language")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Language {

    @Id
    int id;

    @Column
    String name;

    @OneToMany(mappedBy = "language", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @ToString.Exclude
    @ToString.Exclude
    List<QuestionLocale> textOfQuestion;


}


