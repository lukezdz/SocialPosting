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
	readonly birthDate: string,
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
		this.client.post(`${this.backendURL}/login`, function() {Utils.saveUserEmail(email); location.replace(`http://localhost:8084/views/user.html?email=${email}`)}, request);
	}

	public follow(currEmail: string, toFollow: string) {
		const request = {
			'email': currEmail,
			'toFollow': toFollow
		}
		this.client.put(`${this.backendURL}/follow`, function() {location.reload()}, request);
	}

	public unfollow(currEmail: string, toUnfollow: string) {
		const request = {
			'email': currEmail,
			'toUnfollow': toUnfollow
		}
		this.client.put(`${this.backendURL}/unfollow`, function() {location.reload()}, request);
	}

	public deleteUser(email: string) {
		this.client.delete(`${this.backendURL}/${email}`, function() {Utils.deleteUserEmail(); location.replace(`http://localhost:8084/views/user_list.html`)});
	}

	public updateUserProfilePicture(email: string, callback) {

	}

	public getUserProfilePictureData(email: string, callback) {

	}

	public updateUserProfilePictureData(email: string, callback) {

	}
}