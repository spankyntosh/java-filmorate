package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    FilmService filmService;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Пришёл запрос на добавление нового фильма");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilmInfo(@Valid @RequestBody Film film) {
        log.info("Пришёл запрос на изменение информации по фильму");
        return filmService.updateFilmInfo(film);
    }

}
