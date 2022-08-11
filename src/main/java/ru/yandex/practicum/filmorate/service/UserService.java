package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsDaoStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDaoStorage userDaoStorage;
    private final FriendsDaoStorage friendsDaoStorage;

    public List<User> getAllUser() {
        return userDaoStorage.getAllUser();
    }

    public User getUserById(Long id) {
        return userDaoStorage.getUserById(id);
    }

    public User createUser(User user) throws ValidationException {
        return userDaoStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userDaoStorage.updateUser(user);
    }

    public void removeUser(User user) throws ValidationException {
        userDaoStorage.deleteUser(user);
    }

    public void addFriends(Long id, Long friendId) {
        friendsDaoStorage.addFriend(id, friendId);
    }

    public void removeFriends(Long id, Long friendId) {
        friendsDaoStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriendsUser(Long id) {
        return friendsDaoStorage.getAllFriendsUser(id);
    }

    public List<User> getCommonFriends(Long id, Long friendId) {
        List<User> user = getAllFriendsUser(id);
        List<User> otherUser = getAllFriendsUser(friendId);
        return user.stream()
                .filter(otherUser::contains)
                .collect(Collectors.toList());
    }
}