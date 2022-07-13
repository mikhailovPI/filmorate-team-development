package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
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
                if (id1 == id2) {
                    commonFriends.add(user1);
                }
            }
        }
        return commonFriends;
    }

/*    public List<User> getFriends(User user1, User user2) {
        List<User> filteredList;
       return Stream.of(user1.getFriends())
                .filter(t -> t)
                .collect(Collectors.toList());
    }*/
}