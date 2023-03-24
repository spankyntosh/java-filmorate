package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.FilmRecommendationDAO;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class FilmRecommendationDAOImpl implements FilmRecommendationDAO {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDAO filmGenreDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final FilmDirectorDAO filmDirectorDAO;

    @Override
    public Collection<Film> getRecommendation(Integer userId) {

        String statement = "SELECT *" +
                " FROM films fl " +
                " WHERE fl.id IN (" +
                "       SELECT DISTINCT l.film_id FROM likes l " +
                " WHERE l.user_id IN (" +
                "       SELECT l.user_id FROM likes AS l" +
                " WHERE l.film_id IN (" +
                "       SELECT f.id FROM films AS f" +
                " RIGHT JOIN likes l ON f.id = l.film_id" +
                " WHERE l.user_id = ?)" +
                "   AND l.user_id <> ?)" +
                "   AND l.film_id NOT IN " +
                "       (SELECT l.film_id FROM likes l" +
                " WHERE l.user_id = ?))";

        return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO),
                userId, userId, userId);
    }
}
