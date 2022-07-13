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
        return userService.get();
    }

    @GetMapping (value = "/users/{id}")
    public User getUserId (@PathVariable long id) throws ValidationException {
        return userService.getUserId(id);
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.update(user);
    }

    @DeleteMapping(value = "/users")
    public User removeUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.delete(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendsId}")
    public void addFriends (@PathVariable long id, @PathVariable long friendsId ) {
        userService.addFriends(userService.getUserId(id), userService.getUserId(friendsId));
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendsId}")
    public void removeFriends(@PathVariable long id, @PathVariable long friendsId) {
        userService.removeFriends(userService.getUserId(id), userService.getUserId(friendsId));
    }

    @GetMapping(value = "/users/{id}/friends")
    public Set<Long> addListFriends (@PathVariable long id) {
        return userService.getUserId(id).getFriends();
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> addCommonListFriends (@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(userService.getUserId(id), userService.getUserId(otherId));
    }
}