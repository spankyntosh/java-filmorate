package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDAO;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDAOImpl implements MpaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<MPA> findAll() {
        String statement = "SELECT * "
                + "FROM mpa_ratings";

        return jdbcTemplate.query(statement, new MPAMapper());
    }

    @Override
    public MPA findById(Integer mpaId) {
        String statement = "SELECT * "
                + "FROM mpa_ratings "
                + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new MPAMapper(), mpaId);

    }

    @Override
    public boolean isMpaExists(Integer mpaId) {
        String statement = "SELECT * "
                + "FROM mpa_ratings "
                + "WHERE id = ?";

        List<MPA> list = jdbcTemplate.query(statement, new MPAMapper(), mpaId);
        return !list.isEmpty();
    }
}
