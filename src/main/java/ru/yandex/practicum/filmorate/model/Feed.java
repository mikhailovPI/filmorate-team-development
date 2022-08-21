package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "eventId")
public class Feed {
    private Long eventId;
    private Long userId;
    private Long entityId;
    private EventType eventType;
    private OperationType operation;
    private Long timestamp;
}
