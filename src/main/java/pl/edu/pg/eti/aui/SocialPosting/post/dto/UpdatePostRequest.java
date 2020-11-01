package pl.edu.pg.eti.aui.SocialPosting.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdatePostRequest {
	private String content;

	public static BiFunction<Post, UpdatePostRequest, Post> dtoToEntityMapper() {
		return (post, request) -> {
			post.setContent(request.getContent());
			return post;
		};
	}
}
