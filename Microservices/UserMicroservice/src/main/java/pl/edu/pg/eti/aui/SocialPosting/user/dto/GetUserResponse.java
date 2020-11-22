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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {
	private String email;
	private String name;
	private String surname;
	private LocalDate birthDate;
	private List<String> followedUsersEmails;

	public static Function<User, GetUserResponse> entityToDtoMapper() {
		return user -> {
			Collection<User> followed = user.getFollowedUsers();
			List<String> followedEmails = new ArrayList<>();
			followed.forEach(user1 -> followedEmails.add(user1.getEmail()));
			return GetUserResponse.builder()
					.email(user.getEmail())
					.name(user.getName())
					.surname(user.getSurname())
					.birthDate(user.getBirthDate())
					.followedUsersEmails(followedEmails)
					.build();
		};
	}
}
