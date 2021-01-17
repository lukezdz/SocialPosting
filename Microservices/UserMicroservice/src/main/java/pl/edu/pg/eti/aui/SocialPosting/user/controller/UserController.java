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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.CreateUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.FollowUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.GetProfilePictureMetadataResponse;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.GetUserResponse;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.GetUsersResponse;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.LoginRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UnfollowUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UpdatePasswordRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UpdateProfilePictureDescriptionRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.dto.UpdateUserRequest;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.io.IOException;
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

	@GetMapping(value = "{email}/profile-pic", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getUsersProfilePicture(@PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		try {
			if (user.isPresent()) {
				return ResponseEntity.ok(userService.getUserProfilePicture(user.get()));
			}
		}
		catch (IOException e) {}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("{email}/profile-pic/metadata")
	public ResponseEntity<GetProfilePictureMetadataResponse> getProfilePictureDescriptionResponse(@PathVariable("email") String email) {
		return userService.find(email)
				.map(value -> ResponseEntity.ok(GetProfilePictureMetadataResponse.entityToDtoMapper().apply(value)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request, UriComponentsBuilder builder) {
		CreateUserRequest.UserConfig config = CreateUserRequest.dtoToEntityMapper().apply(request);
		User user = userService.createAccount(config.getEmail(), config.getName(), config.getSurname(), config.getPassword(), config.getBirthDate());

		// create corresponding user in post microservice
		RestTemplate restTemplate = new RestTemplate();
		String createUserUrl = "http://postmicroservice/api/users";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject userJsonObject = new JSONObject();
		userJsonObject.put("email", config.getEmail());
		HttpEntity<String> createUserRequest = new HttpEntity<>(userJsonObject.toString(), headers);
		restTemplate.postForObject(createUserUrl, createUserRequest, String.class);

		return ResponseEntity.created(builder.pathSegment("api", "users", "{email}")
				.buildAndExpand(user.getEmail()).toUri()).build();
	}

	@PostMapping("login")
	public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
		Optional<User> user = userService.find(request.getEmail());
		if (user.isPresent() && user.get().checkPassword(request.getPassword())) {
			return ResponseEntity.accepted().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping("{email}")
	public ResponseEntity<Void> deleteUser(@PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			userService.deleteAccount(user.get().getEmail());
			// delete all posts by this user
			try {
				RestTemplate restTemplate = new RestTemplate();
				String request = String.format("http://postmicroservice/api/users/%s", email);
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

	@PutMapping(value = "{email}/profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> updateUsersProfilePicture(
			@PathVariable("email") String email,
			@RequestParam("profile-pic") MultipartFile profilePicture
	) {
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			try {
				userService.updateUserProfilePicture(user.get(), profilePicture.getInputStream());
			}
			catch (IOException e) {
				return ResponseEntity.badRequest().build();
			}

			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("{email}/profile-pic/metadata")
	public ResponseEntity<Void> updateUsersProfilePictureDescription(
			@PathVariable("email") String email,
			@RequestBody UpdateProfilePictureDescriptionRequest request)
	{
		Optional<User> user = userService.find(email);
		if (user.isPresent()) {
			UpdateProfilePictureDescriptionRequest.dtoToEntityMapper().apply(user.get(), request);
			userService.update(user.get());
			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
}
