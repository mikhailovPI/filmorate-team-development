package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> get() {
        return userStorage.getUser();
    }

    public User create(User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    public User update(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public User delete(User user) throws ValidationException {
        return userStorage.deleteUser(user);
    }

    public void addFriends() {

    }
}