package ru.yandex.practicum.filmorate.integrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    final UserDbStorage userDbStorage;
    User user1;
    User user2;
    User user3;

    User userNoName;

    @BeforeEach
    void start() {
        user1 = User.builder()
                .name("user1")
                .email("user1@test.com")
                .login("logUser1")
                .birthday(LocalDate.of(2001, 01, 01))
                .build();

        user2 = User.builder()
                .name("user2")
                .email("user2@test.com")
                .login("logUser2")
                .birthday(LocalDate.of(2002, 02, 02))
                .build();

        user3 = User.builder()
                .name("user3")
                .email("user3@test.com")
                .login("logUser3")
                .birthday(LocalDate.of(2003, 03, 03))
                .build();

        userNoName = User.builder()
                .email("user2@test.com")
                .login("logUser2")
                .birthday(LocalDate.of(2002, 02, 02))
                .build();
    }

    @Test
    void addUser() {
        User testUser = userDbStorage.addUser(user1);
        assertThat(testUser.getId()).isNotNull();
        assertThat(testUser.getName()).isEqualTo(user1.getName());
        assertThat(testUser.getLogin()).isEqualTo(user1.getLogin());
        assertThat(testUser.getEmail()).isEqualTo(user1.getEmail());
        assertThat(testUser.getBirthday()).isEqualTo(user1.getBirthday());
    }

    @Test
    void testAddUserIfNoName() {
        User testUser = userDbStorage.addUser(userNoName);
        assertThat(testUser.getId()).isNotNull();
        assertThat(testUser.getName()).isEqualTo(userNoName.getLogin());
        assertThat(testUser.getLogin()).isEqualTo(userNoName.getLogin());
        assertThat(testUser.getEmail()).isEqualTo(userNoName.getEmail());
        assertThat(testUser.getBirthday()).isEqualTo(userNoName.getBirthday());
    }

    @Test
    void testUpdateUser() {
        user2.setId(1L);
        User testUser = userDbStorage.updateUser(user2);
        assertThat(testUser.getName()).isEqualTo(user2.getName());
        assertThat(testUser.getLogin()).isEqualTo(user2.getLogin());
        assertThat(testUser.getEmail()).isEqualTo(user2.getEmail());
        assertThat(testUser.getBirthday()).isEqualTo(user2.getBirthday());
    }

    @Test
    void testFindUser() {
        user1.setId(1L);
        User testUser = userDbStorage.findUser(1L);
        assertThat(testUser.getName()).isEqualTo(user2.getName());
        assertThat(testUser.getLogin()).isEqualTo(user2.getLogin());
        assertThat(testUser.getEmail()).isEqualTo(user2.getEmail());
        assertThat(testUser.getBirthday()).isEqualTo(user2.getBirthday());
    }

    @Test
    void testFindAllUser() {
        userDbStorage.addUser(user3);
        List<User> allUsers = userDbStorage.findAllUser();
        assertThat(allUsers.size()).isEqualTo(2);
    }
}
