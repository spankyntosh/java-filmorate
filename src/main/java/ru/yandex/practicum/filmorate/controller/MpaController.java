package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.DbMpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {

    private final DbMpaService dbMpaService;

    @GetMapping
    public Collection<MPA> findAll() {
        log.info("Пришёл запрос на получение информации по всем рейтингам MPA");
        return dbMpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public MPA findById(@PathVariable Integer mpaId) {
        log.info(String.format("Пришёл запрос на получение информации по рейтингу с id %d", mpaId));
        return dbMpaService.findById(mpaId);
    }
}
