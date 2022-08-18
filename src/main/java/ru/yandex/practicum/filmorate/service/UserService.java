package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedDaoStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoStorage;
import ru.yandex.practicum.filmorate.storage.friends.FriendsDaoStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDaoStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDaoStorage userDaoStorage;
    private final FriendsDaoStorage friendsDaoStorage;
    private final FilmDaoStorage filmDaoStorage;
    private final FilmService filmService;

    private final FeedDaoStorage feedDaoStorage;

    public List<User> getAllUser() {
        return userDaoStorage.getAllUser();
    }

    public User getUserById(Long id) {
        return userDaoStorage.getUserById(id);
    }

    public User createUser(User user) throws ValidationException {
        return userDaoStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userDaoStorage.updateUser(user);
    }

    public void removeUser(Long id) throws ValidationException {
        userDaoStorage.deleteUser(id);
    }

    public void addFriends(Long userId, Long friendId) {
        friendsDaoStorage.addFriend(userId, friendId);
    }

    public void removeFriends(Long userId, Long friendId) {
        friendsDaoStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriendsUser(Long id) {
        if (userDaoStorage.getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с таким id не существует");
        }
        return friendsDaoStorage.getAllFriendsUser(id);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<User> user = getAllFriendsUser(userId);
        List<User> otherUser = getAllFriendsUser(friendId);
        return user.stream()
                .filter(otherUser::contains)
                .collect(Collectors.toList());
    }

    public Collection<Feed> getUserFeed(long userId) {
        return feedDaoStorage.getUserFeed(userId);
    }

    public List<Film> findRecommendedFilms(Long id) {
        HashMap<User, List<Film>> filmsTable = new HashMap<>();
        List<Film> userFilms = filmDaoStorage.findFilmsLikedByUser(id);
        List<User> users = getAllUser();

        users.remove(userDaoStorage.getUserById(id));

        for (User other : users) {
            List<Film> otherFilms = filmDaoStorage.findFilmsLikedByUser(other.getId());
            filmsTable.put(other, otherFilms);
        }

        List<List<Film>> differencesTable = new ArrayList<>();

        for (List<Film> value : filmsTable.values()) {
            List<Film> filmsPackage = new ArrayList<>();

            for (Film film : value) {
                film = filmService.getFilmById(film.getId());
                if (!userFilms.contains(film)) {
                    filmsPackage.add(film);
                }
            }
            differencesTable.add(filmsPackage);
        }

        differencesTable.removeIf(List::isEmpty);

        return differencesTable.stream()
                .min(Comparator.comparing(List<Film>::size))
                .orElse(new ArrayList<>());
    }
}