package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.dao.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        String statement = "SELECT * "
                + "FROM users";

        return jdbcTemplate.query(statement, new UserMapper());
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");
        int filmId = insert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(filmId);
        return user;
    }

    @Override
    public User delete(Integer id) {
        if (!isUserExists(id)) {
            return null;
        }
        User user = getUserById(id);
        String deleteQuery = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(deleteQuery, id);

        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        String statement = "UPDATE users "
                + "SET name = ?, login = ?, birthday = ?, email = ? "
                + "WHERE id = ?";

        jdbcTemplate.update(statement
                , user.getName()
                , user.getLogin()
                , user.getBirthday()
                , user.getEmail()
                , user.getId());

        return user;
    }

    @Override
    public boolean isUserExists(Integer userId) {
        String statement = "SELECT * "
                + "FROM users "
                + "WHERE id = ?";

        List<User> userList = jdbcTemplate.query(statement, new UserMapper(), userId);
        return !userList.isEmpty();
    }

    @Override
    public User getUserById(Integer userId) {
        String statement = "SELECT * "
                + "FROM users "
                + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new UserMapper(), userId);

    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        String statement = "SELECT * "
                + "FROM users "
                + "WHERE id IN "
                + "(SELECT user_id_to_whom_send "
                + "FROM friendships "
                + "WHERE user_id_who_send = ?)";

        return jdbcTemplate.query(statement, new UserMapper(), userId);
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        String statement = "SELECT * "
                + "FROM users AS u "
                + "LEFT JOIN friendships as f1 ON u.id = f1.user_id_to_whom_send "
                + "LEFT JOIN friendships as f2 ON u.id = f2.user_id_to_whom_send "
                + "WHERE f1.user_id_who_send = ? AND f2.user_id_who_send = ?";

        return jdbcTemplate.query(statement, new UserMapper(), userId, otherId);
    }

}
