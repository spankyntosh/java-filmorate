package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DbDirectorService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DbDirectorService directorService;

    @Autowired
    public DirectorController(DbDirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Collection<Director> getDirectors() {
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public Director addDirectorInfo(@Valid @RequestBody Director director) {
        return directorService.addDirectorInfo(director);
    }

    @PutMapping
    public Director updateDirectorInfo(@Valid @RequestBody Director director) {
        return directorService.updateDirectorInfo(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorInfo(@PathVariable Integer id) {
        directorService.deleteDirectorInfo(id);
    }

}
