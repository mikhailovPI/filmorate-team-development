package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

import java.util.Collection;

public interface FeedDaoStorage {

    void addFeed(long userId, long entityId, EventType eventType, OperationType operationType);

    Collection<Feed> getUserFeed(long userId);
}
