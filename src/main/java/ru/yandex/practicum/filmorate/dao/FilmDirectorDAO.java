package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface FilmDirectorDAO {

    Collection<Director> getFilmDirectors(Integer filmId);
    void addRecord (Integer directorId, Integer filmId);
    void deleteRecords(Integer filmId);
}
