package pl.edu.pg.eti.aui.SocialPosting.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;

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
public class GetUsersResponse {

	@Singular
	private List<String> userEmails;

	public static Function<Collection<User>, GetUsersResponse> entityToDtoMapper() {
		return users -> {
			GetUsersResponseBuilder builder = GetUsersResponse.builder();
			users.stream()
					.map(User::getEmail)
					.forEach(builder::userEmail);
			return builder.build();
		};
	}
}
