package ru.volod878.english.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.volod878.english.domain.model.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByTelegramUserId(Long telegramUserId);

    User findByTelegramUserId(Long telegramUserId);
}