package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.MpaFilm;

public interface MpaFilmDAO {

    void addMpaFilmRecord(Integer filmId, Integer mpaId);

    MpaFilm getMpaFilmRecord(Integer filmId);
    MPA getMpaByFilmId(Integer filmId);
    boolean isMpaFilmRecordExist(Integer filmId);

    void deleteMpaFilmRecord(Integer filmId);
}
