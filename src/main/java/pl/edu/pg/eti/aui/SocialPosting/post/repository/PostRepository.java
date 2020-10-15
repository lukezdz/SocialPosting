package pl.edu.pg.eti.aui.SocialPosting.post.repository;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pg.eti.aui.SocialPosting.datastore.DataStore;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;
import pl.edu.pg.eti.aui.SocialPosting.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class PostRepository implements Repository<Post, String> {
	private DataStore dataStore;

	@Autowired
	public PostRepository(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	@Override
	public Optional<Post> find(String id) {
		return dataStore.getPost(id);
	}

	@Override
	public List<Post> findAll() {
		return dataStore.getPosts();
	}

	@Override
	public void add(Post post) {
		dataStore.addPost(post);
	}

	@Override
	public void delete(Post post) {
		dataStore.deletePost(post);
	}
}
