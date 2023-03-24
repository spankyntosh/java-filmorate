package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.DbGenreService;

import java.util.Collection;


@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final DbGenreService genreService;

    @GetMapping
    public Collection<Genre> findAll() {
        log.info("Пришёл запрос на получение всех жанров фильмов");
        return genreService.findAll();
    }

    @GetMapping("/{genreId}")
    public Genre findById(@PathVariable Integer genreId) {
        log.info(String.format("Пришёл запрос на получение на получение жанра с id %d", genreId));
        return genreService.findById(genreId);
    }
}
