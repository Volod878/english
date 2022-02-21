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

    @Query(value = "SELECT voc.word" +
            " FROM vocabulary_bank.public.vocabulary voc" +
            " LEFT JOIN (SELECT wl.vocabulary_id, wl.answer_is_right FROM vocabulary_bank.public.word_learning wl" +
                " INNER JOIN (SELECT wl.vocabulary_id, MAX(wl.ins_time) ins" +
                " FROM vocabulary_bank.public.word_learning wl" +
                " GROUP BY wl.vocabulary_id) by_ins" +
                " ON wl.vocabulary_id = by_ins.vocabulary_id AND wl.ins_time = by_ins.ins) wl_ins" +
            " ON voc.id = wl_ins.vocabulary_id" +
            " WHERE wl_ins.answer_is_right = false OR wl_ins.vocabulary_id ISNULL",
            nativeQuery = true)
    List<String> findWordsWithoutRightAnswer();

//    @Query(value = "SELECT voc.word" +
//            " FROM vocabulary_bank.public.vocabulary voc" +
//            " LEFT JOIN (SELECT wl.vocabulary_id, wl.answer_is_right FROM word_learning wl" +
//                " INNER JOIN (SELECT wl.vocabulary_id, MAX(wl.ins_time) ins" +
//                " FROM word_learning wl" +
//                " GROUP BY wl.vocabulary_id) by_ins" +
//                " ON wl.vocabulary_id = by_ins.vocabulary_id AND wl.ins_time = by_ins.ins) wl_ins" +
//            " ON voc.id = wl_ins.vocabulary_id" +
//            " WHERE wl_ins.answer_is_right = true",
//            nativeQuery = true)
//    List<String> findWordsWithRightAnswer();
}