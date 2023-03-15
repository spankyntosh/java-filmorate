package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendshipDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ReFriendException;
import ru.yandex.practicum.filmorate.exceptions.UserOrFilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Objects;

@Service
public class DbUserService {

    private final UserDAO userDAO;
    private final FriendshipDAO friendshipDAO;

    @Autowired
    public DbUserService(UserDAO userDAO, FriendshipDAO friendshipDAO) {
        this.userDAO = userDAO;
        this.friendshipDAO = friendshipDAO;
    }

    public Collection<User> getUsers() {
        return userDAO.getUsers();
    }

    public User getUserById(Integer userId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return userDAO.getUserById(userId);
    }

    public Collection<User> getUserFriends(Integer userId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return userDAO.getUserFriends(userId);
    }

    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (!userDAO.isUserExists(otherUserId)) {
            throw new EntityNotFoundException("Попытка получить список общих друзей с пользователем с несуществующим id");
        }
        return userDAO.getCommonFriends(userId, otherUserId);
    }

    public User addUser(User user) {
        for (User existingUser : userDAO.getUsers()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new UserOrFilmAlreadyExistException(String.format("Пользователь с почтой %s уже существует", user.getEmail()));
            }
        }

        return userDAO.addUser(user);
    }

    public User updateUserInfo(User user) {
        if (!userDAO.isUserExists(user.getId())) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", user.getId()));
        }
        return userDAO.updateUserInfo(user);
    }

    public void deleteUser(Integer userId) {
        userDAO.delete(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException("Попытка добавиться в друзья к пользователю с несуществующим id");
        }
        if (!userDAO.isUserExists(friendId)) {
            throw new EntityNotFoundException("Попытка добавиться в друзья с несуществующим id");
        }
        if (friendshipDAO.isUserAlreadyInFriends(userId, friendId)) {
            throw new ReFriendException("Попытка добавить в друзья пользователя уже находящегося в списке друзей");
        }
        friendshipDAO.addFriend(userId, friendId);
    }

    public void excludeFromFriends(Integer userId, Integer friendId) {
        if (!userDAO.isUserExists(userId)) {
            throw new EntityNotFoundException("Попытка исключить из друзей у пользователя с несуществующим id");
        }
        if (!userDAO.isUserExists(friendId)) {
            throw new EntityNotFoundException("Попытка исключить из друзей пользователя с несуществующим id");
        }
        if (!friendshipDAO.isUserAlreadyInFriends(userId, friendId)) {
            throw new ReFriendException("Попытка исключить из друзей пользователя не находящегося в списке друзей");
        }
        friendshipDAO.excludeFromFriends(userId, friendId);
    }

}
