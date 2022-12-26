package ru.yandex.practicum.filmrate.model;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	private long eventId;
	private long timestamp;
	private EventType eventType;
	private EventOperation operation;
	private long userId;
	private long entityId;
}