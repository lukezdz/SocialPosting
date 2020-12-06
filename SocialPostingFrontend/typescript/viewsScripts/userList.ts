import {UserData, UserEmails, Users} from '../user/users';
import { Utils } from '../utils/utils';

const users = new Users();

export function displayUserList(usersEmails: UserEmails): void {
	let emails = usersEmails.userEmails;
	for (let userEmail of emails) {
		users.getUser(userEmail, createUserListUserContainer);
	}
}

export function createUserListUserContainer(userData: UserData): void {
	const wrappingDiv = document.createElement('div');
	
	const link = document.createElement('a');
	link.href = `../views/user?email=${userData.email}`;
	link.classList.add('linkless');
	wrappingDiv.appendChild(link);

	const userDiv = document.createElement('div');
	userDiv.classList.add('user-list-info-container');

	const name = document.createElement('h4');
	name.textContent = `${userData.name} ${userData.surname}`;
	name.classList.add('user-list-info-name');
	userDiv.appendChild(name);

	const email = document.createElement('p');
	email.textContent = userData.email;
	email.classList.add('user-list-info-email');
	userDiv.appendChild(email);

	// if (Utils.isUserLoggedIn()) {
	// 	const follow = document.createElement('button');
	// 	follow.textContent = 'Follow';
	// 	follow.addEventListener('click', function(){handleFollowButton(userData.email)});
	// 	follow.classList.add('user-list-follow-button')
	// 	userDiv.appendChild(follow);
	// }

	link.appendChild(userDiv);
	document.getElementById('user-list-container')?.appendChild(wrappingDiv);
}