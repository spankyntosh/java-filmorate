package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDAO;
import ru.yandex.practicum.filmorate.dao.mappers.LikeMapper;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;
import java.util.Map;

@Component
public class LikeDAOImpl implements LikeDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addRecord(Integer filmId, Integer userId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("likes");
        simpleJdbcInsert.execute(Map.of("film_id", filmId, "user_id", userId));
    }

    @Override
    public Collection<Like> getRecord(Integer filmId) {
        String statement = "SELECT * "
                         + "FROM likes "
                         + "WHERE film_id = ?";

        return jdbcTemplate.query(statement, new LikeMapper(), filmId);
    }

    @Override
    public void deleteRecord(Integer filmId, Integer userId) {
        String statement = "DELETE "
                         + "FROM likes "
                         + "WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(statement, filmId, userId);
    }
}
