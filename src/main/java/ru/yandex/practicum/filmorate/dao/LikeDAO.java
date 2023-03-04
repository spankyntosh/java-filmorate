package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeDAO {

    void addRecord(Integer filmId, Integer userId);
    Collection<Like> getRecord(Integer filmId);
    void deleteRecord(Integer filmId, Integer userId);
}
