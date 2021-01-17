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
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@Entity()
@Table(name = "users")
public class User implements Serializable {
	@ToString.Exclude
	@Value("${socialposting.deployment.image.location}")
	private String basePath;

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
	@ManyToMany
	@ToString.Exclude
	private Set<User> followedUsers;

	@ToString.Exclude
	private String password;

	@Getter
	@ToString.Exclude
	private String profilePicturePath;

	@Getter
	@Setter
	@ToString.Exclude
	private LocalDateTime profilePictureUploadTime;

	public void setPassword(String password) {
		this.password = DigestUtils.sha256Hex(password);
	}

	public boolean checkPassword(String password) {
		return this.password.equals(DigestUtils.sha256Hex(password));
	}

	public void setProfilePicturePath(String path) {
		profilePicturePath = basePath + path;
	}

	public String basicInfo() {
		return String.format("%s %s, %s", getName(), getSurname(), getEmail());
	}
}
