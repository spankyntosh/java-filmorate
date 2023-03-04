package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDAO;

@Component
public class FriendshipDAOImpl implements FriendshipDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
