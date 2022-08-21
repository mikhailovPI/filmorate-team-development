package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

    @Autowired
    public UserController(UserService userService, FilmService filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    @GetMapping("/users")
    public List<User> getUser() {
        return userService.getAllUser();
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

    @DeleteMapping(value = "/users/{id}")
    public void removeUser(@Valid @RequestBody @PathVariable Long id) {
        userService.removeUser(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriends(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriends(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long friendId) {
        userService.removeFriends(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getAllFriendsUser(@PathVariable @Min(1) Long id) {
        return userService.getAllFriendsUser(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getCommonListFriends(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping(value = "/users/{userId}/feed")
    public Collection<Feed> getUserFeed(@PathVariable @Min(1) Long userId) {
        return userService.getUserFeed(userId);
    }

    @GetMapping(value = "/users/{id}/recommendations")
    public List<Film> findRecommendedFilms(@PathVariable Long id) {
        return filmService.findRecommendedFilms(id);
    }
}