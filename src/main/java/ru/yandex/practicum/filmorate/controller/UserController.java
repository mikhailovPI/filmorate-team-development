package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private long id = 0;
    private Map<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.error("Такой пользователь уже существует.");
            throw new ValidationException("Такой пользователь уже существует.");
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.debug("Имя не указано. В качестве имени используется логин: " + user.getLogin() + ".");
            user.setName(user.getLogin());
        } else {
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("Создали пользователя: " + user.toString() + ".");
        }
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновили пользователя: " + user.toString() + ".");
        } else {
            log.error("Id пользователя не найдено.");
            throw new ValidationException("Id пользователя не найдено.");
        }
        return users.get(user.getId());
    }
}