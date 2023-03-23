package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.dao.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class GenreDAOImpl implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String statement = "SELECT * "
                + "FROM genres";

        return jdbcTemplate.query(statement, new GenreMapper());
    }

    @Override
    public Genre findById(Integer genreId) {
        String statement = "SELECT * "
                + "FROM genres "
                + "WHERE id = ?";

        return jdbcTemplate.queryForObject(statement, new GenreMapper(), genreId);
    }

    @Override
    public boolean isGenreExists(Integer genreId) {
        String statement = "SELECT * "
                + "FROM genres "
                + "WHERE id = ?";

        List<Genre> genreList = jdbcTemplate.query(statement, new GenreMapper(), genreId);
        return !genreList.isEmpty();
    }
}
