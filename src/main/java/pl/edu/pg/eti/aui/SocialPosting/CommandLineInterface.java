package pl.edu.pg.eti.aui.SocialPosting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.aui.SocialPosting.exceptions.InvalidInputException;
import pl.edu.pg.eti.aui.SocialPosting.exceptions.UserNotLoggedInException;
import pl.edu.pg.eti.aui.SocialPosting.post.service.PostService;
import pl.edu.pg.eti.aui.SocialPosting.user.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

@Component
public class CommandLineInterface implements CommandLineRunner {
	private final static String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	private final UserService userService;
	private final PostService postService;
	private final Scanner scanner;
	private String currentUserEmail;
	private boolean isLoggedIn = false;

	@Autowired
	public CommandLineInterface(UserService userService, PostService postService) {
		this.userService = userService;
		this.postService = postService;
		scanner = new Scanner(System.in);
	}

	@Override
	public void run(String... args) throws Exception {
		help();
		boolean go = true;
		while(go) {
			System.out.print("> ");
			String command = scanner.nextLine();
			try {
				switch (command) {
					case "viewPosts": {
						viewPosts();
						continue;
					}
					case "viewUsers": {
						viewUsers();
						continue;
					}
					case "help": {
						help();
						continue;
					}
					case "exit": {
						go = false;
						continue;
					}
				}
				if (isLoggedIn) {
					switch (command) {
						case "logOut": {
							logOut();
							break;
						}
						case "viewMyPosts": {
							viewMyPosts();
							break;
						}
						case "viewFollowed": {
							viewFollowedUsers();
							break;
						}
						case "viewFollowedUsersPosts": {
							viewFollowedUserPosts();
							break;
						}
						case "follow": {
							follow();
							break;
						}
						case "unfollow": {
							unfollow();
							break;
						}
						case "addPost": {
							addPost();
							break;
						}
						case "deletePost": {
							deletePost();
							break;
						}
						case "deleteAccount": {
							deleteAccount();
							break;
						}
						case "whoami": {
							whoami();
							break;
						}
						default: {
							System.out.println("Unknown command.");
							help();
						}
					}
				} else {
					switch (command) {
						case "logIn": {
							logIn();
							break;
						}
						case "createAccount": {
							createAccount();
							break;
						}
						default: {
							System.out.println("Unknown command.");
							help();
						}
					}
				}
			}
			catch(InvalidInputException iie) {}
			catch(UserNotLoggedInException unle) {
				// This should never happen, as commands that require to be logged in are not accessible to guest user
				System.err.println("You need to be logged in to perform this action.");
			}
		}
	}

	private void logIn() {
		String email;
		String password;

		System.out.print("email: ");
		email = scanner.nextLine();
		System.out.print("password: ");
		password = scanner.nextLine();

		userService.find(email).ifPresentOrElse(user -> {
			if (user.checkPassword(password)) {
				currentUserEmail = email;
				isLoggedIn = true;
				System.out.println(String.format("Welcome, %s %s!", user.getName(), user.getSurname()));
			}
			else {
				System.err.println("Email or password wrong.");
			}
		}, () -> {
				System.err.println("Email or password wrong.");
		});
	}

	private void createAccount() throws InvalidInputException {
		String email;
		String name;
		String surname;
		String password;
		LocalDate birthDate;

		System.out.println("Please provide all necessary information:");
		System.out.print("email: ");
		email = scanner.nextLine();

		if (!email.matches(EMAIL_REGEX)) {
			System.out.println("This is not a valid email address");
			throw new InvalidInputException();
		}

		System.out.print("your name: ");
		name = scanner.nextLine();
		System.out.print("your surname: ");
		surname = scanner.nextLine();
		System.out.print("password: ");
		password = scanner.nextLine();
		System.out.print("please repeat your password: ");
		String passwordRepeat = scanner.nextLine();

		if (!passwordRepeat.equals(password)) {
			System.out.println("Password don't match");
			throw new InvalidInputException();
		}

		System.out.print("birth date (dd-MM-yyyy): ");
		String birthDateString = scanner.nextLine();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		dtf = dtf.withLocale(Locale.US);
		birthDate = LocalDate.parse(birthDateString, dtf);

		userService.createAccount(email, name, surname, password, birthDate);
		System.out.println("Your account has been successfully created.");
	}

