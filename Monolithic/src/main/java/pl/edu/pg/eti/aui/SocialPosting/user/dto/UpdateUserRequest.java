package pl.edu.pg.eti.aui.SocialPosting.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;

import java.time.LocalDate;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateUserRequest {
	private String name;
	private String surname;
	private LocalDate birthDate;

	public static BiFunction<User, UpdateUserRequest, User> dtoToEntityMapper() {
		return (user, request) -> {
			if (request.getName() != null && !request.getName().equals("")) {
				user.setName(request.getName());
			}
			if (request.getSurname() != null && !request.getSurname().equals("")) {
				user.setSurname(request.getSurname());
			}
			if (request.getBirthDate() != null) {
				user.setBirthDate(request.getBirthDate());
			}
			return user;
		};
	}
}
