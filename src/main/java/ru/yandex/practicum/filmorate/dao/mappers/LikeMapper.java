package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Like()
                .toBuilder()
                .filmId(rs.getInt("film_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}
