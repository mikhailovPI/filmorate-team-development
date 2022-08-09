package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDaoStorage {

    List<User> getListFriends(Long userId);

    List<User> getListCommonFriends(Long userId, Long friendId);

    void addFriend(Long userId, Long friendId);
    void deleteFriend (Long userId, Long friendId);

    List<User> saveListFriends(Long userId, Long friendId);


}
