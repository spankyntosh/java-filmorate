package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public User addUser(User user) {
        for (User existingUser : userStorage.getUsers()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new UserOrFilmAlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }
        return userStorage.addUser(user);
    }

    public User updateUserInfo(User user) {
        if (userStorage.isUserExists(user)) {
            throw new UpdateFilmOrUserWithIncorrectIdException("Попытка обновить информацию по пользователю с несуществующим id");
        }
        return userStorage.updateUserInfo(user);
    }
}
