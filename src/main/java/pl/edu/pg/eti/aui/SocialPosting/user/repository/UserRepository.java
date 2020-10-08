package pl.edu.pg.eti.aui.SocialPosting.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pg.eti.aui.SocialPosting.datastore.DataStore;
import pl.edu.pg.eti.aui.SocialPosting.repository.Repository;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class UserRepository implements Repository<User, String> {
	private DataStore dataStore;

	@Autowired
	public UserRepository(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	@Override
	public Optional<User> get(String id) {

		return Optional.empty();
	}

	@Override
	public List<User> getAll() {
		return dataStore.getUsers();
	}

	@Override
	public void add(User user) {
		dataStore.addUser(user);
	}

	@Override
	public void delete(User user) {
		dataStore.deleteUser(user);
	}
}
