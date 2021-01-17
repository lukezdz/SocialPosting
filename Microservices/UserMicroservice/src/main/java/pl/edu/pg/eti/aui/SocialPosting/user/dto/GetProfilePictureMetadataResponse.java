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

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetProfilePictureMetadataResponse {
	private String description;
	private LocalDateTime profilePictureUploadTime;

	public static Function<User, GetProfilePictureMetadataResponse> entityToDtoMapper() {
		return user -> GetProfilePictureMetadataResponse.builder()
				.description(user.getProfilePictureDescription())
				.profilePictureUploadTime(user.getProfilePictureUploadTime())
				.build();
	}
}
