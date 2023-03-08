package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;


public class FilmMapper implements RowMapper<Film> {

    private MpaFilmDAO mpaFilmDAO;
    private FilmGenreDAO filmGenreDAO;

    public FilmMapper() {
    }

    public FilmMapper(MpaFilmDAO mpaFilmDAO, FilmGenreDAO filmGenreDAO) {
        this.mpaFilmDAO = mpaFilmDAO;
        this.filmGenreDAO = filmGenreDAO;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        if (mpaFilmDAO == null & filmGenreDAO == null) {
            return new Film(rs.getInt("id")
                    , rs.getString("name")
                    , rs.getString("description")
                    , rs.getDate("release_date").toLocalDate()
                    , rs.getInt("duration"));
        } else {
            MPA mpa = mpaFilmDAO.getMpaByFilmId(rs.getInt("id"));
            Collection<Genre> genres = filmGenreDAO.getFilmGenres(rs.getInt("id"));

            return new Film()
                    .toBuilder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(mpa)
                    .genres(genres)
                    .build();
        }

    }
}
