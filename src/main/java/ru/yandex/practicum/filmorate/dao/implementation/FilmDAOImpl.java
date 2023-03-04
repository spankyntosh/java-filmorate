package ru.yandex.practicum.filmorate.dao.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.LikeDAO;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilmDAOImpl implements FilmDAO {

    private final FilmGenreDAO filmGenreDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final LikeDAO likeDAO;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDAOImpl(JdbcTemplate jdbcTemplate, FilmGenreDAO filmGenreDAO, MpaFilmDAO mpaFilmDAO, LikeDAO likeDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDAO = filmGenreDAO;
        this.mpaFilmDAO = mpaFilmDAO;
        this.likeDAO = likeDAO;
    }

    @Override
    public Collection<Film> getFilms() {
        String statement = "SELECT * "
                         + "FROM films";

        List<Film> filmList = jdbcTemplate.query(statement, new FilmMapper());
        return filmList.stream()
                .peek(film -> film.setMpa(mpaFilmDAO.getMpaByFilmId(film.getId())))
                .peek(film -> film.setGenres(filmGenreDAO.getFilmGenres(film.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String statement = "SELECT * "
                         + "FROM films "
                         + "WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(statement, new FilmMapper(), filmId);

        film.setMpa(mpaFilmDAO.getMpaByFilmId(filmId));
        film.setGenres(filmGenreDAO.getFilmGenres(filmId));
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        String statement = "SELECT * "
                + "FROM films "
                + "WHERE id IN "
                + "(SELECT film_id "
                + "FROM likes "
                + "GROUP BY film_id "
                + "ORDER BY COUNT(user_id) DESC) "
                + "LIMIT ?";

        List<Film> filmList = jdbcTemplate.query(statement, new FilmMapper(), count);
        return filmList.stream()
                .peek(film -> film.setMpa(mpaFilmDAO.getMpaByFilmId(film.getId())))
                .peek(film -> film.setGenres(filmGenreDAO.getFilmGenres(film.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilmInfo(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        int filmId = insert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(filmId);
        if (film.getMpa() != null) {
            mpaFilmDAO.addMpaFilmRecord(film.getId(), film.getMpa().getId());
        }
        if (film.getGenres() != null) {
            filmGenreDAO.addFilmGenreRecord(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film updateFilmInfo(Film film) {
        String statement = "UPDATE films "
                         + "SET name = ?, description = ?, duration = ?, release_date = ? WHERE id = ?";

        jdbcTemplate.update(statement
                , film.getName()
                , film.getDescription()
                , film.getDuration()
                , film.getReleaseDate()
                , film.getId()
        );

        mpaFilmDAO.deleteMpaFilmRecord(film.getId());
        if (film.getMpa() != null) {
            mpaFilmDAO.addMpaFilmRecord(film.getId(), film.getMpa().getId());
        }

        filmGenreDAO.deleteRecordsByFilmId(film.getId());
        if (!film.getGenres().isEmpty()) {
            film.getGenres().stream().forEach(genre -> filmGenreDAO.addFilmGenreRecord(film.getId(), List.of(genre)));
        }

        return film;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        likeDAO.addRecord(filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        likeDAO.deleteRecord(filmId, userId);
    }


}
