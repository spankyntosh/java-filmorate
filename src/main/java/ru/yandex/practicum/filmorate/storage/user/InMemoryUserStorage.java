package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int idCounter = 1;
    Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(idCounter);
        idCounter++;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        if (user.getFriends() == null) {
            user.setFriends(users.get(user.getId()).getFriends());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean isUserExists(Integer userId) {
        return users.containsKey(userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void excludeFromFriends(Integer userId, Integer friendId) {
        users.get(userId).getFriends().remove(friendId);
    }

    @Override
    public User getUserById(Integer userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        return users.get(userId).getFriends()
                .stream()
                .map(friendId -> users.get(friendId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherUserId) {
        Set<Integer> userFriends = users.get(userId).getFriends();
        Set<Integer> otherUserFriends = users.get(otherUserId).getFriends();
        return userFriends.stream()
                .flatMap(userFriend -> otherUserFriends.stream().filter(userFriend::equals))
                .sorted(Integer::compareTo)
                .map(id -> users.get(id)).collect(Collectors.toList());

    }

    @Override
    public boolean isUserAlreadyInFriends(Integer userId, Integer friendId) {
        return users.get(userId).getFriends().contains(friendId);
    }

}
