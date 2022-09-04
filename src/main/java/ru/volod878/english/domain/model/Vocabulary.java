package ru.volod878.english.domain.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "word", unique = true, nullable = false)
    private String word;

    @Column(name = "transcription_us", nullable = false)
    private String transcriptionUs;

    @Column(name = "transcription_uk", nullable = false)
    private String transcriptionUk;

    @Column(name = "sound_us_path", nullable = false)
    private String soundUsPath;

    @Column(name = "sound_uk_path", nullable = false)
    private String soundUkPath;

    @Column(name = "translates", nullable = false)
    private String translates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vocabulary that = (Vocabulary) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}