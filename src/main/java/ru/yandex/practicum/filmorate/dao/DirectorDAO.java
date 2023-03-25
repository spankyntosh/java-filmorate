package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.List;

public interface DirectorDAO {

    Collection<Director> getDirectors();

    Director getDirectorById(Integer directorId);

    Director addDirectorInfo(Director director);

    Director updateDirectorInfo(Director director);

    void deleteDirectorInfo(Integer directorId);

    boolean isDirectorExists(Integer directorId);
    boolean isAllDirectorsExists(List<Integer> directorIds, Integer expectedIdsCount);
}
