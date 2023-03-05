package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserDAO {

    Collection<User> getUsers();
    User addUser(User user);
    User updateUserInfo(User user);
    boolean isUserExists(Integer userId);
    User getUserById(Integer userId);
    Collection<User> getUserFriends(Integer userId);
    Collection<User> getCommonFriends(Integer userId, Integer otherId);

}
