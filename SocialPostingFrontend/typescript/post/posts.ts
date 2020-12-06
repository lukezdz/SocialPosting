import { HttpClient, Utils } from "../utils/utils";

export interface PostData {
	readonly id: string,
	readonly authorEmail: string,
	readonly creationTime: string,
	readonly content: string
}

export interface SimplePostData {
	readonly id: string,
	readonly authorsEmail: string,
	readonly content: string
}

export interface PostsData {
	readonly posts: SimplePostData[]
}

export class Posts {
	client: HttpClient;
	backendURL: string;
	
	constructor() {
		this.backendURL = Utils.getBackendUrl() + "/posts";
		this.client = new HttpClient();
	}

	public getPostIds(callback): void {
		this.client.get(this.backendURL, callback);
	}

	public getPostIdsByAuthor(authorsEmail: string, callback): void {
		this.client.get(`${this.backendURL}/by/${authorsEmail}`, callback);
	}

	public getPost(id: string, callback): void {
		this.client.get(`${this.backendURL}/${id}`, callback);
	}

	public createPost(content: string, authorsEmail: string, callback) {
		const request = {
			"content": content,
			"authorsEmail": authorsEmail
		}
		this.client.post(`${this.backendURL}`, callback, request);
	}

	public updatePost(id: string, content: string, callback) {
		const request = {
			"content": content
		}
		this.client.put(`${this.backendURL}/${id}`, callback, request);
	}

	public deletePost(id: string) {
		this.client.delete(`${this.backendURL}/${id}`, function(){location.reload()});
	}
}