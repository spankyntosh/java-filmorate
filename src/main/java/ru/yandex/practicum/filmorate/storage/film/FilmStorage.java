package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getFilms();

    Film getFilmById(Integer filmId);

    Collection<Film> getPopularFilms(Integer count);

    Film addFilm(Film film);

    Film updateFilmInfo(Film film);

    boolean isFilmExists(Integer filmId);

    boolean isFilmAlreadyHaveLikeFromUser(Integer filmId, Integer userId);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

}
