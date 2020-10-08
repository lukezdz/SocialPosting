package pl.edu.pg.eti.aui.SocialPosting.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<Entity, Key> {
	Optional<Entity> get(Key id);

	List<Entity> getAll();

	void add(Entity entity);

	void delete(Entity entity);
}
