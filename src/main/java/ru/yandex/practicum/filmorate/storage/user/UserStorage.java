package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;


public interface UserStorage {

    Collection<User> getUsers();
    User addUser(User user);
    User updateUserInfo(User user);
    boolean isUserExists(Integer userId);
    void addFriend(String userId, String friendId);
    void excludeFromFriends(String userId, String friendId);
    User getUserById(Integer userId);
    Collection<User> getUserFriends(Integer userId);
    Collection<User> getCommonFriends(Integer userId, Integer otherId);
    boolean isUserAlreadyInFriends(String userId, String friendId);
}
