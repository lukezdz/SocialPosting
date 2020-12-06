import { Utils, HttpClient } from "../utils/utils";

export interface CreateUserRequest {
	readonly email: string,
	readonly name: string,
	readonly surname: string,
	readonly password: string,
	readonly birthDate: string
}

export interface UserData {
	readonly email: string,
	readonly name: string,
	readonly surname: string,
	readonly followedUsersEmails: string[],
	readonly password?: string
}

export interface UserEmails {
	readonly userEmails: string[]
}

export class Users {	
	client: HttpClient;
	backendURL: string;

	constructor() {
		this.backendURL = Utils.getBackendUrl() + '/users';
		this.client = new HttpClient();
	}

	public getUsersEmails(callback) {
		this.client.get(this.backendURL, callback);
	}

	public getUser(email: string, callback) {
		this.client.get(`${this.backendURL}/${email}`, callback);
	}

	public createUser(request: CreateUserRequest, callback) {
		this.client.post(`${this.backendURL}`, callback, request);
	}

	public updateUser(email: string, request, callback) {
		this.client.put(`${this.backendURL}/${email}`, callback, request);
	}

	public login(email: string, password: string) {
		const request = {
			'email': email,
			'password': password
		}
		this.client.post(`${this.backendURL}/login`, function() {Utils.saveUserEmail(email); location.replace(`http://localhost:8084/views/user?email=${email}`)}, request);
	}
}