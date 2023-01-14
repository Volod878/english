package ru.volod878.english.domain.model;

import lombok.*;
import ru.volod878.english.bot.command.CommandName;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "telegram_user_id", unique = true)
    private Long telegramUserId;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Enumerated(value = STRING)
    @Column(name = "active_command")
    private CommandName activeCommand;

    public User(org.telegram.telegrambots.meta.api.objects.User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.telegramUserId = user.getId();
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
