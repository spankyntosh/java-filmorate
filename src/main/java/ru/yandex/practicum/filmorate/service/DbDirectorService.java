package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.RequiredBodyFieldAbsenceException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Objects;

@Service
public class DbDirectorService {

    private final DirectorDAO directorDAO;

    @Autowired
    public DbDirectorService(DirectorDAO directorDAO) {
        this.directorDAO = directorDAO;
    }

    public Collection<Director> getDirectors() {
        return directorDAO.getDirectors();
    }

    public Director getDirectorById(Integer directorId) {
        if (!directorDAO.isDirectorExists(directorId)) {
            throw new EntityNotFoundException(String.format("Режиссёр с идентификатором %d не найден", directorId));
        }
        return directorDAO.getDirectorById(directorId);
    }

    public Director addDirectorInfo(Director director) {
        if (Objects.isNull(director.getName()) || director.getName().isEmpty()) {
            throw new RequiredBodyFieldAbsenceException("Поле с именем режиссёра отсутствует или имеет пустое значение");
        }
        directorDAO.addDirectorInfo(director);
        return director;
    }

    public Director updateDirectorInfo(Director director) {
        if (Objects.isNull(director.getName()) || director.getName().isEmpty()) {
            throw new RequiredBodyFieldAbsenceException("Поле с именем режиссёра отсутствует или имеет пустое значение");
        }
        if (Objects.isNull(director.getId())) {
            throw new RequiredBodyFieldAbsenceException("Поле с id режиссёра отсутствует или имеет пустое значение");
        }
        if (!directorDAO.isDirectorExists(director.getId())) {
            throw new EntityNotFoundException(String.format("Режиссёр с идентификатором %d не найден", director.getId()));
        }
        directorDAO.updateDirectorInfo(director);
        return director;
    }

    public void deleteDirectorInfo(Integer directorId) {
        if (!directorDAO.isDirectorExists(directorId)) {
            throw new EntityNotFoundException(String.format("Режиссёр с идентификатором %d не найден", directorId));
        }
        directorDAO.deleteDirectorInfo(directorId);
    }


}
