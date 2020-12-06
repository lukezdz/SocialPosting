import { Utils } from '../utils/utils';

export function prepareNavigationBar() {
	if (Utils.isUserLoggedIn()) {
		const div = document.getElementById('nav-login-link');
		div?.children[0].remove();
		
		const logout = document.createElement('button');
		logout.textContent = "Log out";
		logout.classList.add('imitate-link')
		logout.addEventListener('click', function(){Utils.deleteUserEmail(); window.location.reload()});
		div?.appendChild(logout);
	}
}