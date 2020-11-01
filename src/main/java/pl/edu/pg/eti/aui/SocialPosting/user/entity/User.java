package pl.edu.pg.eti.aui.SocialPosting.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


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
	private String name;

	@Getter
	@Setter
	private String surname;

	@Getter
	@Setter
	@Id
	private String email;

	@Getter
	@Setter
	private LocalDate birthDate;

	@Getter
	@Setter
	@OneToMany
	@ToString.Exclude
	private List<Post> myPosts;

	@Getter
	@Setter
	@ManyToMany
	@ToString.Exclude
	private Set<User> followedUsers;

	@ToString.Exclude
	private String password;

	public void setPassword(String password) {
		this.password = DigestUtils.sha256Hex(password);
	}

	public boolean checkPassword(String password) {
		return this.password.equals(DigestUtils.sha256Hex(password));
	}

	public String basicInfo() {
		return String.format("%s %s, %s", getName(), getSurname(), getEmail());
	}
}
