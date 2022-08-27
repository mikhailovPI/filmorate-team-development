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
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
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

    @GetMapping()
    public List<User> getUser() {
        return userService.getAllUser();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping(value = "/{id}")
    public void removeUser(@Valid @RequestBody @PathVariable Long id) {
        userService.removeUser(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriends(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFriends(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        userService.removeFriends(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getAllFriendsUser(@PathVariable Long id) {
        return userService.getAllFriendsUser(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonListFriends(
            @PathVariable Long id,
            @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping(value = "/{userId}/feed")
    public Collection<Feed> getUserFeed(@PathVariable Long userId) {
        return userService.getUserFeed(userId);
    }

    @GetMapping(value = "/{id}/recommendations")
    public List<Film> findRecommendedFilms(@PathVariable Long id) {
        return filmService.findRecommendedFilms(id);
    }
}