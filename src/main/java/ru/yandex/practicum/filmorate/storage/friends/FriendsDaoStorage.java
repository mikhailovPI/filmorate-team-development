package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDaoStorage {

    List<User> getAllFriendsUser(Long userId);

    void addFriend(Long userId, Long friendId);

    void deleteFriend (Long userId, Long friendId);

}
