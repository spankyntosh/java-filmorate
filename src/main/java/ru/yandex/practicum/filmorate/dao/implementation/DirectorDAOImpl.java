package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
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

        String statement = "SELECT EXISTS (SELECT * "
                + "FROM directors "
                + "WHERE id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(statement, Boolean.TYPE, directorId));
    }

    @Override
    public boolean isAllDirectorsExists(List<Integer> directorIds, Integer expectedIdsCount) {
        String baseStatement = "SELECT COUNT (director_name) FROM directors WHERE id IN (";
        StringBuilder queryBuilder = new StringBuilder(baseStatement);
        for (int i = 0; i < directorIds.size(); i++) {
            if (i == directorIds.size() - 1) {
                queryBuilder.append(directorIds.get(i) + " )");
            } else {
                queryBuilder.append(directorIds.get(i) + ", ");
            }
        }
        Integer dbCount = jdbcTemplate.queryForObject(queryBuilder.toString(), Integer.TYPE);
        return dbCount == expectedIdsCount;
    }
}