	private void viewPosts() {
		postService.findAll().forEach(post -> System.out.println(post.presentContent()));
	}

	private void viewUsers() {
		userService.findAll().forEach(user -> System.out.println(user.basicInfo()));
	}

	private void logOut() throws UserNotLoggedInException {
		checkIfLoggedIn();
		currentUserEmail = "";
		isLoggedIn = false;
	}

	private void viewMyPosts() throws UserNotLoggedInException {
		checkIfLoggedIn();
		userService.find(currentUserEmail).ifPresentOrElse(user -> {
			postService.findByUser(user).forEach(post -> System.out.println(post.presentContent()));
		}, () -> {
			System.out.println("Something went wrong. Couldn't find your user. Logging you out.");
			isLoggedIn = false;
			currentUserEmail = "";
		});
	}

	private void viewFollowedUsers() throws UserNotLoggedInException {
		checkIfLoggedIn();
		try {
			userService.find(currentUserEmail).ifPresent(user -> {
				user.getFollowedUsers().forEach(followedUser -> {
						System.out.println(followedUser.basicInfo());
				});
			});
		}
		catch(NullPointerException exception) {}
	}

	private void viewFollowedUserPosts() throws UserNotLoggedInException {
		checkIfLoggedIn();
		try {
			userService.find(currentUserEmail).ifPresent(user -> {
				user.getFollowedUsers().forEach(followedUser -> {
					postService.findByUser(followedUser).forEach(post -> {
						System.out.println(post.presentContent());
					});
				});
			});
		}
		catch(NullPointerException exception) {}
	}

	private void follow() throws UserNotLoggedInException {
		checkIfLoggedIn();
		String userEmail;

		System.out.print("user's email: ");
		userEmail = scanner.nextLine();

		userService.follow(currentUserEmail, userEmail);
	}

	private void unfollow() throws UserNotLoggedInException {
		checkIfLoggedIn();
		String userEmail;

		System.out.print("user's email: ");
		userEmail = scanner.nextLine();

		userService.unfollow(currentUserEmail, userEmail);
	}

	private void addPost() throws UserNotLoggedInException {
		checkIfLoggedIn();

		String content;

		System.out.print("content: ");
		content = scanner.nextLine();

		userService.find(currentUserEmail).ifPresentOrElse(user -> {
			postService.add(content, user);
		}, () -> {
			System.out.println("Something went wrong.");
		});
	}

	private void deletePost() throws UserNotLoggedInException {
		checkIfLoggedIn();

		String postId;

		System.out.print("post id: ");
		postId = scanner.nextLine();

		postService.delete(postId, currentUserEmail);
	}

	private void deleteAccount() throws UserNotLoggedInException {
		checkIfLoggedIn();
		userService.deleteAccount(currentUserEmail);
		logOut();
	}

	private void whoami() throws UserNotLoggedInException {
		checkIfLoggedIn();
		userService.find(currentUserEmail).ifPresentOrElse(user -> System.out.println(user), () -> System.err.println("Something went wrong."));
	}

	private void help() {
		if (isLoggedIn) {
			try {
				loggedInHelp();
			}
			catch(IOException exception) {
				System.err.println("Couldn't find help file.");
			}
		}
		else {
			try {
				guestHelp();
			}
			catch(IOException exception) {
				System.err.println("Couldn't find help file.");
			}
		}
	}

	private void guestHelp() throws IOException {
		Path path = Paths.get("D:\\dane\\STUDIA\\Architektury uslug internetowych\\SocialPosting\\target\\classes\\help\\guestHelp.txt");
		String content = new String(Files.readAllBytes(path));
		System.out.println(content);
	}

	private void loggedInHelp() throws IOException {
		Path path = Paths.get("D:\\dane\\STUDIA\\Architektury uslug internetowych\\SocialPosting\\target\\classes\\help\\loggedInHelp.txt");
		String content = new String(Files.readAllBytes(path));
		System.out.println(content);
	}

	private void checkIfLoggedIn() throws UserNotLoggedInException {
		if (!isLoggedIn) {
			throw new UserNotLoggedInException();
		}
	}
}
