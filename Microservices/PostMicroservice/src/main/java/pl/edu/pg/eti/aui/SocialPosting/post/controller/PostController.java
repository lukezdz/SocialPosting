package pl.edu.pg.eti.aui.SocialPosting.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.pg.eti.aui.SocialPosting.post.dto.CreatePostRequest;
import pl.edu.pg.eti.aui.SocialPosting.post.dto.GetPostResponse;
import pl.edu.pg.eti.aui.SocialPosting.post.dto.GetPostsResponse;
import pl.edu.pg.eti.aui.SocialPosting.post.dto.GetSpecifiedUserResponse;
import pl.edu.pg.eti.aui.SocialPosting.post.dto.UpdatePostRequest;
import pl.edu.pg.eti.aui.SocialPosting.post.entity.Post;
import pl.edu.pg.eti.aui.SocialPosting.post.service.PostService;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("api/posts")
public class PostController {
	private final PostService postService;
	private final UserService userService;

	@Autowired
	public PostController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<GetPostsResponse> getPosts() {
		List<Post> all = postService.findAll();
		Function<Collection<Post>, GetPostsResponse> mapper = GetPostsResponse.entityToDtoMapper();
		GetPostsResponse response = mapper.apply(all);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by/{email}")
	public ResponseEntity<GetPostsResponse> getPostsBy(@PathVariable("email") String email) {
		Optional<User> user = userService.find(email);
		if (user.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<Post> usersPosts = postService.findByUserEmail(user.get().getEmail());
		GetPostsResponse response = GetPostsResponse.entityToDtoMapper().apply(usersPosts);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/followed/{email}")
	public ResponseEntity<GetPostsResponse> getPostsByFollowed(@PathVariable("email") String email) {
		Optional<User> userOptional = userService.find(email);
		if (userOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<Post> followedPosts = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		String request = "http://localhost:8080/api/users/" + email;
	 	GetSpecifiedUserResponse userResponse = restTemplate.getForObject(request, GetSpecifiedUserResponse.class);
		userResponse.getFollowedUsersEmails().forEach(followedEmail -> {
			followedPosts.addAll(postService.findByUserEmail(followedEmail));
		});
		GetPostsResponse response = GetPostsResponse.entityToDtoMapper().apply(followedPosts);
		return ResponseEntity.ok(response);
	}

	@GetMapping("{id}")
	public ResponseEntity<GetPostResponse> getPost(@PathVariable("id") String postId) {
		return postService.find(postId)
				.map(value -> ResponseEntity.ok(GetPostResponse.entityToDtoMapper().apply(value)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request, UriComponentsBuilder builder) {
		CreatePostRequest.Post postConfig = CreatePostRequest.dtoToEntityMapper(userService).apply(request);
		if (postConfig.getUser() == null) {
			return ResponseEntity.notFound().build();
		}
		Post post = postService.create(postConfig.getContent(), postConfig.getUser());
		return ResponseEntity.created(builder.pathSegment("api", "posts", "{id}").buildAndExpand(post.getId()).toUri()).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Void> updatePost(@RequestBody UpdatePostRequest request, @PathVariable("id") String id) {
		Optional<Post> post = postService.find(id);
		if (post.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		UpdatePostRequest.dtoToEntityMapper().apply(post.get(), request);
		postService.update(post.get());
		return ResponseEntity.accepted().build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deletePost(@PathVariable("id") String id) {
		Optional<Post> post = postService.find(id);
		if (post.isPresent()) {
			postService.delete(post.get().getId(), post.get().getAuthor().getEmail());
			return ResponseEntity.accepted().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
}
