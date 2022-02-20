package ru.volod878.english.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.volod878.english.domain.model.WordLearning;

@Repository
@Transactional(readOnly = true)
public interface WordLearningRepository extends JpaRepository<WordLearning, Integer> {
}