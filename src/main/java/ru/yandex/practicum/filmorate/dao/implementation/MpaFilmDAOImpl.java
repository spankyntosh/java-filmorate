package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMpaMapper;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.util.List;

@Component
public class MpaFilmDAOImpl implements MpaFilmDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaFilmDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addMpaFilmRecord(Integer filmId, Integer mpaId) {
        String statement = "INSERT INTO mpa_films "
                         + "VALUES (?, ?)";

        jdbcTemplate.update(statement, filmId, mpaId);
    }

    @Override
    public MpaFilm getMpaFilmRecord(Integer filmId) {
        String statement = "SELECT * "
                         + "FROM mpa_films "
                         + "WHERE film_id = ?";

        return jdbcTemplate.queryForObject(statement, new FilmMpaMapper(), filmId);
    }

    @Override
    public MPA getMpaByFilmId(Integer filmId) {
        String statement = "SELECT mr.id, "
                         + "mr.name "
                         + "FROM mpa_films AS mf "
                         + "LEFT JOIN mpa_ratings AS mr ON mr.id = mf.mpa_rating "
                         + "WHERE film_id = ?";

        return jdbcTemplate.queryForObject(statement, new MPAMapper(), filmId);
    }

    @Override
    public boolean isMpaFilmRecordExist(Integer filmId) {
        String statement = "SELECT * "
                         + "FROM mpa_films "
                         + "WHERE film_id = ?";

        List<MpaFilm> list = jdbcTemplate.query(statement, new FilmMpaMapper(), filmId);
        if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void deleteMpaFilmRecord(Integer filmId) {
        String statement = "DELETE FROM mpa_films WHERE film_id = ?";
        jdbcTemplate.update(statement, filmId);
    }
}
