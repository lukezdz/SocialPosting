package pl.edu.pg.eti.aui.SocialPosting.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class LoginRequest {
	private String email;
	private String password;
}
