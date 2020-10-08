package pl.edu.pg.eti.aui.SocialPosting.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User implements Serializable {
	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String surname;

	@Getter
	@Setter
	private String login;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private Date birthDate;

	@ToString.Exclude
	private String password;

	@Getter
	@Setter
	private Set<Post> posts;

	public void setPassword(String password) {
		this.password = DigestUtils.sha256Hex(password);
	}

	public boolean checkPassword(String password) {
		return this.password.equals(DigestUtils.sha256Hex(password));
	}
}
