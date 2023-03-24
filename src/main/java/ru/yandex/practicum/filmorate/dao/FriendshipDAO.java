package ru.yandex.practicum.filmorate.dao;

public interface FriendshipDAO {

    void addFriend(Integer userWhoSendId, Integer userWhomSendId);

    void excludeFromFriends(Integer currentUserId, Integer excludingFriendId);

    boolean isUserAlreadyInFriends(Integer userWhoSendId, Integer userWhomSendId);
}
