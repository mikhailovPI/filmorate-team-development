package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUser() {
        return userService.getUser();
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable @Min(1) Long id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping(value = "/users")
    public void removeUser(@Valid @RequestBody User user) {
        userService.removeUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendsId}")
    public void addFriends(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long friendsId) {
        userService.addFriends(id, friendsId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriends(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long friendId) {
        userService.removeFriends(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getListFriends(@Positive @PathVariable Long id) {
        return userService.getListFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> addCommonListFriends(
            @Min(1) @PathVariable Long id,
            @Min(1) @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}