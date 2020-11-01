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

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetPostResponse {
	private String id;
	private String content;
	private LocalDateTime creationTime;
	private String authorEmail;

	public static Function<Post, GetPostResponse> entityToDtoMapper() {
		return post -> GetPostResponse.builder()
				.id(post.getId())
				.content(post.getContent())
				.creationTime(post.getCreationTime())
				.authorEmail(post.getAuthor().getEmail())
				.build();
	}
}
