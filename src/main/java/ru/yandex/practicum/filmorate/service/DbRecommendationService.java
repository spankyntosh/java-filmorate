package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRecommendationDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Service
public class DbRecommendationService {

    private final FilmRecommendationDAO filmRecommendationDAO;
    private final UserDAO userDAO;

    @Autowired
    public DbRecommendationService(FilmRecommendationDAO filmRecommendationDAO, UserDAO userDAO) {
        this.filmRecommendationDAO = filmRecommendationDAO;
        this.userDAO = userDAO;
    }

    public Collection<Film> getRecommendations(Integer userId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return filmRecommendationDAO.getRecommendation(userId);
    }

}
