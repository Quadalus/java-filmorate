package ru.yandex.practicum.filmrate.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Directory {
	private Integer id;
	private String name;
}