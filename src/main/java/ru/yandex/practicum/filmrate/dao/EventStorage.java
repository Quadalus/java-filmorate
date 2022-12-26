package ru.yandex.practicum.filmrate.dao;

import ru.yandex.practicum.filmrate.model.Event;

import java.util.List;

public interface EventStorage {
	List<Event> getUserEvents(int id);

	void addEvent(Event event);
}