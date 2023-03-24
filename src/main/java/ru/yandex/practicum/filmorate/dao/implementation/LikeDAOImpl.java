package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.LikeDAO;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mappers.LikeMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LikeDAOImpl implements LikeDAO {

    private final MpaFilmDAO mpaFilmDAO;
    private final FilmGenreDAO filmGenreDAO;
    private final FilmDirectorDAO filmDirectorDAO;
    private final JdbcTemplate jdbcTemplate;

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

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sql = "SELECT f.id, f.name, f.description, f.duration, f.release_date, f.rating "
                + "FROM films AS f "
                + "LEFT JOIN likes AS l ON f.id = l.film_id "
                + "WHERE id IN "
                + "(SELECT film_id FROM likes WHERE user_id = ? INTERSECT SELECT film_id FROM likes WHERE user_id = ?) "
                + "GROUP BY f.id "
                + "ORDER BY COUNT(l.user_id) DESC";

        return jdbcTemplate.query(sql, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), userId, friendId);
    }
}
