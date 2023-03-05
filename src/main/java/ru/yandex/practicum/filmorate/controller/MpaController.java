package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.DbMpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final DbMpaService dbMpaService;

    @Autowired
    public MpaController(DbMpaService dbMpaService) {
        this.dbMpaService = dbMpaService;
    }

    @GetMapping
    public Collection<MPA> findAll() {
        return dbMpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public MPA findById(@PathVariable Integer mpaId) {
        return dbMpaService.findById(mpaId);
    }
}
