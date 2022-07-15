package ru.yandex.practicum.filmorate.service;

import com.sun.source.doctree.SeeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

    public User getUserId(Long userId) {
        if (userId<0) {
            throw new NotFoundException("Введен не верный id = " + userId);
        }
        return userStorage.getUserId(userId);
    }

    public User createUser(User user) throws ValidationException {
        if (user == null) {
            throw new NotFoundException("Передан пустой пользователь.");
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        if (userStorage.getUserId(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден для обновления.");
        }
        return userStorage.updateUser(user);
    }

    public void removeUser(User user) throws ValidationException {
        if (userStorage.getUserId(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден для удаления.");
        }
        userStorage.deleteUser(user);
    }

    public void addFriends(User user1, User user2) {
        if (!userStorage.getUser().contains(user1) || !userStorage.getUser().contains(user2)) {
            throw new NotFoundException("Пользователя не добавить в друзья, т.к. его не существует.");
        }
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
    }

    public void removeFriends(User user1, User user2) {
        if (!userStorage.getUser().contains(user1) || !userStorage.getUser().contains(user2)) {
            throw new NotFoundException("Пользователя не удалить из друзьей, т.к. его не существует.");
        }
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    public List<User> getCommonFriends(User user1, User user2) {
        List<User> commonFriends = new ArrayList<>();
        for (Long id1 : user1.getFriends()) {
            for (Long id2 : user2.getFriends()) {
                if (id1.equals(id2)) {
                    commonFriends.add(user1);
                }
            }
        }
        int size = commonFriends.size();
        return commonFriends;
    }

    public List<User> getListFriends(Long id) {
        List<User> userFriendsList = new ArrayList<>();
        for (Long idi: userStorage.getUserId(id).getFriends()) {
            userFriendsList.add(userStorage.getUserId(idi));
        }
        return userFriendsList;
    }
}