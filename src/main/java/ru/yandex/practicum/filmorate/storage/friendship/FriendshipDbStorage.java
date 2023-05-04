package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FriendshipDbStorage implements FriendshipStorage {
    private JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(FriendshipDbStorage.class);

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public List<Long> findAllFriends(Long userId) {
        List<Long> allFriends = new ArrayList<>();
        try {
            String sql = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = ?";
            allFriends.addAll(jdbcTemplate.queryForList(sql, Long.class, userId, "CONFIRMED"));
        } catch (EmptyResultDataAccessException e) {
            log.info("Список друзей пуст");
        }
        return allFriends;
    }

    @Override
    public List<Long> findCommonFriends(Long user1, Long user2) {
        List<Long> commonFriends = new ArrayList<>();
        try {
            String sql = "SELECT USER_ID FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM (SELECT * " +
                    "FROM FRIENDSHIP WHERE USER_ID =? AND STATUS = ?) AS a WHERE " +
                    "FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = ?))";
            commonFriends.addAll(jdbcTemplate.queryForList(sql, Long.class, user1, "CONFIRMED",
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
}
