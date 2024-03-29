package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.QueryParamException;
import ru.yandex.practicum.filmorate.exceptions.ReLikeException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbFilmService {

    private final FilmDAO filmDAO;
    private final UserDAO userDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final FilmGenreDAO filmGenreDAO;
    private final LikeDAO likeDAO;
    private final DirectorDAO directorDAO;
    private final FilmDirectorDAO filmDirectorDAO;
    private final EventDAO eventDAO;
    private final JdbcTemplate jdbcTemplate;

    public Collection<Film> getFilms() {
        return filmDAO.getFilms();
    }

    public Film getFilmById(Integer filmId) {
        if (!filmDAO.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        return filmDAO.getFilmById(filmId);
    }

    public Collection<Film> getDirectorAllFilms(Integer directorId, String sortBy) {
        if (!directorDAO.isDirectorExists(directorId)) {
            throw new EntityNotFoundException("Режиссёр с таким идентификатором не найден");
        }
        if (sortBy == null || !(sortBy.contentEquals("year") || sortBy.contentEquals("likes"))) {
            throw new QueryParamException("Допустимые значения для sortBy likes или year");
        }
        return filmDAO.getDirectorAllFilms(directorId, sortBy);

    }

    public Collection<Film> getMostPopularFilms(Integer count, Integer genre, Integer year) {

        String statement;

        if (genre != null & year != null) {
            statement = "SELECT f.* "
                    + "FROM films as f "
                    + "LEFT JOIN likes AS l ON f.id = l.film_id "
                    + "LEFT JOIN films_genres AS g ON f.id = g.film_id "
                    + "WHERE EXTRACT(YEAR from f.release_date) = ? "
                    + "AND g.genre_id = ? "
                    + "GROUP BY f.id "
                    + "ORDER BY COUNT(l.user_id) DESC "
                    + "LIMIT ?";

            return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), year, genre, count);
        }
        if (genre != null) {
            statement = "SELECT f.* "
                    + "FROM films as f "
                    + "LEFT JOIN likes AS l ON f.id = l.film_id "
                    + "LEFT JOIN films_genres AS g ON f.id = g.film_id "
                    + "WHERE g.genre_id = ? "
                    + "GROUP BY f.id "
                    + "ORDER BY COUNT(l.user_id) DESC "
                    + "LIMIT ?";
            return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), genre, count);
        }
        if (year != null) {
            statement = "SELECT f.* "
                    + "FROM films as f "
                    + "LEFT JOIN likes AS l ON f.id = l.film_id "
                    + "LEFT JOIN films_genres AS g ON f.id = g.film_id "
                    + "WHERE EXTRACT(YEAR from f.release_date) = ? "
                    + "GROUP BY f.id "
                    + "ORDER BY COUNT(l.user_id) DESC "
                    + "LIMIT ?";
            return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), year, count);
        } else {
            statement = "SELECT f.* "
                    + "FROM films as f "
                    + "LEFT JOIN likes AS l ON f.id = l.film_id "
                    + "GROUP BY f.id "
                    + "ORDER BY COUNT(l.user_id) DESC "
                    + "LIMIT ?";

            return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), count);
        }
    }

    public Collection<Film> search(String query, String by) {
        if (by.contains("director") && by.contains("title")) {
            return filmDAO.search(query);
        }
        if (by.contains("director")) {
            return filmDAO.searchByDirector(query);
        }
        if (by.contains("title")) {
            return filmDAO.searchByTitle(query);
        }

        throw new QueryParamException("Допустимые значения для by - title и/или director");
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
        Event event = new Event()
                .toBuilder()
                .userId(userId)
                .entityId(filmId)
                .timestamp(System.currentTimeMillis())
                .eventType(EventType.LIKE)
                .operation(Operation.ADD)
                .build();
        eventDAO.addEvent(event);
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
        Event event = new Event()
                .toBuilder()
                .userId(userId)
                .entityId(filmId)
                .timestamp(System.currentTimeMillis())
                .eventType(EventType.LIKE)
                .operation(Operation.REMOVE)
                .build();
        eventDAO.addEvent(event);
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь %d", userId));
        }
        if (!userDAO.isUserExists(friendId)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь %d", friendId));
        }
        return likeDAO.getCommonFilms(userId, friendId);
    }
}
