package pl.edu.pg.eti.aui.SocialPosting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class DataInitializer {
	private static final String DEFAULT_PASSWORD = "0000";
	private final UserService userService;

	@Autowired
	public DataInitializer(UserService userService) {
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		User markCucumber = User.builder()
				.email("mark.cucumber@feetbook.com")
				.name("Mark")
				.surname("Cucumber")
				.birthDate(LocalDate.of(1980, 1, 1))
				.build();
		User jeffCudos = User.builder()
				.email("jeff.cudos@borneo.com")
				.name("Jeff")
				.surname("Cudos")
				.birthDate(LocalDate.of(1980, 1, 1))
				.build();
		User billFences = User.builder()
				.email("bill.fences@megahard.com")
				.name("Bill")
				.surname("Fences")
				.birthDate(LocalDate.of(1980, 1, 1))
				.build();

		userService.add(markCucumber, DEFAULT_PASSWORD);
		userService.add(jeffCudos, DEFAULT_PASSWORD);
		userService.add(billFences, DEFAULT_PASSWORD);
	}
}
