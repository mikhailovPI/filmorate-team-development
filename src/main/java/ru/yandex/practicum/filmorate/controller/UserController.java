package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
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
    public User getUserId(@PathVariable Long id) {
        return userService.getUserId(id);
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
            @PathVariable Long id,
            @PathVariable Long friendsId) {
        userService.addFriends(userService.getUserId(id), userService.getUserId(friendsId));
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendsId}")
    public void removeFriends(
            @PathVariable Long id,
            @PathVariable Long friendsId) {
        userService.removeFriends(userService.getUserId(id), userService.getUserId(friendsId));
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getListFriends(@PathVariable Long id) {
        return userService.getListFriends(id);

    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> addCommonListFriends(
            @PathVariable Long id,
            @PathVariable Long otherId) {
        return userService.getCommonFriends(userService.getUserId(id), userService.getUserId(otherId));
    }
}