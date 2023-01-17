package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {

    Map<Integer, Film> filmsCollection = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return this.filmsCollection.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        filmsCollection.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilmInfo(@RequestBody Film film) {
        filmsCollection.put(film.getId(), film);
        return film;
    }

}
