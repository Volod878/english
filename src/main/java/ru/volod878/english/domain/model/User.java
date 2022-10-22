package ru.volod878.english.domain.model;

import lombok.*;

import javax.persistence.*;

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

    public User(org.telegram.telegrambots.meta.api.objects.User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.telegramUserId = user.getId();
    }
}
