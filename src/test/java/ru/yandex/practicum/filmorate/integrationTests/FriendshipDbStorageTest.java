package ru.yandex.practicum.filmorate.integrationTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDbStorageTest {
    final FriendshipDbStorage friendshipDbStorage;
    final UserDbStorage userDbStorage;
    User user1;
    User user2;
    User user3;

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
    }

    @AfterEach
    void end() {
        userDbStorage.deleteAllUser();
    }

    @Test
    void testFindCommonFriends() {
        User testUser1 = userDbStorage.addUser(user1);
        User testUser2 = userDbStorage.addUser(user2);
        User testUser3 = userDbStorage.addUser(user3);
        friendshipDbStorage.addFriend(1L, 3L);
        friendshipDbStorage.addFriend(2L, 3L);
        List<Long> common12 = friendshipDbStorage.findCommonFriends(1L, 2L);
        assertThat(common12.get(0)).isEqualTo(3);

    }

    @Test
    void testAddFriendAndFindAllFriends() {
        User testUser1 = userDbStorage.addUser(user1);
        User testUser2 = userDbStorage.addUser(user2);
        friendshipDbStorage.addFriend(1L, 2L);
        List<Long> friend12 = friendshipDbStorage.findAllFriends(1L);
        assertThat(friend12.get(0)).isEqualTo(2);
    }

    @Test
    void testDeleteFriend() {
        friendshipDbStorage.deleteFriend(1L, 3L);
        List<Long> friendList = friendshipDbStorage.findAllFriends(1L);
        assertThat(!friendList.contains(2)).isEqualTo(true);
    }
}
