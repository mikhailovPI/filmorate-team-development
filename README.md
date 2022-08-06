# Сервис для работы с фильмами

**Данная программа позволяет оценивать фильмы и получать топ-фильмов по запросу**

Модель фильма включает в себя:
1. Название фильма
2. Описание фильма
3. Продолжительность фильма
4. Дату релиза
5. Количество оценок от пользователя
6. Жанр фильма
7. Возрастное ограничение фильма

Модель пользователя включает в себя:
1. Логин пользователя
2. Имя пользователя
3. Email пользователя
4. Дату рождения
5. Список друзей

Данные будут храниться в БД. Схема БД представлена ниже.
https://drawsql.app/yandex-7/diagrams/filmorate/embed
![This is an image](E:\Yandex\Project_practicum\java-filmorate\java-filmorate\filmorate)

Примеры Endpoint запросов (программа написана на Java):

```java

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }
....
    @PutMapping(value = "/films/{id}/like/{userId}")
    public Integer putLikeFilm(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long userId) {
        return filmService.putLike(id, userId);
    }
}
```
