package pl.edu.pg.eti.aui.SocialPosting.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void add(User user) {
		user.setFollowedUsersEmails(new ArrayList<String>());
		userRepository.add(user);
	}

	public void add(User user, String password) {
		user.setFollowedUsersEmails(new ArrayList<String>());
		user.setPassword(password);
		userRepository.add(user);
	}

	public void createAccount(String email, String name, String surname, String password, LocalDate birthDate) {
		User user = User.builder().email(email).name(name).surname(surname).birthDate(birthDate).build();
		add(user, password);
	}

	public void deleteAccount(String email) {
		find(email).ifPresentOrElse(user -> userRepository.delete(user), () -> System.out.println("Something went wrong"));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> find(String email) {
		return userRepository.find(email);
	}

	public void follow(String myEmail, String toFollowEmail) {
		find(myEmail).ifPresentOrElse(me -> {
			find(toFollowEmail).ifPresentOrElse(toFollow -> {
				me.getFollowedUsersEmails().add(toFollow.getEmail());
				System.out.printf("%s %s has been followed!%n", toFollow.getName(), toFollow.getSurname());
			}, () -> {
				System.err.println("Couldn't find user to follow.");
			});
		}, () -> {
			System.err.println("Something went wrong.");
		});
	}

	public void unfollow(String myEmail, String toUnfollowEmail) {
		find(myEmail).ifPresentOrElse(me -> {
			find(toUnfollowEmail).ifPresentOrElse(toUnfollow -> {
				me.getFollowedUsersEmails().remove(toUnfollow.getEmail());
				System.out.printf("%s %s has been unfollowed.%n", toUnfollow.getName(), toUnfollow.getSurname());
			}, () -> {
				System.err.println("Given user doesn't exist or isn't followed by you.");
			});
		}, () -> {
			System.err.println("Something went wrong.");
		});
	}
}
