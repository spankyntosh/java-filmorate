package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDAO;

@Component
public class GenreDAOImpl implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
