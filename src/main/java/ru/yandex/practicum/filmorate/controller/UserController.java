package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.DbRecommendationService;
import ru.yandex.practicum.filmorate.service.DbUserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final DbUserService userService;
    private final DbRecommendationService recommendationService;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Пришел запрос на получение всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info(String.format("Пришел запрос на получение пользователя с id = %d", id));
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer id) {
        log.info(String.format("Пришел запрос на получение друзей пользователя с id = %d", id));
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info(String.format("Пришел запрос на получение общих друзей пользователя с id = %d. otherId = %d",
                id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable Integer id) {
        log.info(String.format("Пришел запрос на получение рекомендаций для пользователя с id = %d", id));
        return recommendationService.getRecommendations(id);
    }

    @GetMapping("/{id}/feed")
    public Collection<Event> getUserFeed(@PathVariable Integer id) {
        log.info(String.format("Пришел запрос на получение ленты событий пользователя с id = %d", id));
        return userService.getUserFeed(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Пришёл запрос на добавление нового пользователя");
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        log.info(String.format("Пришёл запрос на удаление пользователя по id = %d", id));
        userService.deleteUser(id);
    }

    @PutMapping
    public User updateUserInfo(@Valid @RequestBody User user) {
        log.info("Пришёл запрос на изменение информации по пользователю");
        return userService.updateUserInfo(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Пришёл запрос на добавление в друзья");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void excludeFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Пришёл запрос на исключение из друзей");
        userService.excludeFromFriends(id, friendId);
    }


}
