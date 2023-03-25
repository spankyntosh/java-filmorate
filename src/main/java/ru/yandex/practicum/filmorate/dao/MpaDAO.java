package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MpaDAO {

    Collection<MPA> findAll();

    MPA findById(Integer mpaId);

    public boolean isMpaExists(Integer mpaId);
}
