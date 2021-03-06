package pl.edu.pg.eti.aui.SocialPosting.post.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;
import pl.edu.pg.eti.aui.SocialPosting.post.repository.PostRepository;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
	private PostRepository postRepository;

	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public Optional<Post> find(String id) {
		return postRepository.findById(id);
	}

	public List<Post> findByUserEmail(String email) {
		return postRepository.findAll()
				.stream()
				.filter(post -> post.getAuthor().getEmail().equals(email))
				.collect(Collectors.toList());
	}

	public void add(String content, User user) {
		create(content, user);
	}

	@Transactional
	public Post create(String content, User user) {
		LocalDateTime now = LocalDateTime.now();
		String contentHash = DigestUtils.sha256Hex(content);
		String id = DigestUtils.sha256Hex(String.format("%s %s %s", user.getEmail(), contentHash, now.toString()));
		Post post = Post.builder()
				.author(user)
				.content(content)
				.creationTime(now)
				.id(id)
				.build();

		return postRepository.save(post);
	}

	@Transactional
	public void update(Post post) {
		postRepository.save(post);
	}

	@Transactional
	public void delete(String id, String authorsEmail) {
		find(id).ifPresentOrElse(post -> {
			if (post.getAuthor().getEmail().equals(authorsEmail)) {
				postRepository.delete(post);
				System.out.println("Deleted post.");
			}
			else {
				System.err.println("You cannot delete a post you didn't create!");
			}
		}, () -> {System.err.println("Something went wrong. No post of given id.");});
	}
}
