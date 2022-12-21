package ru.yandex.practicum.filmrate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Director {
	public Director(String name) {
		this.name = name;
	}

	private Integer id;
	@NotBlank
	@NotNull
	private String name;
}
