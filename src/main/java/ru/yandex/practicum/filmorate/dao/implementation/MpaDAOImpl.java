package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.MpaDAO;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.List;

public class MpaDAOImpl implements MpaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPA> findAll() {
        String statement = "SELECT * "
                         + "FROM mpa_ratings";

        return jdbcTemplate.query(statement, new MPAMapper());
    }

    @Override
    public MPA findById(Integer mpaId) {
        String statement = "SELECT * "
                         + "FROM mpa_ratings"
                         + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new MPAMapper(), mpaId);

    }

    @Override
    public boolean isMpaExists(Integer mpaId) {
        String statement = "SELECT * "
                         + "FROM mpa_ratings"
                         + "WHERE id = ?";

        List<MPA> list = jdbcTemplate.queryForList(statement, MPA.class, mpaId);
        return !list.isEmpty();
    }
}
