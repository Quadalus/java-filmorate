package ru.yandex.practicum.filmrate.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmrate.controller.FilmController;
import ru.yandex.practicum.filmrate.controller.ReviewController;
import ru.yandex.practicum.filmrate.controller.UserController;
import ru.yandex.practicum.filmrate.model.Film;
import ru.yandex.practicum.filmrate.model.Mpa;
import ru.yandex.practicum.filmrate.model.Review;
import ru.yandex.practicum.filmrate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewControllerTest {
	private final FilmController filmController;
	private final UserController userController;
	private final ReviewController reviewController;

	@Autowired
	public ReviewControllerTest(FilmController filmController, UserController userController, ReviewController reviewController) {
		this.filmController = filmController;
		this.userController = userController;
		this.reviewController = reviewController;
	}

	Film film = Film.builder()
			.name("fsgsvsf")
			.description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
					" Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу.")
			.releaseDate(LocalDate.of(2001, 12, 19))
			.duration(178)
			.mpa(new Mpa(1, "Комедия"))
			.build();

	Film film1 = Film.builder()
			.name("ytrdygsrg")
			.description("«Сказания о Средиземье — это хроника Великой войны за Кольцо, длившейся не одну тысячу лет." +
					" Тот, кто владел Кольцом, получал неограниченную власть, но был обязан служить злу.")
			.releaseDate(LocalDate.of(2001, 12, 19))
			.duration(178)
			.mpa(new Mpa(1, "Комедия"))
			.build();

	User user = User.builder()
			.email("amaemasdfwil@fff.com")
			.login("nick")
			.name("name")
			.birthday(LocalDate.of(1990, 10, 4))
			.build();

	User user1 = User.builder()
			.email("amaecdsmail@fff.com")
			.login("nick")
			.name("name")
			.birthday(LocalDate.of(1990, 10, 4))
			.build();

	@Test
	public void checkAddReview() {
		User user3 = userController.addUser(user);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		assertEquals(1, reviewController.getReviewsForFilm(0, 10).size());
		assertEquals(newReview.getContent(), reviewController.getReviewById(newReview.getReviewId()).getContent());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkUpdateReview() {
		User user3 = userController.addUser(user);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		reviewController.updateReview(Review.builder()
				.reviewId(newReview.getReviewId())
				.content("Update")
				.isPositive(false)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		assertEquals("Update", reviewController.getReviewById(newReview.getReviewId()).getContent());
		assertEquals(false, reviewController.getReviewById(newReview.getReviewId()).getIsPositive());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkDeleteReview() {
		User user3 = userController.addUser(user);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		assertEquals(1, reviewController.getReviewsForFilm(0, 10).size());
		reviewController.deleteReview(newReview.getReviewId());
		assertEquals(0, reviewController.getReviewsForFilm(0, 10).size());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkGetReviewById() {
		User user3 = userController.addUser(user);
		Film film3 = filmController.addFilm(film);
		Review newReview1 = Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build();
		Review newReview = reviewController.addReview(newReview1);
		newReview1.setReviewId(newReview.getReviewId());
		assertEquals(newReview1, newReview);
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkGetReviewsForFilm() {
		User user3 = userController.addUser(user);
		Film film3 = filmController.addFilm(film);
		Film film4 = filmController.addFilm(film1);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review1")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		Review newReview1 = reviewController.addReview(Review.builder()
				.content("New Review2")
				.isPositive(false)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		Review newReview2 = reviewController.addReview(Review.builder()
				.content("New Review3")
				.isPositive(false)
				.userId(user3.getId())
				.filmId(film4.getId()).build());
		assertEquals(1, reviewController.getReviewsForFilm(film4.getId(), 10).size());
		assertEquals(2, reviewController.getReviewsForFilm(film3.getId(), 10).size());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkAddLikeReview() {
		User user3 = userController.addUser(user);
		User user4 = userController.addUser(user1);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		reviewController.addLikeReview(newReview.getReviewId(), user4.getId());
		assertEquals(1, reviewController.getReviewById(newReview.getReviewId()).getUseful());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkAddDislikeReview() {
		User user3 = userController.addUser(user);
		User user4 = userController.addUser(user1);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		reviewController.addDislikeReview(newReview.getReviewId(), user4.getId());
		assertEquals(-1, reviewController.getReviewById(newReview.getReviewId()).getUseful());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkDeleteLikeReview() {
		User user3 = userController.addUser(user);
		User user4 = userController.addUser(user1);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		reviewController.addLikeReview(newReview.getReviewId(), user4.getId());
		assertEquals(1, reviewController.getReviewById(newReview.getReviewId()).getUseful());
		reviewController.deleteLikeReview(newReview.getReviewId(), user4.getId());
		assertEquals(0, reviewController.getReviewById(newReview.getReviewId()).getUseful());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}

	@Test
	public void checkDeleteDislikeReview() {
		User user3 = userController.addUser(user);
		User user4 = userController.addUser(user1);
		Film film3 = filmController.addFilm(film);
		Review newReview = reviewController.addReview(Review.builder()
				.content("New Review")
				.isPositive(true)
				.userId(user3.getId())
				.filmId(film3.getId()).build());
		reviewController.addDislikeReview(newReview.getReviewId(), user4.getId());
		assertEquals(-1, reviewController.getReviewById(newReview.getReviewId()).getUseful());
		reviewController.deleteDislikeReview(newReview.getReviewId(), user4.getId());
		assertEquals(0, reviewController.getReviewById(newReview.getReviewId()).getUseful());
		userController.deleteAllUsers();
		filmController.deleteAllFilms();
	}
}
