package pl.edu.pg.eti.aui.SocialPosting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.aui.SocialPosting.post.service.PostService;
import pl.edu.pg.eti.aui.SocialPosting.user.entity.User;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class DataInitializer {
	private static final String DEFAULT_PASSWORD = "0000";
	private final UserService userService;
	private final PostService postService;

	@Autowired
	public DataInitializer(UserService userService, PostService postService) {
		this.userService = userService;
		this.postService = postService;
	}

	@PostConstruct
	public void init() {
		User markCucumber = User.builder()
				.email("mark.cucumber@feetbook.com")
				.build();
		User jeffCudos = User.builder()
				.email("jeff.cudos@borneo.com")
				.build();
		User billFences = User.builder()
				.email("bill.fences@megahard.com")
				.build();

		userService.add(markCucumber);
		userService.add(jeffCudos);
		userService.add(billFences);

		postService.create("I really don't like those Congress people!", markCucumber);
		postService.create("I am not a Reptilian!!! What are you talking about???", markCucumber);
		postService.create("My company is named after a place that has a rainforest!", jeffCudos);
		postService.create("Free shipping for everyone who buys Bumblebee membership!", jeffCudos);
		postService.create("New update to Walls is now live!", billFences);
		postService.create("Pancakes.", billFences);
		postService.create("I've added a post!", billFences);
	}
}
