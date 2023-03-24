package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDAO;
import ru.yandex.practicum.filmorate.dao.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FilmDirectorDAOImpl implements FilmDirectorDAO {

    private final JdbcTemplate jdbcTemplate;

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
        Map<String, Integer> directorFilmMap = Map.of("director_id", directorId, "film_id", filmId);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films_directors");
        insert.execute(directorFilmMap);
    }

    @Override
    public void addRecords(List<Director> directors, Integer filmId) {

        String statement = "INSERT INTO films_directors (director_id, film_id) VALUES(?, ?)";
        BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, directors.get(i).getId());
                ps.setInt(2, filmId);
            }

            @Override
            public int getBatchSize() {
                return directors.size();
            }
        };

        jdbcTemplate.batchUpdate(statement, setter);

    }

    @Override
    public void deleteRecords(Integer filmId) {
        String statement = "DELETE "
                + "FROM films_directors "
                + "WHERE film_id = ?";

        jdbcTemplate.update(statement, filmId);
    }
}
