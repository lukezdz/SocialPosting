package pl.edu.pg.eti.aui.SocialPosting.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.pg.eti.aui.SocialPosting.post.service.PostService;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.CreateUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
	private final UserService userService;
	private final PostService postService;

	@Autowired
	public UserController(UserService userService, PostService postService) {
		this.userService = userService;
		this.postService = postService;
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request, UriComponentsBuilder builder) {
		CreateUserRequest.UserConfig config = CreateUserRequest.dtoToEntityMapper().apply(request);
		User user = User.builder().email(config.getEmail()).build();
		userService.add(user);
		return ResponseEntity.created(builder.pathSegment("api", "users", "{email}")
				.buildAndExpand(user.getEmail()).toUri()).build();
	}

	@DeleteMapping("{email}")
	public ResponseEntity<Void> deleteUser(@PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			postService.findByUserEmail(email).forEach(post -> postService.delete(post.getId(), email));
			userService.deleteAccount(user.get().getEmail());
			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
}
