import { Users } from './user/users';
import { Posts } from './post/posts';
import { prepareEditPage } from './viewsScripts/editUser';
import { displayPostList }  from './viewsScripts/postList';
import { prepareSingUpPage } from './viewsScripts/singUp'
import { displayUserList } from './viewsScripts/userList';
import { displayFullUserInfo } from './viewsScripts/user';
import { prepareLoginPage } from './viewsScripts/login';
import { prepareNavigationBar } from './viewsScripts/shared';

const users = new Users();
const posts = new Posts();

export function editUserScript() {
	prepareNavigationBar();
	prepareEditPage();
}

export function indexScript() {
	prepareNavigationBar();
}

export function loginScript() {
	prepareNavigationBar();
	prepareLoginPage();
}

export function postListScript() {
	prepareNavigationBar();
	posts.getPostIds(displayPostList);
}

export function signUpScript() {
	prepareNavigationBar();
	prepareSingUpPage();
}

export function userListScript() {
	prepareNavigationBar();
	users.getUsersEmails(displayUserList);
}

export function userScript() {
	prepareNavigationBar();
	const params = new URLSearchParams(window.location.search);
	const email = params.get('email');
	if (email) {
		users.getUser(email, displayFullUserInfo);
	}
}




