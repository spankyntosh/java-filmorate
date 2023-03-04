package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
public class FilmGenreDAOImpl implements FilmGenreDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilmGenreRecord(Integer filmId, Collection<Genre> genres) {
        String statement = "INSERT INTO films_genres "
                         + "VALUES (?, ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(statement, filmId, genre.getId());
        }
    }

    @Override
    public Collection<Genre> getFilmGenres(Integer filmId) {
        String statement = "SELECT g.id, "
                         + "g.name "
                         + "FROM films_genres AS fg "
                         + "LEFT JOIN genres AS g ON fg.genre_id = g.id "
                         + "WHERE film_id = ?";

        return jdbcTemplate.query(statement, new GenreMapper(), filmId);
    }

    @Override
    public void deleteRecordsByFilmId(Integer filmId) {
        String statement = "DELETE "
                         + "FROM films_genres "
                         + "WHERE film_id = ?";

        jdbcTemplate.update(statement, filmId);
    }
}
