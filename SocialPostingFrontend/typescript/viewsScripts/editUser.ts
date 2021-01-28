import { UserData, Users } from '../user/users';
import { Utils } from '../utils/utils';

const users = new Users();

export function prepareEditPage() {
	const params = new URLSearchParams(window.location.search);
	const email = params.get('email');
	if (email) {
		users.getUser(email, populateValues);
	}
}

function populateValues(userData: UserData) {
	const name = document.getElementById('name') as HTMLInputElement;
	const surname = document.getElementById('surname') as HTMLInputElement;
	const birth = document.getElementById('birth') as HTMLInputElement;
	const button = document.getElementById('save-user-info-button') as HTMLButtonElement;

	name.value = userData.name;
	surname.value = userData.surname;
	birth.value = userData.birthDate;

	button.addEventListener('click', function() {handleSaveButton(userData.email)})
}

function handleSaveButton(email: string) {
	const name = document.getElementById('name') as HTMLInputElement;
	const surname = document.getElementById('surname') as HTMLInputElement;
	const birth = document.getElementById('birth') as HTMLInputElement;

	const request = {
		"name": name.value,
		"surname": surname.value,
		"birthDate": birth.value
	}

	users.updateUser(email, request, function(){location.replace(`./user.html?email=${email}`)});
	
	const filefield = document.getElementById('profile-pic') as HTMLInputElement;
	if (filefield && filefield.files) {
		users.client.uploadFile(
			`${Utils.getBackendUrl()}/users/${email}/profile-pic`,
			function(){location.replace(`./user.html?email=${email}`); location.reload()},
			'profile-pic',
			filefield.files[0],
			filefield.files[0].name
		);
	}

	const profilePicDesc = document.getElementById('profile-pic-desc') as HTMLInputElement;
	if (profilePicDesc) {
		const request = {
			"description": profilePicDesc.value
		}
		users.client.put(
			`${Utils.getBackendUrl()}/users/${email}/profile-pic/metadata`,
			function(){location.replace(`./user.html?email=${email}`); location.reload()},
			request
		);
	}
}