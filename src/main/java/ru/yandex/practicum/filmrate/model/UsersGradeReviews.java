package ru.yandex.practicum.filmrate.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersGradeReviews {
	private Integer userId;
	private Integer review_id;
	private Integer grade;

	public Integer getGrade() {
		if (grade == null) {
			return 0;
		} else return grade;
	}
}