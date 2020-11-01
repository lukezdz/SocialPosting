package pl.edu.pg.eti.aui.SocialPosting.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreatePostRequest {
	private String content;
	private String authorsEmail;

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@ToString
	@EqualsAndHashCode
	public static class Post {
		private String content;
		private User user;
	}

	public static Function<CreatePostRequest, Post> dtoToEntityMapper(UserService userService) {
		return request -> {
			Optional<User> user = userService.find(request.getAuthorsEmail());
			if (user.isPresent()) {
				return Post.builder().content(request.getContent()).user(user.get()).build();
			} else {
				return null;
			}
		};
	}
}
