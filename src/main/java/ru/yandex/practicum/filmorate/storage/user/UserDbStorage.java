package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO users (USER_NAME,email,login,birthday)" +
                "Values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        findUser(user.getId());
        String sql = "UPDATE users SET USER_NAME = ?, email = ?," +
                "login = ?, birthday = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> findAllUser() {
        String sql = "SELECT * FROM users";
        List<User> allUsers = jdbcTemplate.query(sql, this::mapRowToUser);
        return allUsers;
    }

    public User findUser(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USER_ID = ?",
                    this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь не найден");
        }
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

    public void deleteAllUser() {
        String sql = "DELETE FROM LIKES_LIST;ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;\n" +
                "DELETE FROM USERS;";
        jdbcTemplate.update(sql);
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
}