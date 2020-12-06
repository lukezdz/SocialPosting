import { Users } from './user/users';
import { Posts } from './post/posts';
import { displayPostList }  from './scripts/postList';
import { preparePage } from './scripts/singUp'
import { displayUserList } from './scripts/userList';
import { displayFullUserInfo } from './scripts/user';
import { prepareLoginPage } from './scripts/login';

const users = new Users();
const posts = new Posts();

export function loginScript() {
	prepareLoginPage();
}

export function postListScript() {
	posts.getPostIds(displayPostList);
}

export function signUpScript() {
	preparePage();
}

export function userListScript() {
	users.getUsersEmails(displayUserList);
}

export function userScript() {
	const params = new URLSearchParams(window.location.search);
	const email = params.get('email');
	if (email) {
		users.getUser(email, displayFullUserInfo);
	}
}




