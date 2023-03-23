package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface FilmGenreDAO {

    void addFilmGenreRecord(Integer filmId, Collection<Genre> genres);

    Collection<Genre> getFilmGenres(Integer filmId);

    void deleteRecordsByFilmId(Integer filmId);


}
