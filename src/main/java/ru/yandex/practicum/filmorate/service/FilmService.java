package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UpdateFilmOrUserWithIncorrectIdException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        for (Film filmInCollection : filmStorage.getFilms()) {
            if (filmInCollection.getName().equals(film.getName())
                    && filmInCollection.getReleaseDate().isEqual(film.getReleaseDate())
                    && filmInCollection.getDuration() == film.getDuration()
            ) {
                throw new UserOrFilmAlreadyExistException("Такой фильм уже есть в коллекции");
            }
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilmInfo(Film film) {

        if (film.getId() == null || !filmStorage.ifFilmExists(film)) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка обновить информацию по фильму с несуществующим id фильма");
        }
        return filmStorage.updateFilmInfo(film);
    }
}
