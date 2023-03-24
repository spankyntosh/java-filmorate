package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DbDirectorService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {

    private final DbDirectorService directorService;

    @GetMapping
    public Collection<Director> getDirectors() {
        log.info("Пришёл запрос на получение всех режиссёров");
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) {
        log.info(String.format("Пришёл запрос на получение информации по режиссёру с id %d", id));
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public Director addDirectorInfo(@Valid @RequestBody Director director) {
        log.info(String.format("Пришёл запрос на добавление информации по режиссёру: %s", director));
        return directorService.addDirectorInfo(director);
    }

    @PutMapping
    public Director updateDirectorInfo(@Valid @RequestBody Director director) {
        log.info(String.format("Пришёл запрос на обновление информации по режиссёру: %s", director));
        return directorService.updateDirectorInfo(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorInfo(@PathVariable Integer id) {
        log.info(String.format("Пришёл запрос на удаление информации по режиссёру с id %d", id));
        directorService.deleteDirectorInfo(id);
    }

}
