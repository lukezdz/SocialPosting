import { Posts, PostsData, PostData } from '../post/posts';
import { Utils } from '../utils/utils';

const posts = new Posts();

export function displayPostList(postsData: PostsData) {
	let ids: string[] = [];
	let p = postsData.posts;
	for (let data of p) {
		ids.push(data.id);
	}
	for (let id of ids) {
		posts.getPost(id, createPostListPostContainer);
	}
}

function createPostListPostContainer(postData: PostData) {
	const postDiv = document.createElement('div');
	postDiv.classList.add('post-list-post-container');
	postDiv.id = postData.id;

	const link = document.createElement('a');
	link.href = `../views/user?email=${postData.authorEmail}`;
	link.classList.add('linkless');
	postDiv.appendChild(link);

	const userInfo = document.createElement('p');
	userInfo.textContent = postData.authorEmail;
	userInfo.classList.add('post-list-user-email');
	link.appendChild(userInfo);

	const date = document.createElement('p');
	date.textContent = Utils.isoFormatDateFriendlyDate(postData.creationTime);
	date.classList.add('post-list-date');
	postDiv.appendChild(date);

	const edit = document.createElement('button');
	edit.textContent = 'Edit';
	edit.addEventListener('click', function(){handleEditButton(postData.id)});
	edit.classList.add('post-list-edit-button');
	edit.id = `${postData.id}-EditButton`
	postDiv.appendChild(edit);

	const content = document.createElement('p');
	content.textContent = postData.content;
	content.classList.add('post-list-post-content');
	content.id = `${postData.id}-Content`;
	postDiv.appendChild(content);

	const container = document.getElementById('post-list-container');
	container?.appendChild(postDiv);
}

function handleEditButton(id: string) {
	const div = document.getElementById(id);
	const contentElement = document.getElementById(`${id}-Content`);
	const editButton = document.getElementById(`${id}-EditButton`);

	if (contentElement) {
		const text = contentElement.textContent;
		contentElement.remove();
		editButton?.remove();

		const textArea = document.createElement('textarea');
		textArea.textContent = text;
		textArea.classList.add('post-list-edit-area');
		textArea.id = `${id}-EditArea`
		div?.appendChild(textArea);
		
		const saveEdit = document.createElement('button');
		saveEdit.textContent = 'Save';
		saveEdit.classList.add('post-list-edit-save-button');
		saveEdit.addEventListener('click', function() {handleSaveButton(id)})
		div?.appendChild(saveEdit);
	}

}

function handleSaveButton(id: string) {
	const textArea = document.getElementById(`${id}-EditArea`) as HTMLTextAreaElement;
	const content = textArea.value;
	if(content) {
		posts.updatePost(id, content, function(){window.location.reload()});
	}
}