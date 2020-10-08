package pl.edu.pg.eti.aui.SocialPosting.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.aui.SocialPosting.post.repository.PostRepository;

@Service
public class PostService {
	private PostRepository postRepository;

	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
}
