package pl.edu.pg.eti.aui.SocialPosting.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class User implements Serializable {
	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String surname;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private LocalDate birthDate;

	@Getter
	@Setter
	private List<String> followedUsersEmails;

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
