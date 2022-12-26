package ru.yandex.practicum.filmrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmrate.dao.EventStorage;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.exception.NotFoundException;
import ru.yandex.practicum.filmrate.model.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final EventStorage eventStorage;
	private final UserStorage userStorage;

	public List<Event> getUserEvents(int id) {
		if (userStorage.getUserById(Math.toIntExact(id)).isPresent()) {
			return eventStorage.getUserEvents(id);
		} else {
			throw new NotFoundException(String.format("Невозможно получить пользователя с id=%d", id));
		}
	}

	public void addEvent(Event event) {
		eventStorage.addEvent(event);
	}
}