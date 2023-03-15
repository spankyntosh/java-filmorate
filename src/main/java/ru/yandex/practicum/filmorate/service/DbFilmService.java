package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ReLikeException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DbFilmService {

    private final FilmDAO filmDAO;
    private final UserDAO userDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final FilmGenreDAO filmGenreDAO;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFilmService(FilmDAO filmDAO, UserDAO userDAO, MpaFilmDAO mpaFilmDAO, FilmGenreDAO filmGenreDAO, JdbcTemplate jdbcTemplate) {
        this.filmDAO = filmDAO;
        this.userDAO = userDAO;
        this.mpaFilmDAO = mpaFilmDAO;
        this.filmGenreDAO = filmGenreDAO;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Film> getFilms() {
        return filmDAO.getFilms();
    }

    public Film getFilmById(Integer filmId) {
        if (!filmDAO.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        return filmDAO.getFilmById(filmId);
    }

    public Collection<Film> getPopularFilms(Integer count) {

        String statement = "SELECT f.* "
                + "FROM films as f "
                + "LEFT JOIN likes AS l ON f.id = l.film_id "
                + "GROUP BY f.id "
                + "ORDER BY COUNT(l.user_id) DESC "
                + "LIMIT ?";

        return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO), count);
    }

    public Film addFilm(Film film) {
        for (Film filmInCollection : getFilms()) {
            if (filmInCollection.getName().equals(film.getName())
                    && filmInCollection.getReleaseDate().isEqual(film.getReleaseDate())
                    && filmInCollection.getDuration() == film.getDuration()
            ) {
                throw new UserOrFilmAlreadyExistException("Такой фильм уже есть в коллекции");
            }
        }
        return filmDAO.addFilmInfo(film);
    }

    public Film updateFilmInfo(Film film) {

        if (film.getId() == null || !filmDAO.isFilmExists(film.getId())) {
            throw new EntityNotFoundException("Попытка обновить информацию по фильму с несуществующим id фильма");
        }
        return filmDAO.updateFilmInfo(film);
    }

    public void deleteFilm(Integer filmId) {
        filmDAO.delete(filmId);
    }

    public void addLike(Integer filmId, Integer userId) {
        if (!filmDAO.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (filmDAO.isFilmAlreadyHaveLikeFromUser(filmId, userId)) {
            throw new ReLikeException(String.format("у фильма с id %d уже есть лайк от пользователя с id %d", filmId, userId));
        }
        filmDAO.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (!filmDAO.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (!filmDAO.isFilmAlreadyHaveLikeFromUser(filmId, userId)) {
            throw new ReLikeException(String.format("у фильма с id %d уже отсутствует лайк от пользователя с id %d", filmId, userId));
        }
        filmDAO.removeLike(filmId, userId);
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь %d", userId));
        }
        if (!userDAO.isUserExists(friendId)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь %d", friendId));
        }

        List<Film> commonFilms = new ArrayList<>();
        String sql = "SELECT film_id " +
                "FROM (SELECT film_id FROM likes WHERE user_id = ? INTERSECT SELECT film_id FROM likes WHERE user_id = ?) " +
                "GROUP BY film_id ORDER BY COUNT(film_id) DESC";
        SqlRowSet commonFilmsIdRow = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        while (commonFilmsIdRow.next()) {
            commonFilms.add(getFilmById(commonFilmsIdRow.getInt("film_id")));
        }
        return commonFilms;
    }
}
