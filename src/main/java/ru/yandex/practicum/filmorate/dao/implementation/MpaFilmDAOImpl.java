package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMpaMapper;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaFilmDAOImpl implements MpaFilmDAO {

    private final JdbcTemplate jdbcTemplate;

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
        return !list.isEmpty();
    }

    @Override
    public void deleteMpaFilmRecord(Integer filmId) {
        String statement = "DELETE FROM mpa_films WHERE film_id = ?";
        jdbcTemplate.update(statement, filmId);
    }
}
