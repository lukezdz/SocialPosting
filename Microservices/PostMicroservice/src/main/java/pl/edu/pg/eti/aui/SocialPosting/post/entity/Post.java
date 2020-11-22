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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "posts")
public class Post {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

	@Id
	private String id;
	private String content;
	private LocalDateTime creationTime;

	@ManyToOne
	@JoinColumn(name = "user")
	private User author;

	public String presentContent() {
		return String.format(
				"%s:\n%s\nid: %s\n%s\n", getAuthor().getEmail(), creationTime.format(FORMATTER), id, content
		);
	}
}
