package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDAO;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;

@Component
public class MpaRatingDAOImpl implements MpaRatingDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaRatingDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMpaById(Integer mpaId) {
        String statement = "SELECT * "
                         + "FROM mpa_ratings "
                         + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new MPAMapper(), mpaId);
    }
}
