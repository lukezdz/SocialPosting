import * as moment from 'moment';

export class Utils {
	public static getBackendUrl() {
		return 'http://localhost:8080/api';
	}

	public static isoFormatDateFriendlyDate(dateString: string): string {
		const date = moment.utc(dateString);
		const age = moment.now() - date.valueOf();

		const justNowThreshold = moment.duration(3, 'minutes').asMilliseconds();
		const minuteThreshold = moment.duration(1, 'hour').asMilliseconds();
		const hourThreshold = moment.duration(1, 'day').asMilliseconds();
		const daysThreshold = moment.duration(1, 'month').asMilliseconds();
		
		if (age <= justNowThreshold) {
			return "Just now";
		}
		else if (age <= minuteThreshold) {
			return moment.duration(age).minutes() + " minutes ago"
		}
		else if (age <= hourThreshold) {
			return moment.duration(age).hours() + " hours ago";
		}
		else if (age <= daysThreshold) {
			return moment.duration(age).days() + " days ago";
		}
		else {
			return date.format('DD/MM/YYYY HH:mm');
		}
	}

	public static saveUserEmail(email: string) {
		sessionStorage.setItem('currEmail', email);
	}

	public static deleteUserEmail() {
		sessionStorage.removeItem('currEmail');
	}

	public static getLoggedInUserEmail(): string | null {
		return sessionStorage.getItem('currEmail');
	}

	public static isUserLoggedIn(): boolean {
		if (Utils.getLoggedInUserEmail() !== null) {
			return true;
		}
		return false;
	}
}

export class HttpClient {
	baseURL: string;

	/**
	 * Sends HTTP DELETE request to provided URL
	 * @param url full url
	 * @param callback function taking no params needed to be executed when response is ready
	 */
	public delete(url, callback) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if(this.readyState === 4 && this.status === 202) {
				callback();
			}
		};
		xhttp.open("DELETE", url, true);
		xhttp.send();
	}

	/**
	 * Sends HTTP GET request to provided URL
	 * @param url full url
	 * @param callback function taking JSON object as parameter to be executed when response is ready
	 */
	public get(url, callback) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState === 4 && this.status === 200) {
				let responseText = this.responseText;
				let parsed = JSON.parse(responseText);
				callback(parsed);
			}
		};
		xhttp.open("GET", url, true);
		xhttp.send();
	}

	/**
	 * Sends HTTP POST request to provided URL
	 * @param url full url
	 * @param callback function taking no params needed to be executed when response is ready
	 * @param request JSON body of request
	 */
	public post(url, callback, request) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState === 4 && (this.status === 201 || this.status === 202)) {
				callback();
			}
		}
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-Type", "application/json");
		xhttp.send(JSON.stringify(request));
	}

	/**
	 * Sends HTTP PUT request to provided URL
	 * @param url full url
	 * @param callback function taking no params needed to be executed when response is ready
	 * @param request JSON body of request
	 */
	public put(url, callback, request) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState === 4 && this.status === 202) {
				callback();
			}
		}
		xhttp.open("PUT", url, true);
		xhttp.setRequestHeader("Content-Type", "application/json");
		xhttp.send(JSON.stringify(request));
	}
	
}