package ru.volod878.english.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}