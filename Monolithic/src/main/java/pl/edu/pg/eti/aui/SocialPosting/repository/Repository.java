package pl.edu.pg.eti.aui.SocialPosting.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<Entity, Key> {
	void add(Entity entity);

	void delete(Entity entity);

	List<Entity> findAll();

	Optional<Entity> find(Key key);
}
