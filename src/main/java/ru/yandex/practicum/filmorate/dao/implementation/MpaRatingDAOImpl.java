package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRatingDAO;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;

@Repository
@RequiredArgsConstructor
public class MpaRatingDAOImpl implements MpaRatingDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA getMpaById(Integer mpaId) {
        String statement = "SELECT * "
                + "FROM mpa_ratings "
                + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new MPAMapper(), mpaId);
    }
}
