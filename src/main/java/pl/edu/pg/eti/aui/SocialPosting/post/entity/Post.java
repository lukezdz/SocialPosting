package pl.edu.pg.eti.aui.SocialPosting.post.entity;

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

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Post {
	private String id;
	private String content;
	private LocalDateTime creationTime;
	private User author;

	public String presentContent() {
		return String.format("%s:\n%s\nid: %s\n%s", getAuthor().basicInfo(), creationTime.toString(), id, content);
	}
}
