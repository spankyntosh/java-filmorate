package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;


public interface GenreDAO {

    Collection<Genre> findAll();

    Genre findById(Integer genreId);

    boolean isGenreExists(Integer genreId);

}
