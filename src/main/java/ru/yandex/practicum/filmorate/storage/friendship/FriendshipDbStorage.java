package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FriendshipDbStorage implements FriendshipStorage {
    private JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;

    private final Logger log = LoggerFactory.getLogger(FriendshipDbStorage.class);

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Long addFriend(Long userId, Long friendId) {
        String result = null;
        try {
            result = jdbcTemplate.queryForObject("SELECT STATUS " +
                            "FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID =?",
                    String.class, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            String sql1 = "INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID, STATUS)" +
                    "VALUES(?, ?, ?)";
            jdbcTemplate.update(sql1, userId, friendId, "CONFIRMED");

            String sql2 = "INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID, STATUS)" +
                    "VALUES(?, ?, ?)";
            jdbcTemplate.update(sql2, friendId, userId, "UNCONFIRMED");
            log.info("Отправлен запрос в друзья");
        }
        if (result == "CONFIRMED") {
            log.info("Вы уже друзья");
        }
        if (result == "UNCONFIRMED") {
            jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = ? WHERE USER_ID = ? AND FRIEND_ID = ?",
                    "CONFIRMED", userId, friendId);
        }
        return userId;
    }

    @Override
    public List<User> findAllFriends(Long userId) {
        List<User> allFriends = new ArrayList<>();
        try {
            String sql = "SELECT * FROM USERS WHERE USER_ID IN " +
                    "(SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = ?)";
            allFriends.addAll(jdbcTemplate.query(sql, this::mapRowToUser, userId, "CONFIRMED"));
        } catch (EmptyResultDataAccessException e) {
            log.info("Список друзей пуст");
        }
        return allFriends;
    }

    @Override
    public List<User> findCommonFriends(Long user1, Long user2) {
        List<User> commonFriends = new ArrayList<>();
        try {
            String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM (SELECT * " +
                    "FROM FRIENDSHIP WHERE USER_ID =? AND STATUS = ?) AS a WHERE " +
                    "FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = ?))";
            commonFriends.addAll(jdbcTemplate.query(sql, this::mapRowToUser, user1, "CONFIRMED",
                    user2, "CONFIRMED"));
        } catch (EmptyResultDataAccessException e) {
            log.info("Нет общих друзей");
        }
        return commonFriends;
    }

    @Override
    public Long deleteFriend(Long userId, Long friendId) {
        jdbcTemplate.update("UPDATE FRIENDSHIP SET STATUS = ? WHERE USER_ID = ? AND FRIEND_ID = ?",
                "UNCONFIRMED", userId, friendId);
        return userId;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .name(resultSet.getString("USER_NAME"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getObject("birthday", LocalDate.class))
                .build();
    }
}