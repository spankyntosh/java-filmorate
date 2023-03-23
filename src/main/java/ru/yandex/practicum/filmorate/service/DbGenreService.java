package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class DbGenreService {

    private final GenreDAO genreDAO;

    public Collection<Genre> findAll() {
        return genreDAO.findAll();
    }

    public Genre findById(Integer genreId) {
        if (!genreDAO.isGenreExists(genreId)) {
            throw new EntityNotFoundException(String.format("Жанра фильма с id %s не существует", genreId));
        }
        return genreDAO.findById(genreId);
    }
}
