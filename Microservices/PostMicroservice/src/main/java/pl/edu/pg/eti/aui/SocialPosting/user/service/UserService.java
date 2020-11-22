package pl.edu.pg.eti.aui.SocialPosting.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void add(User user) {
		userRepository.save(user);
	}

	@Transactional
	public void update(User user) {
		userRepository.save(user);
	}

	@Transactional
	public void deleteAccount(String email) {
		find(email).ifPresentOrElse(user -> {
			userRepository.delete(user);
		}, () -> System.err.println("Something went wrong while deleting user"));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public Optional<User> find(String email) {
		return userRepository.findByEmail(email);
	}
}
