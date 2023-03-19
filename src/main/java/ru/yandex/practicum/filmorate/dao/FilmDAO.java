package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmDAO {

    Collection<Film> getFilms();
    Film getFilmById(Integer filmId);
    Collection<Film> getDirectorAllFilms(Integer directorId, String sortBy);
    Film addFilmInfo(Film film);
    void delete(Integer id);
    boolean isFilmExists(Integer filmId);
    Film updateFilmInfo(Film film);
    boolean isFilmAlreadyHaveLikeFromUser(Integer filmId, Integer userId);
    void addLike(Integer filmId, Integer userId);
    void removeLike(Integer filmId, Integer userId);
}
