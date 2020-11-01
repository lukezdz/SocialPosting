package pl.edu.pg.eti.aui.SocialPosting.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.aui.SocialPosting.post.service.PostService;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private UserRepository userRepository;
	private PostService postService;

	@Autowired
	public UserService(UserRepository userRepository, PostService postService) {
		this.userRepository = userRepository;
		this.postService = postService;
	}

	@Transactional
	public void add(User user) {
		user.setFollowedUsers(new HashSet<>());
		userRepository.save(user);
	}

	@Transactional
	public void add(User user, String password) {
		user.setFollowedUsers(new HashSet<>());
		user.setPassword(password);
		userRepository.save(user);
	}

	public User createAccount(String email, String name, String surname, String password, LocalDate birthDate) {
		User user = User.builder().email(email).name(name).surname(surname).birthDate(birthDate).build();
		add(user, password);
		return user;
	}

	@Transactional
	public void update(User user) {
		userRepository.save(user);
	}

	@Transactional
	public void deleteAccount(String email) {
		find(email).ifPresentOrElse(user -> {
			postService.findByUser(user).forEach(post -> {
				String id = post.getId();
				postService.delete(id, email);
			});
			userRepository.delete(user);
			}, () -> System.err.println("Something went wrong"));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public Optional<User> find(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	public void follow(String myEmail, String toFollowEmail) {
		find(myEmail).ifPresentOrElse(me -> {
			find(toFollowEmail).ifPresentOrElse(toFollow -> {
				me.getFollowedUsers().add(toFollow);
				userRepository.save(me);
				System.out.printf("%s %s has been followed!%n", toFollow.getName(), toFollow.getSurname());
			}, () -> {
				System.err.println("Couldn't find user to follow.");
			});
		}, () -> {
			System.err.println("Something went wrong.");
		});
	}

	@Transactional
	public void unfollow(String myEmail, String toUnfollowEmail) {
		find(myEmail).ifPresentOrElse(me -> {
			find(toUnfollowEmail).ifPresentOrElse(toUnfollow -> {
				me.getFollowedUsers().remove(toUnfollow);
				userRepository.save(me);
				System.out.printf("%s %s has been unfollowed.%n", toUnfollow.getName(), toUnfollow.getSurname());
			}, () -> {
				System.err.println("Given user doesn't exist or isn't followed by you.");
			});
		}, () -> {
			System.err.println("Something went wrong.");
		});
	}
}
