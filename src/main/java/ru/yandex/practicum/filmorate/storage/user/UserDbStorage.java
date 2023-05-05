package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validationUserName(user);
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
        findUser(user.getId());//если не находит, выбрасывает NotFoundException
        validationUserName(user);
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
        List<Long> users = jdbcTemplate.queryForList("SELECT USER_ID FROM USERS", Long.class);
        if (!users.contains(id)) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь не найден");
        }
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USER_ID = ?",
                this::mapRowToUser, id);
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

    private void validationUserName(User user) {
        if (user.getName() == null || user.getName().equals(" ") || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void deleteAllUser() {
        String sql = "DELETE FROM LIKES_LIST;ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;\n" +
                "DELETE FROM USERS;";
        jdbcTemplate.update(sql);
    }
}
