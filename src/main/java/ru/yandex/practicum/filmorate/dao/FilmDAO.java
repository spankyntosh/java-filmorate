package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmDAO {

    Collection<Film> getFilms();
    Film getFilmById(Integer filmId);
    Collection<Film> getPopularFilms(Integer count);
    Film addFilmInfo(Film film);
    Film updateFilmInfo(Film film);
    void addLike(Integer filmId, Integer userId);
    void removeLike(Integer filmId, Integer userId);
}
