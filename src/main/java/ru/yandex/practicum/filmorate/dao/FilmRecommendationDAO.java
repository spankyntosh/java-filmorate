package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRecommendationDAO {

    Collection<Film> getRecommendation(Integer userId);
}
