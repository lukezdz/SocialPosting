import { CreateUserRequest, Users } from '../user/users';


const users = new Users();

export function preparePage() {
	const button = document.getElementById('create-account-button');
	button?.addEventListener('click', checkAndSendRequest);
}

export function checkAndSendRequest() {
	const email = document.getElementById('email') as HTMLInputElement;
	const name = document.getElementById('name') as HTMLInputElement;
	const surname = document.getElementById('surname') as HTMLInputElement;
	const birthDate = document.getElementById('birth') as HTMLInputElement;
	const password = document.getElementById('password') as HTMLInputElement;
	let validity = true;

	if (email == null || email.value == null) {
		email.classList.add('invalid-input-field');
		validity = false;
	}

	if (name == null || name.value == null) {
		name.classList.add('invalid-input-field')
		validity = false;
	}

	if (surname == null || surname.value == null) {
		surname.classList.add('invalid-input-field')
		validity = false;
	}
	
	if (birthDate == null || birthDate.value == null) {
		birthDate.classList.add('invalid-input-field')
		validity = false;
	}

	if (password == null || password.value == null) {
		password.classList.add('invalid-input-field')
		validity = false;
	}

	if (!validity) {
		return;
	}

	const request: CreateUserRequest = {
		email: email.value,
		name: name.value,
		surname: surname.value,
		birthDate: birthDate.value,
		password: password.value
	}

	users.createUser(request, function(){location.replace(`http://localhost:8084/views/user?email=${email.value}`)});
}