package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FilmMapper implements RowMapper<Film> {

    public FilmMapper() {
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("id")
                , rs.getString("name")
                , rs.getString("description")
                , rs.getDate("release_date").toLocalDate()
                , rs.getInt("duration"));

    }
}
