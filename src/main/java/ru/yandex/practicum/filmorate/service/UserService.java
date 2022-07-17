package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUser() {
        return userStorage.getUser();
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) throws ValidationException {
        if (user == null) {
            throw new NotFoundException("Передан пустой пользователь.");
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден для обновления.");
        }
        return userStorage.updateUser(user);
    }

    public void removeUser(User user) throws ValidationException {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден для удаления.");
        }
        userStorage.deleteUser(user);
    }

    public void addFriends(Long id, Long friendId) {
        if (!userStorage.getUser().contains(getUserById(id)) ||
                !userStorage.getUser().contains(getUserById(friendId))) {
            throw new NotFoundException("Пользователя не добавить в друзья, т.к. его не существует.");
        }
        getUserById(id).getFriends().add(friendId);
        getUserById(friendId).getFriends().add(id);
    }

    public void removeFriends(Long id, Long friendId) {
        if (!userStorage.getUser().contains(getUserById(id)) ||
                !userStorage.getUser().contains(getUserById(friendId))) {
            throw new NotFoundException("Пользователя не удалить из друзьей, т.к. его не существует.");
        }
        getUserById(id).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> userFriends1 = userStorage.getUserById(id).getFriends();
        Set<Long> userFriends2 = userStorage.getUserById(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();

        for (Long id1 : userFriends1) {
            for (Long id2 : userFriends2) {
                if (id1.equals(id2)) {
                    commonFriends.add(userStorage.getUserById(id1));
                }
            }
        }
        return commonFriends;
    }

    public List<User> getListFriends(Long id) {
        List<User> userFriendsList = new ArrayList<>();

        for (Long id1 : userStorage.getUserById(id).getFriends()) {
            userFriendsList.add(userStorage.getUserById(id1));
        }
        return userFriendsList;
    }

}