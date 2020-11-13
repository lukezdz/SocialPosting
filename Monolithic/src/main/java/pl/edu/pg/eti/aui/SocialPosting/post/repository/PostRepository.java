package pl.edu.pg.eti.aui.SocialPosting.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

	Optional<Post> findById(String id);

	List<Post> findAll();
}
