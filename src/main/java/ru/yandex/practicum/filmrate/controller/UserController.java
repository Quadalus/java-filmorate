package ru.yandex.practicum.filmrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmrate.model.Event;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.User;
import ru.yandex.practicum.filmrate.service.FeedService;
import ru.yandex.practicum.filmrate.service.UserService;
import ru.yandex.practicum.filmrate.service.ValidationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final ValidationService validationService;
	private final FeedService feedService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<User> findAllUsers() {
		log.info("Список пользователей получен.");
		return userService.findAllUser();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User findUser(@PathVariable int id) {
		User foundUser = userService.findUserById(id);
		log.info("Пользователь с id={} получен.", foundUser.getId());
		return foundUser;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User addUser(@Valid @RequestBody User user) {
		validationService.validateUser(user);
		User addedUser = userService.addUser(user);
		log.info("Пользователь с id={} был добавлен.", addedUser.getId());
		return addedUser;
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public User updateOrAddUser(@Valid @RequestBody User user) {
		validationService.validateUser(user);
		User updatedUser = userService.updateUser(user);
		log.info("Пользователь с id={} обновлён.", updatedUser.getId());
		return updatedUser;
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAllUsers() {
		userService.deleteAllUsers();
		log.info("Все пользователи были удалены.");
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable int id) {
		userService.deleteUserById(id);
		log.info("Пользователь c id={} был удалён.", id);
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	@ResponseStatus(HttpStatus.OK)
	public List<User> getCommonFriend(@PathVariable int id,
	                                  @PathVariable int otherId) {
		log.info("Список общих друзей получен.");
		return userService.getMutualFriends(id, otherId);
	}

	@GetMapping("/{id}/friends")
	@ResponseStatus(HttpStatus.OK)
	public List<User> getUserFriends(@PathVariable int id) {
		log.info("Список друзей получен.");
		return userService.getUserFriends(id);
	}

	@PutMapping("/{id}/friends/{friendId}")
	@ResponseStatus(HttpStatus.OK)
	public void addFriend(@PathVariable int id,
	                      @PathVariable int friendId) {
		log.info("Пользователь с id={} добавил в друзья пользователя с id={}", id, friendId);
		userService.addFriend(id, friendId);
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFriend(@PathVariable int id,
	                         @PathVariable int friendId) {
		log.info("Пользователь с id={} удалил из друзей пользователя с id={}", id, friendId);
		userService.deleteFriend(id, friendId);
	}

	@GetMapping("/{id}/recommendations")
	@ResponseStatus(HttpStatus.OK)
	public List<Film> getRecommendedFilms(@PathVariable(name = "id") int userId) {
		log.info("Пользователь с id={} получил список рекомендованных фильмов", userId);
		return userService.getRecommendedFilms(userId);
	}

	@GetMapping("/{id}/feed")
	@ResponseStatus(HttpStatus.OK)
	public List<Event> getUserEvents(@PathVariable int id) {
		log.info("Лента событий для пользователя с id={} получена", id);
		return feedService.getUserEvents(id);
	}
}