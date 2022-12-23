package ru.yandex.practicum.filmrate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	private Integer reviewId;
	@NotNull
	@NotBlank
	private String content;
	@NotNull
	private Boolean isPositive;
	private Integer userId;
	private Integer filmId;
	private Integer useful;

	public Integer getUseful() {
		if (useful == null) {
			return 0;
		} else return useful;
	}
}