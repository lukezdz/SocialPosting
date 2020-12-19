import {UserData, UserEmails, Users} from '../user/users';
import { DOMUtils, Utils } from '../utils/utils';

const users = new Users();

export function displayUserList(usersEmails: UserEmails): void {
	let emails = usersEmails.userEmails;
	for (let userEmail of emails) {
		users.getUser(userEmail, createUserListUserContainer);
	}
}

export function createUserListUserContainer(userData: UserData): void {
	const wrappingDiv = document.createElement('div');
	
	const link = DOMUtils.createLink(`../views/user.html?email=${userData.email}`, ['linkless']);
	wrappingDiv.appendChild(link);

	const userDiv = DOMUtils.createDiv(['user-list-info-container']);

	const name = DOMUtils.createH4(`${userData.name} ${userData.surname}`, ['user-list-info-name']);
	userDiv.appendChild(name);

	const email = DOMUtils.createParagraph(userData.email, ['user-list-info-email']);
	userDiv.appendChild(email);

	link.appendChild(userDiv);
	document.getElementById('user-list-container')?.appendChild(wrappingDiv);
}