package ru.volod878.english.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.volod878.english.model.Vocabulary;

@Repository
@Transactional(readOnly = true)
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {
}