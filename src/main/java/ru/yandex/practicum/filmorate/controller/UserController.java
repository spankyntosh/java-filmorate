package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Пришёл запрос на добавление нового пользователя");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUserInfo(@Valid @RequestBody User user) {
        log.info("Пришёл запрос на изменение информации по пользователю");
        return userService.updateUserInfo(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Пришёл запрос на добавление в друзья");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void excludeFromFriends(@PathVariable String id, @PathVariable String friendId) {
        log.info("Пришёл запрос на исключение из друзей");
        userService.excludeFromFriends(id, friendId);
    }


}
