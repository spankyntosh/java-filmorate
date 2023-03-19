package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDAO;
import ru.yandex.practicum.filmorate.dao.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Map;

@Component
public class FilmDirectorDAOImpl implements FilmDirectorDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDirectorDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Director> getFilmDirectors(Integer filmId) {
        String statement = "SELECT * "
                + "FROM directors as d "
                + "LEFT JOIN films_directors AS fd ON d.id = fd.director_id "
                + "WHERE fd.film_id = ?";
        return jdbcTemplate.query(statement, new DirectorMapper(), filmId);
    }

    @Override
    public void addRecord(Integer directorId, Integer filmId) {
        Map<String, Integer> map = Map.of("director_id", directorId, "film_id", filmId);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films_directors");
        insert.execute(map);
    }

    @Override
    public void deleteRecords(Integer filmId) {
        String statement = "DELETE "
                + "FROM films_directors "
                + "WHERE film_id = ?";

        jdbcTemplate.update(statement, filmId);
    }
}
