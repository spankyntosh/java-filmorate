package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DbFilmService;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final DbFilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Пришел запрос на получение всех фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info(String.format("Пришел запрос на получение фильма с id = %d", id));
        return filmService.getFilmById(id);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getDirectorFilms(@PathVariable Integer directorId
                                           , @RequestParam(required = true) String sortBy) {
        log.info(String.format("Пришел запрос на получение режиссера с id = %d", directorId));
        return filmService.getDirectorAllFilms(directorId, sortBy);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count,
                                                @RequestParam(required = false) Integer genreId,
                                                @RequestParam(required = false) Integer year) {
        log.info(String.format("Пришел запрос на получение популярных фильмов. count = %d, genreId = %d, year = %d",
                count, genreId, year));
        return filmService.getMostPopularFilms(count, genreId, year);
    }

    @GetMapping("/search")
    public Collection<Film> search(@RequestParam String query,
                                   @RequestParam String by) {
        log.info(String.format("Пришел запрос на поиск фильмов. query = %s, by = %s", query, by));
        return filmService.search(query, by);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Пришёл запрос на добавление нового фильма");
        return filmService.addFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Integer id) {
        log.info(String.format("Пришёл запрос на удаление фильма по id = %d", id));
        filmService.deleteFilm(id);
    }

    @PutMapping
    public Film updateFilmInfo(@Valid @RequestBody Film film) {
        log.info("Пришёл запрос на изменение информации по фильму");
        return filmService.updateFilmInfo(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info(String.format("Пришёл запрос на добавление лайка фильму. id = %d, userId = %d", id, userId));
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info(String.format("Пришёл запрос на удаление лайка фильму. id = %d, userId = %d", id, userId));
        filmService.removeLike(id, userId);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(
            @RequestParam Integer userId,
            @RequestParam Integer friendId) {
        log.info(String.format("Пришёл запрос на получение общих для друзей фильмов отсортированных по популярности. " +
                "userId = %d, friendId = %d", userId, friendId));
        return filmService.getCommonFilms(userId, friendId);
    }

}
