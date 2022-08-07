package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserDaoStorage userDaoStorage;

    @Autowired
    public UserService(UserDaoStorage userDaoStorage) {
        this.userDaoStorage = userDaoStorage;
    }

    public List<User> getAllUser() {
        return userDaoStorage.getAllUser();
    }

    public User getUserById(Long userId) {
        return userDaoStorage.getUserById(userId);
    }

    public User createUser(User user) throws ValidationException {
        if (user == null) {
            throw new EntityNotFoundException("Передан пустой пользователь.");
        }
        return userDaoStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        if (userDaoStorage.getUserById(user.getUserId()) == null) {
            throw new EntityNotFoundException("Пользователь не найден для обновления.");
        }
        return userDaoStorage.updateUser(user);
    }

    public void removeUser(User user) throws ValidationException {
        if (userDaoStorage.getUserById(user.getUserId()) == null) {
            throw new EntityNotFoundException("Пользователь не найден для удаления.");
        }
        userDaoStorage.deleteUser(user);
    }

/*    public void addFriends(Long id, Long friendId) {
        if (!userDaoStorage.getAllUser().contains(getUserById(id)) ||
                !userDaoStorage.getAllUser().contains(getUserById(friendId))) {
            throw new EntityNotFoundException("Пользователя не добавить в друзья, т.к. его не существует.");
        }
        getUserById(id).getFriends().add(friendId);
        getUserById(friendId).getFriends().add(id);
    }

    public void removeFriends(Long id, Long friendId) {
        if (!userDaoStorage.getAllUser().contains(getUserById(id)) ||
                !userDaoStorage.getAllUser().contains(getUserById(friendId))) {
            throw new EntityNotFoundException("Пользователя не удалить из друзьей, т.к. его не существует.");
        }
        getUserById(id).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> userFriends1 = userDaoStorage.getUserById(id).getFriends();
        Set<Long> userFriends2 = userDaoStorage.getUserById(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();

        for (Long id1 : userFriends1) {
            for (Long id2 : userFriends2) {
                if (id1.equals(id2)) {
                    commonFriends.add(userDaoStorage.getUserById(id1));
                }
            }
        }
        return commonFriends;
    }

    public List<User> getListFriends(Long id) {
        List<User> userFriendsList = new ArrayList<>();

        for (Long id1 : userDaoStorage.getUserById(id).getFriends()) {
            userFriendsList.add(userDaoStorage.getUserById(id1));
        }
        return userFriendsList;
    }*/
}