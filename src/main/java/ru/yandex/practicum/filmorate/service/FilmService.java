package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ReLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer filmId) {
        if (!filmStorage.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film addFilm(Film film) {
        for (Film filmInCollection : filmStorage.getFilms()) {
            if (filmInCollection.getName().equals(film.getName())
                    && filmInCollection.getReleaseDate().isEqual(film.getReleaseDate())
                    && filmInCollection.getDuration() == film.getDuration()
            ) {
                throw new UserOrFilmAlreadyExistException("Такой фильм уже есть в коллекции");
            }
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilmInfo(Film film) {

        if (film.getId() == null || !filmStorage.isFilmExists(film.getId())) {
            throw new EntityNotFoundException("Попытка обновить информацию по фильму с несуществующим id фильма");
        }
        return filmStorage.updateFilmInfo(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        if (!filmStorage.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (filmStorage.isFilmAlreadyHaveLikeFromUser(filmId, userId)) {
            throw new ReLikeException(String.format("у фильма с id %d уже есть лайк от пользователя с id %d", filmId, userId));
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (!filmStorage.isFilmExists(filmId)) {
            throw new EntityNotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (!filmStorage.isFilmAlreadyHaveLikeFromUser(filmId, userId)) {
            throw new ReLikeException(String.format("у фильма с id %d уже отсутствует лайк от пользователя с id %d", filmId, userId));
        }
        filmStorage.removeLike(filmId, userId);
    }
}
