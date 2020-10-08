package pl.edu.pg.eti.aui.SocialPosting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.aui.SocialPosting.user.repository.UserRepository;

@Component
public class CommandLineInterface implements CommandLineRunner {
	private UserRepository userRepository;

	@Autowired
	public CommandLineInterface(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.getAll().forEach(System.out::println);
	}
}
