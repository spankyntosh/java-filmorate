package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ReFriendException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer userId) {
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return userStorage.getUserById(userId);
    }

    public Collection<User> getUserFriends(Integer userId) {
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return userStorage.getUserFriends(userId);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) {
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (!userStorage.isUserExists(otherUserId)) {
            throw new EntityNotFoundException("Попытка получить список общих друзей с пользователем с несуществующим id");
        }
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    public User addUser(User user) {
        for (User existingUser : userStorage.getUsers()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new UserOrFilmAlreadyExistException(String.format("Пользователь с почтой %s уже существует", user.getEmail()));
            }
        }

        return userStorage.addUser(user);
    }

    public User updateUserInfo(User user) {
        if (!userStorage.isUserExists(user.getId())) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", user.getId()));
        }
        return userStorage.updateUserInfo(user);
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException("Попытка добавиться в друзья к пользователю с несуществующим id");
        }
        if (!userStorage.isUserExists(friendId)) {
            throw new EntityNotFoundException("Попытка добавиться в друзья с несуществующим id");
        }
        if (userStorage.isUserAlreadyInFriends(userId, friendId)) {
            throw new ReFriendException("Попытка добавить в друзья пользователя уже находящегося в списке друзей");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void excludeFromFriends(Integer userId, Integer friendId) {
        if (!userStorage.isUserExists(userId)) {
            throw new EntityNotFoundException("Попытка исключить из друзей у пользователя с несуществующим id");
        }
        if (!userStorage.isUserExists(friendId)) {
            throw new EntityNotFoundException("Попытка исключить из друзей пользователя с несуществующим id");
        }
        if (!userStorage.isUserAlreadyInFriends(userId, friendId)) {
            throw new ReFriendException("Попытка исключить из друзей пользователя не находящегося в списке друзей");
        }
        userStorage.excludeFromFriends(userId, friendId);
    }


}
