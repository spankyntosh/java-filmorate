package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();
    Film addFilm(Film film);
    Film updateFilmInfo(Film film);

}
