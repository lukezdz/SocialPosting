package pl.edu.pg.eti.aui.SocialPosting.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;

import java.util.function.BiFunction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateProfilePictureDescriptionRequest {
	private String description;

	public static BiFunction<User, UpdateProfilePictureDescriptionRequest, User> dtoToEntityMapper() {
		return (user, request) -> {
			if (request.description != null) {
				user.setProfilePictureDescription(request.description);
			}
			return user;
		};
	}
}
