package ru.yandex.practicum.filmrate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ErrorResponse {
	private final String error;
	private final String description;
}
