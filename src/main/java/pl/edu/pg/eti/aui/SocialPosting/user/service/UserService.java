package pl.edu.pg.eti.aui.SocialPosting.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.aui.SocialPosting.user.repository.UserRepository;

@Service
public class UserService {
	private UserRepository repository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.repository = userRepository;
	}


}
