package ru.yandex.practicum.filmrate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Film {
	private Integer id;
	@NotBlank
	private String name;
	@NotBlank
	private String description;
	@NotNull
	private LocalDate releaseDate;
	@PositiveOrZero
	private Integer duration;
	private Integer rate;
	private Mpa mpa;
	private Set<Genre> genres = new LinkedHashSet<>();

	private Set<Director> directors = new LinkedHashSet<>();

	public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Integer rate, Mpa mpa) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.duration = duration;
		this.rate = rate;
		this.mpa = mpa;
	}

	public void setGenreToFilm(Genre genre) {
		genres.add(genre);
	}

	public void setDirectorsToFilm(Director director) {
		directors.add(director);
	}
}
