package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private long id = 0;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Такой пользователь уже существует.");
        } else {
            if (user.getName().isBlank()) {
                log.debug("Имя не указано. В качестве имени используется логин.");
                user.setName(user.getLogin());
            }
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("Создали пользователя: {}.", user);
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновили пользователя: {}.", user);
            return user;
        } else {
            log.error("Id пользователя не найдено.");
            throw new ValidationException("Id пользователя не найдено.");
        }
    }

    @Override
    public void deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Такой пользователь уже существует.");
        } else {
            log.info("Удалили пользователя: {}.", user);

            users.remove(user.getId());
        }
    }

    @Override
    public List<User> getUser() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }
}