package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private int idCounter = 1;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    Map<Integer, User> users = new HashMap<>();
    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Пришёл запрос на добавление нового пользователя");
        user.setId(idCounter);
        idCounter++;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUserInfo(@Valid @RequestBody User user) {
        log.info("Пришёл запрос на изменение информации по пользователю");
        users.put(user.getId(), user);
        return user;
    }

}
