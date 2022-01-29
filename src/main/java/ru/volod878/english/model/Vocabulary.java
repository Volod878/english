package ru.volod878.english.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "vocabulary")
@NoArgsConstructor
@AllArgsConstructor
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "transcription_us", nullable = false)
    private String transcriptionUs;

    @Column(name = "transcription_uk", nullable = false)
    private String transcriptionUk;

    @Column(name = "sound_us", nullable = false)
    private String soundUs;

    @Column(name = "sound_uk", nullable = false)
    private String soundUk;

    @Column(name = "translate", nullable = false)
    private List<String> translate;
}