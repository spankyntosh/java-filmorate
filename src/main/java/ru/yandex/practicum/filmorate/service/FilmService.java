package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmOrUserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserReLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer filmId) {
        if (filmStorage.isFilmExists(filmId)) {
            throw new UserOrFilmAlreadyExistException(String.format("фильм с id %s не найден", filmId));
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
            throw new FilmOrUserNotFoundException("Попытка обновить информацию по фильму с несуществующим id фильма");
        }
        return filmStorage.updateFilmInfo(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        if (filmStorage.isFilmAlreadyHaveLikeFromUser(filmId, userId)) {
            throw new UserReLikeException(String.format("у фильма с id %d уже есть лайк от пользователя с id %d", filmId, userId));
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (!filmStorage.isFilmAlreadyHaveLikeFromUser(filmId, userId)) {
            throw new UserReLikeException(String.format("у фильма с id %d уже отсутствует лайк от пользователя с id %d", filmId, userId));
        }
        filmStorage.removeLike(filmId, userId);
    }
}
