import { UserData, Users } from '../user/users';
import { createUserListUserContainer } from './userList'
import { displayPostList } from './postList';
import { Posts } from '../post/posts';
import { DOMUtils, Utils } from '../utils/utils';

const users = new Users();
const posts = new Posts()

export function displayFullUserInfo(userData: UserData) {
	const basicInfoDiv = document.getElementById('basic-info');

	const name = DOMUtils.createH2(`${userData.name} ${userData.surname}`, ['user-basic-info-name']);
	const email = DOMUtils.createParagraph(userData.email, ['user-basic-info-email']);
	basicInfoDiv?.appendChild(name);
	basicInfoDiv?.appendChild(email);

	if (Utils.isUserLoggedIn()) {
		if (userData.email == Utils.getLoggedInUserEmail()) {
			const editButton = DOMUtils.createButton({
				text: 'Edit',
				config: [{event: 'click', handler: function() {handleEditButton(userData.email)}}],
				classes: ['user-basic-info-button']
			})
			const deleteButton = DOMUtils.createButton({
				text: 'Delete',
				config: [{event: 'click', handler: function() {handleDeleteButton(userData.email)}}],
				classes: ['user-basic-info-delete']
			})
			basicInfoDiv?.appendChild(editButton);
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
	const headingFollowed = DOMUtils.createH3('Followed users')
	followedDiv?.appendChild(headingFollowed);

	let emails = userData.followedUsersEmails;
	for (let email of emails) {
		users.getUser(email, createUserListUserContainer);
	}

	const postsDiv = document.getElementById('post-list-container');

	const headingPosts = DOMUtils.createH3('User\'s posts');
	postsDiv?.appendChild(headingPosts);

	if (Utils.isUserLoggedIn() && Utils.getLoggedInUserEmail() == userData.email) {
		const addPostDiv = DOMUtils.createDiv(['user-add-post-container']);
		postsDiv?.appendChild(addPostDiv);

		const postTextArea = DOMUtils.createTextArea({
			placeholder: 'Add new post here...',
			id: `${userData.email}-TextArea`,
			classes: ['user-add-post-text-area']
		});
		addPostDiv.appendChild(postTextArea);

		const addPostButton = DOMUtils.createButton({
			text: 'Add',
			config: [{event: 'click', handler: function() {handleAddPostButton(userData.email)}}],
			classes: ['user-add-post-button'],
			id: `${userData.email}-AddPostButton`
		});
		addPostDiv.appendChild(addPostButton);
	}

	posts.getPostIdsByAuthor(userData.email, displayPostList);
}

function setFollowButton(email: string, list: string[]) {
	const basicInfoDiv = document.getElementById('basic-info');
	if (list.includes(email)) {
		const unfollowButton = DOMUtils.createButton({
			text: 'Unfollow',
			config: [{event: 'click', handler: function() {handleUnfollowButton(email)}}],
			classes: ['user-basic-info-config']
		});
		basicInfoDiv?.appendChild(unfollowButton);
	}
	else {
		const followButton = DOMUtils.createButton({
			text: 'Follow',
			config: [{event: 'click', handler: function() {handleFollowButton(email)}}],
			classes: ['user-basic-info-button']
		});
		basicInfoDiv?.appendChild(followButton);
	}
}

function handleEditButton(email: string) {
	location.replace(`http://localhost:8084/views/edit_user.html?email=${email}`);
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