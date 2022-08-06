package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDaoStorage {

    public User createUser(User user);

    public User updateUser(User user);

    public void deleteUser(User user);

    public List<User> getAllUser();

    public User getUserById(Long id);
}