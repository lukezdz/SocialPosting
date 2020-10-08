package pl.edu.pg.eti.aui.SocialPosting.datastore;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DataStore {
	private Set<User> users = new HashSet<>();
	private Set<Post> posts = new HashSet<>();

	public synchronized List<User> getUsers() {
		return new ArrayList<>(users);
	}

	public synchronized Optional<User> getUser(String login) {
		return users.stream()
				.filter(user -> user.getLogin().equals(login))
				.findFirst();
	}

	public synchronized void addUser(User user) {
		getUser(user.getLogin()).ifPresentOrElse(
				original -> {
					throw new IllegalArgumentException(
							String.format("User login \"%s\" is not unique", user.getLogin()));
				},
				() -> users.add(user));
	}

	public synchronized void deleteUser(User user) {
		users.remove(user);
	}

	public synchronized  List<Post> getPosts() {
		return new ArrayList<>(posts);
	}

	public synchronized Optional<Post> getPost(String id) {
		return posts.stream()
				.filter(post -> post.getId().equals(id))
				.findFirst();
	}

	public synchronized void addPost(Post post) {
		String contentHash = DigestUtils.sha256Hex(post.getContent());
		String id = DigestUtils.sha256Hex(String.format("%s %d", contentHash, Instant.now().toEpochMilli()));
		post.setId(id);
		posts.add(post);
	}

	public synchronized void deletePost(Post post) {
		posts.remove(post);
	}
}
