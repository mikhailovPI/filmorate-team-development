package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDaoStorage {

    User getUserById(Long id);

    List<User> getAllUser();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User saveUser(User user);

}