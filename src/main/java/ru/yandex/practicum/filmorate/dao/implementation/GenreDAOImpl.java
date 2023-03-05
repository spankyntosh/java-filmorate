package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.dao.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;


@Component
public class GenreDAOImpl implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> findAll() {
        String statement = "SELECT * "
                         + "FROM genres";

        return jdbcTemplate.query(statement, new GenreMapper());
    }

    @Override
    public Genre findById(Integer genreId) {
        String statement = "SELECT * "
                         + "FROM genres"
                         + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new GenreMapper(), genreId);
    }

    @Override
    public boolean isGenreExists(Integer genreId) {
        String statement = "SELECT * "
                         + "FROM genres"
                         + "WHERE id = ?";

        List<Genre> genreList = jdbcTemplate.queryForList(statement, Genre.class, genreId);
        return !genreList.isEmpty();
    }
}
