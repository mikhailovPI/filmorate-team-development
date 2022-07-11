package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private InMemoryUserStorage userStorage;
    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping("/users")
    public List<User> get() {
        return userStorage.getUser();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return  userStorage.createUser(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    @DeleteMapping(value = "/users")
    public User delete (@Valid @RequestBody User user) throws ValidationException {
        return userStorage.deleteUser(user);
    }
}