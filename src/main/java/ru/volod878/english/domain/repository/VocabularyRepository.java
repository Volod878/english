package ru.volod878.english.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.volod878.english.domain.model.Vocabulary;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {
    Vocabulary findByWord(String word);

    List<Vocabulary> findByWordIn(Set<String> words);

    @Query(value = "SELECT voc.word " +
            "FROM vocabulary_bank.public.vocabulary voc",
            nativeQuery = true)
    List<String> findAllWords();
}