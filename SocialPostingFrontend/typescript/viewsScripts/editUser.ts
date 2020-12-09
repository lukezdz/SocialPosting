import { UserData, Users } from '../user/users';

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

	users.updateUser(email, request, function(){location.replace(`./views/user?email=${email}`)});
}