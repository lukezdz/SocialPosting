package pl.edu.pg.eti.aui.SocialPosting.user.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
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
		find(email).ifPresentOrElse(
				userRepository::delete,
				() -> System.err.println("Something went wrong"));
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

	@Transactional
	public void updateUserProfilePicture(User user, InputStream pictureStream) throws IOException {
		BufferedImage image = ImageIO.read(pictureStream);
		String filepath = resolvePicturePath(user.getEmail()) + ".png";
		ImageIO.write(image, "png", new File(filepath));
		user.setProfilePicturePath(filepath);
		user.setProfilePictureUploadTime(LocalDateTime.now());
	}

	public byte[] getUserProfilePicture(User user) throws IOException {
		String filepath = user.getProfilePicturePath() != null ? user.getProfilePicturePath() : "/image/default.png";
		InputStream is = new FileInputStream(filepath);

		byte[] target = new byte[is.available()];
		is.read(target);

		return target;
	}

	private String resolvePicturePath(String email) {
		String[] split = email.split("@");
		String[] domain = split[1].split(".");

		StringBuilder finalPath = new StringBuilder();
		for (int i = domain.length - 1; i >= 0; i--) {
			finalPath.append(domain[i]);
		}

		finalPath.append(split[0]);
		finalPath.append(DigestUtils.sha256Hex(LocalDateTime.now().toString()));

		return finalPath.toString();
	}
}
