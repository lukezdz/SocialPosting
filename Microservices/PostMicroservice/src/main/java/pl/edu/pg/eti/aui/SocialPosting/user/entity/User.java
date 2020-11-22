package pl.edu.pg.eti.aui.SocialPosting.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@Entity()
@Table(name = "users")
public class User implements Serializable {
	@Getter
	@Setter
	@Id
	private String email;

	@Getter
	@Setter
	@OneToMany
	@ToString.Exclude
	private List<Post> myPosts;
}
