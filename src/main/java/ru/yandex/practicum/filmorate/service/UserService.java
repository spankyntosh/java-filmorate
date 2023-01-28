package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.TryingToReFriendException;
import ru.yandex.practicum.filmorate.exceptions.UpdateFilmOrUserWithIncorrectIdException;
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

    public Collection<User> getUserFriends(Integer userId) {
        if (!userStorage.isUserExists(userId)) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка получить список друзей у пользователя с несуществующим id");
        }
        return userStorage.getUserFriends(userId);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) {
        if (!userStorage.isUserExists(userId)) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка получить список общих друзей у пользователя с несуществующим id");
        }
        if (!userStorage.isUserExists(otherUserId)) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка получить список общих друзей с пользователем с несуществующим id");
        }
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    public User addUser(User user) {
        for (User existingUser : userStorage.getUsers()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new UserOrFilmAlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }
        return userStorage.addUser(user);
    }

    public User updateUserInfo(User user) {
        if (!userStorage.isUserExists(user.getId())) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка обновить информацию по пользователю с несуществующим id");
        }
        return userStorage.updateUserInfo(user);
    }

    public void addFriend(String userId, String friendId) {
        if (!userStorage.isUserExists(Integer.valueOf(userId))) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка добавить в друзья пользователя с несуществующим id");
        }
        if (!userStorage.isUserExists(Integer.valueOf(friendId))) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка добавиться в друзья с несуществующим id пользователя");
        }
        if (userStorage.isUserAlreadyInFriends(userId, friendId)) {
            throw new TryingToReFriendException("Попытка добавить в друзья пользователя уже находящегося в списке друзей");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void excludeFromFriends(String userId, String friendId) {
        if (!userStorage.isUserExists(Integer.valueOf(userId))) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка исключить из друзей пользователя с несуществующим id");
        }
        if (!userStorage.isUserExists(Integer.valueOf(friendId))) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка исключить друга у пользователя с несуществующим id");
        }
        if (!userStorage.isUserAlreadyInFriends(userId, friendId)) {
            throw new TryingToReFriendException("Попытка исключить из друзей пользователя не находящегося в списке друзей");
        }
        userStorage.excludeFromFriends(userId, friendId);
    }


}
