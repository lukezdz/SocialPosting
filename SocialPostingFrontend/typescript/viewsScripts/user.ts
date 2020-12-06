import { UserData, Users } from '../user/users';
import { createUserListUserContainer } from './userList'
import { displayPostList } from './postList';
import { Posts } from '../post/posts';
import { Utils } from '../utils/utils';

const users = new Users();
const posts = new Posts()

export function displayFullUserInfo(userData: UserData) {
	const basicInfoDiv = document.getElementById('basic-info');

	const name = document.createElement('h2');
	name.textContent = `${userData.name} ${userData.surname}`;
	name.classList.add('user-basic-info-name');
	basicInfoDiv?.appendChild(name);

	const email = document.createElement('p');
	email.textContent = userData.email;
	email.classList.add('user-basic-info-email');
	basicInfoDiv?.appendChild(email);

	if (Utils.isUserLoggedIn()) {
		if (userData.email == Utils.getLoggedInUserEmail()) {
			const edit = document.createElement('button');
			edit.textContent = 'Edit';
			edit.addEventListener('click', function(){handleEditButton(userData.email)});
			edit.classList.add('user-basic-info-button');
			basicInfoDiv?.appendChild(edit);

			const deleteButton = document.createElement('button');
			deleteButton.textContent = 'Delete';
			deleteButton.addEventListener('click', function(){handleDeleteButton(userData.email)});
			deleteButton.classList.add('user-basic-info-delete');
			basicInfoDiv?.appendChild(deleteButton);
		}
		else {
			const currentLoggedInUserMail = Utils.getLoggedInUserEmail()!;
			users.getUser(currentLoggedInUserMail, function(data: UserData) {
				setFollowButton(userData.email, data.followedUsersEmails);
			})
		}
	}

	const followedDiv = document.getElementById('user-list-container');
	const headingFollowed = document.createElement('h3');
	headingFollowed.textContent = 'Followed users';
	followedDiv?.appendChild(headingFollowed);

	let emails = userData.followedUsersEmails;
	for (let email of emails) {
		users.getUser(email, createUserListUserContainer);
	}

	const postsDiv = document.getElementById('post-list-container');

	const headingPosts = document.createElement('h3');
	headingPosts.textContent = 'User\'s posts';
	postsDiv?.appendChild(headingPosts);

	if (Utils.isUserLoggedIn() && Utils.getLoggedInUserEmail() == userData.email) {
		const addPostDiv = document.createElement('div');
		addPostDiv.classList.add('user-add-post-container');
		postsDiv?.appendChild(addPostDiv);

		const postTextArea = document.createElement('textarea');
		postTextArea.placeholder = 'Add new post here...';
		postTextArea.classList.add('user-add-post-text-area');
		postTextArea.id = `${userData.email}-TextArea`;
		addPostDiv.appendChild(postTextArea);

		const addPostButton = document.createElement('button');
		addPostButton.textContent = 'Add';
		addPostButton.classList.add('user-add-post-button');
		addPostButton.id = `${userData.email}-AddPostButton`;
		addPostButton.addEventListener('click', function(){handleAddPostButton(userData.email)});
		addPostDiv.appendChild(addPostButton);
	}

	posts.getPostIdsByAuthor(userData.email, displayPostList);
}

function setFollowButton(email: string, list: string[]) {
	const basicInfoDiv = document.getElementById('basic-info');
	if (list.includes(email)) {
		const unfollow = document.createElement('button');
		unfollow.textContent = 'Unfollow';
		unfollow.addEventListener('click', function(){handleUnfollowButton(email)});
		unfollow.classList.add('user-basic-info-button');
		basicInfoDiv?.appendChild(unfollow);
	}
	else {
		const follow = document.createElement('button');
		follow.textContent = 'Follow';
		follow.addEventListener('click', function(){handleFollowButton(email)});
		follow.classList.add('user-basic-info-button');
		basicInfoDiv?.appendChild(follow);
	}
}

function handleEditButton(email: string) {
	location.replace(`http://localhost:8084/views/edit_user?email=${email}`);
}

function handleDeleteButton(email: string) {
	users.deleteUser(email);
}

function handleAddPostButton(email: string) {
	const textArea = document.getElementById(`${email}-TextArea`) as HTMLTextAreaElement;
	const postContent = textArea.value;

	posts.createPost(postContent, email, function(){window.location.reload()});
}

function handleFollowButton(email: string) {
	const currEmail = Utils.getLoggedInUserEmail()!;
	users.follow(currEmail, email);
}

function handleUnfollowButton(email: string) {
	const currEmail = Utils.getLoggedInUserEmail()!;
	users.unfollow(currEmail, email);
}