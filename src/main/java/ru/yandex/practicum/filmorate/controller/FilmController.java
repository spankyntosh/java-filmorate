package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UpdateFilmOrUserWithIncorrectIdException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    int idCounter = 1;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    Map<Integer, Film> filmsCollection = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return this.filmsCollection.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Пришёл запрос на добавление нового фильма");
        for (Film filmInCollection : filmsCollection.values()) {
            if (filmInCollection.getName().equals(film.getName())
                && filmInCollection.getReleaseDate().isEqual(film.getReleaseDate())
                && filmInCollection.getDuration() == film.getDuration()
            ) {
                throw new UserOrFilmAlreadyExistException("Такой фильм уже есть в коллекции");
            }
        }
        film.setId(idCounter);
        idCounter++;
        filmsCollection.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilmInfo(@Valid @RequestBody Film film) {
        log.info("Пришёл запрос на изменение информации по фильму");
        if (!filmsCollection.keySet().contains(film.getId())) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка обновить информацию по фильму с несуществующим id фильма");
        }
        filmsCollection.put(film.getId(), film);
        return film;
    }

}
