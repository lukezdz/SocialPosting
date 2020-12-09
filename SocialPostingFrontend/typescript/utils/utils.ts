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

export interface EventHandlerConfig {
	event: string,
	handler
}

export interface TextAreaProps {
	text?: string,
	placeholder?: string,
	classes?: string[],
	id?: string
}

export interface ButtonProps {
	text: string,
	config?: EventHandlerConfig[],
	classes?: string[],
	id?: string
}

export class DOMUtils {
	public static createH2(text: string, classes?: string[], id?: string): HTMLHeadingElement {
		return this.createHeading(2, text, classes, id);
	}

	public static createH3(text: string, classes?: string[], id?: string): HTMLHeadingElement {
		return this.createHeading(3, text, classes, id);
	}

	public static createH4(text: string, classes?: string[], id?: string): HTMLHeadingElement {
		return this.createHeading(4, text, classes, id);
	}

	private static createHeading(headingSize: number, text: string, classes?: string[], id?: string): HTMLHeadingElement {
		const heading = document.createElement(`h${headingSize}`) as HTMLHeadingElement;
		heading.textContent = text;

		if(id) {
			heading.id = id;
		}

		if(classes) {
			heading.classList.add(...classes);
		}

		return heading;
	}

	public static createParagraph(text: string, classes?: string[], id?: string): HTMLParagraphElement {
		const paragraph = document.createElement('p') as HTMLParagraphElement;
		paragraph.textContent = text;

		if(id) {
			paragraph.id = id;
		}

		if(classes) {
			paragraph.classList.add(...classes);
		}

		return paragraph;
	}

	public static createDiv(classes?: string[], id?: string): HTMLDivElement {
		const div = document.createElement('div');
		
		if(id) {
			div.id = id;
		}

		if(classes) {
			div.classList.add(...classes);
		}

		return div;
	}

	public static createButton(props: ButtonProps): HTMLButtonElement {
		const button = document.createElement('button') as HTMLButtonElement;
		button.textContent = props.text;
		
		if (props.config) {
			for(let con of props.config) {
				button.addEventListener(con.event, con.handler);
			}
		}

		if (props.id) {
			button.id = props.id;
		}

		if (props.classes) {
			button.classList.add(...props.classes);
		}

		return button;
	}

	public static createLink(href: string, classes?: string[], id?: string): HTMLAnchorElement {
		const link = document.createElement('a');
		link.href = href;

		if (id) {
			link.id = id;
		}

		if (classes) {
			link.classList.add(...classes);
		}

		return link;
	}

	public static createTextArea(props: TextAreaProps): HTMLTextAreaElement {
		const textArea = document.createElement('textarea');
		if (props.text) {
			textArea.textContent = props.text;
		}

		if (props.placeholder) {
			textArea.placeholder = props.placeholder;
		}
		
		if (props.classes) {
			textArea.classList.add(...props.classes);
		}

		if (props.id) {
			textArea.id = props.id;
		}

		return textArea;
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