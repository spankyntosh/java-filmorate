package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FilmMpaMapper implements RowMapper<MpaFilm> {

    @Override
    public MpaFilm mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MpaFilm()
                .toBuilder()
                .filmId(rs.getInt("film_id"))
                .mpaId(rs.getInt("mpa_rating"))
                .build();
    }
}
