# Сервис для работы с фильмами

**Данная программа позволяет оценивать фильмы и получать топ-фильмов по запросу**

Используемые стек: Java 11, Spring Boot, Maven, H2
Данный проект дорабатывался в команде.   

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

Примеры Endpoint запросов (программа написана на Java):

```java

    public List<Film> getTopFilmsGenreYear(Integer count, Integer genreId, Integer year) {
        List<Film> topFilms = new ArrayList<>();
        if (genreId == null && year == null) {
            for (Film film : filmDaoStorage.getTopLikeFilm(count)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } else if (year == null) {
            for (Film film : filmDaoStorage.getTopFilmsGenre(count, genreId)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } else if (genreId == null) {
            for (Film film : filmDaoStorage.getTopFilmsYear(count, year)) {
                loadData(film);
                topFilms.add(film);
            }
            return topFilms;
        } 
        ...
    }
```

Примеры запросов в БД (используемый язык: Java и SQL):

```java

    @Override
    public List<Film> getDirectorsFilmSortByRate(Integer directorId) {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.RELEASE_DATE, F.DESCRIPTION, F.DURATION, F.RATE," +
                "F.MPA_ID, M.MPA_NAME " +
                "FROM FILMS AS F " +
                "JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "LEFT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID " +
                "WHERE FD.DIRECTOR_ID = ? " +
                "ORDER BY F.RATE DESC";
        return getSortedFilmsList(directorId, sqlQuery);
    }
```
