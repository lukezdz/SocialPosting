package pl.edu.pg.eti.aui.SocialPosting.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateUserRequest {

	private String email;
	private String name;
	private String surname;
	private LocalDate birthDate;
	private String password;

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@ToString
	@EqualsAndHashCode
	public static class UserConfig {
		private String email;
		private String name;
		private String surname;
		private LocalDate birthDate;
		private String password;
	}

	public static Function<CreateUserRequest, UserConfig> dtoToEntityMapper() {
		return request -> UserConfig.builder()
				.email(request.getEmail())
				.name(request.getName())
				.surname(request.getSurname())
				.birthDate(request.getBirthDate())
				.password(request.getPassword())
				.build();
	}
}
