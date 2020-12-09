import { DOMUtils, Utils } from '../utils/utils';

export function prepareNavigationBar() {
	if (Utils.isUserLoggedIn()) {
		const div = document.getElementById('nav-login-link');
		div?.children[0].remove();

		const logout = DOMUtils.createButton({
			text: 'Log out',
			config: [{event: 'click', handler: function() {Utils.deleteUserEmail(); window.location.reload()}}],
			classes: ['imitate-link']
		})
		div?.appendChild(logout);
	}
}