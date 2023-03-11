package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.DbGenreService;

import java.util.Collection;


@RestController
@RequestMapping("/genres")
public class GenreController {

    private final DbGenreService genreService;

    @Autowired
    public GenreController(DbGenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{genreId}")
    public Genre findById(@PathVariable Integer genreId) {
        return genreService.findById(genreId);
    }
}
