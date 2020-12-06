import { Users } from '../user/users';

const users = new Users();

export function prepareLoginPage() {
	const loginButton = document.getElementById('login-button');
	loginButton?.addEventListener('click', handleLoginButton);
}

function handleLoginButton() {
	const emailField = document.getElementById('email') as HTMLInputElement;
	const passwordField = document.getElementById('password') as HTMLInputElement;
	let validity = true;

	if (!emailField || !emailField.value) {
		emailField.classList.add('invalid-input');
		validity = false;
	}

	if (!passwordField || !passwordField.value) {
		passwordField.classList.add('invalid-input');
		validity = false;
	}

	if (!validity) {
		return;
	}

	users.login(emailField.value, passwordField.value);
}