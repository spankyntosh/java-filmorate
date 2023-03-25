package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface FilmGenreDAO {

    void addFilmGenreRecord(Integer filmId, Collection<Genre> genres);
    void addFilmGenreRecords(Collection<Genre> genres, Integer filmId);

    Collection<Genre> getFilmGenres(Integer filmId);

    void deleteRecordsByFilmId(Integer filmId);


}
