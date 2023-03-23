package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.dao.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DirectorDAOImpl implements DirectorDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Director> getDirectors() {

        String statement = "SELECT * "
                + "FROM directors;";

        return jdbcTemplate.query(statement, new DirectorMapper());
    }

    @Override
    public Director getDirectorById(Integer directorId) {
        String statement = "SELECT * "
                + "FROM directors "
                + "WHERE id = ?;";

        return jdbcTemplate.queryForObject(statement, new DirectorMapper(), directorId);
    }

    @Override
    public Director addDirectorInfo(Director director) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");
        int directorId = insert.executeAndReturnKey(director.toMap()).intValue();

        director.setId(directorId);
        return director;
    }

    @Override
    public Director updateDirectorInfo(Director director) {
        String statement = "UPDATE directors "
                + "SET director_name = ? "
                + "WHERE id = ?";

        jdbcTemplate.update(statement, director.getName(), director.getId());
        return director;
    }

    @Override
    public void deleteDirectorInfo(Integer directorId) {
        String statement = "DELETE FROM directors WHERE id = ?";

        jdbcTemplate.update(statement, directorId);
    }

    @Override
    public boolean isDirectorExists(Integer directorId) {
        String statement = "SELECT * "
                + "FROM directors "
                + "WHERE id = ?";
        List<Director> list = jdbcTemplate.query(statement, new DirectorMapper(), directorId);

        return !list.isEmpty();
    }
}
