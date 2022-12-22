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
	private boolean isPositive;
	private Integer userId;
	private Integer filmId;
	private Integer useful;
}