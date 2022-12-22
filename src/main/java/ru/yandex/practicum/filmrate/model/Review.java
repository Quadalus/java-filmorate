package ru.yandex.practicum.filmrate.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	private Integer id;
	private String content;
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