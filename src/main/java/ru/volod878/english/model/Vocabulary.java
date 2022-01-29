package ru.volod878.english.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "vocabulary")
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
}