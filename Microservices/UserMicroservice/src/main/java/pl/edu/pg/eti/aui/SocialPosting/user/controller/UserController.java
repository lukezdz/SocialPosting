package pl.edu.pg.eti.aui.SocialPosting.user.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.CreateUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.FollowUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.GetUserResponse;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.GetUsersResponse;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UnfollowUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UpdatePasswordRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UpdateUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<GetUsersResponse> getUsers() {
		return ResponseEntity.ok(GetUsersResponse.entityToDtoMapper().apply(userService.findAll()));
	}

	@GetMapping("{email}")
	public ResponseEntity<GetUserResponse> getUser(@PathVariable("email") String email) {
		return userService.find(email)
				.map(value -> ResponseEntity.ok(GetUserResponse.entityToDtoMapper().apply(value)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request, UriComponentsBuilder builder) {
		CreateUserRequest.UserConfig config = CreateUserRequest.dtoToEntityMapper().apply(request);
		User user = userService.createAccount(config.getEmail(), config.getName(), config.getSurname(), config.getPassword(), config.getBirthDate());

		// create corresponding user in post microservice
		RestTemplate restTemplate = new RestTemplate();
		String createUserUrl = "http://localhost:8081/api/users";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject userJsonObject = new JSONObject();
		userJsonObject.put("email", config.getEmail());
		HttpEntity<String> createUserRequest = new HttpEntity<String>(userJsonObject.toString(), headers);
		restTemplate.postForObject(createUserUrl, createUserRequest, String.class);

		return ResponseEntity.created(builder.pathSegment("api", "users", "{email}")
				.buildAndExpand(user.getEmail()).toUri()).build();
	}

	@DeleteMapping("{email}")
	public ResponseEntity<Void> deleteUser(@PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			userService.deleteAccount(user.get().getEmail());
			// delete all posts by this user
			try {
				RestTemplate restTemplate = new RestTemplate();
				String request = String.format("http://localhost:8081/api/users/%s", email);
				restTemplate.delete(request);
			}
			catch (RestClientException exception) {
				exception.printStackTrace();
			}

			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("{email}")
	public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request, @PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			UpdateUserRequest.dtoToEntityMapper().apply(user.get(), request);
			userService.update(user.get());
			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("password/{email}")
	public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest request, @PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			UpdatePasswordRequest.dtoToEntityMapper().apply(user.get(), request);
			userService.update(user.get());
			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("follow")
	public ResponseEntity<Void> followUser(@RequestBody FollowUserRequest request) {
		Optional<User> user = userService.find(request.getEmail());
		Optional<User> toFollow = userService.find(request.getToFollow());
		if (user.isPresent() && toFollow.isPresent()) {
			userService.follow(request.getEmail(), request.getToFollow());
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("unfollow")
	public ResponseEntity<Void> unfollowUser(@RequestBody UnfollowUserRequest request) {
		Optional<User> user = userService.find(request.getEmail());
		Optional<User> toUnFollow = userService.find(request.getToUnfollow());
		if (user.isPresent() && toUnFollow.isPresent()) {
			userService.unfollow(request.getEmail(), request.getToUnfollow());
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.notFound().build();
	}
}
