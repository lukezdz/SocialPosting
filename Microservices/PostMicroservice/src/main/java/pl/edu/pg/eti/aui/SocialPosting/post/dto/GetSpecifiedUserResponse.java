package pl.edu.pg.eti.aui.SocialPosting.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GetSpecifiedUserResponse implements Serializable {
	String email;
	String name;
	String surname;
	LocalDate birthDate;
	List<String> followedUsersEmails;
}
