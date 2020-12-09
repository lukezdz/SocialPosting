import { Posts, PostsData, PostData } from '../post/posts';
import { DOMUtils, Utils } from '../utils/utils';

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
	const postDiv = DOMUtils.createDiv(['post-list-post-container'], postData.id);

	const link = DOMUtils.createLink(`../views/user?email=${postData.authorEmail}`, ['linkless']);
	const userInfo = DOMUtils.createParagraph(postData.authorEmail, ['post-list-user-email']);
	link.appendChild(userInfo);
	postDiv.appendChild(link);

	const date = DOMUtils.createParagraph(Utils.isoFormatDateFriendlyDate(postData.creationTime), ['post-list-date']);
	postDiv.appendChild(date);

	if (Utils.isUserLoggedIn() && Utils.getLoggedInUserEmail() === postData.authorEmail) {
		const editButton = DOMUtils.createButton({
			text: 'Edit',
			config: [{event: 'click', handler: function() {handleEditButton(postData.id)}}],
			classes: ['post-list-edit-button'],
			id: `${postData.id}-EditButton`
		})
		postDiv.appendChild(editButton);

		const deleteButton = DOMUtils.createButton({
			text: 'Delete',
			config: [{event: 'click', handler: function() {handleDeleteButton(postData.id)}}],
			classes: ['post-list-delete-button'],
			id: `${postData.id}-DeleteButton`
		});
		postDiv.appendChild(deleteButton);
	}

	const content = DOMUtils.createParagraph(postData.content, ['post-list-post-content'], `${postData.id}-Content`);
	postDiv.appendChild(content);

	const container = document.getElementById('post-list-container');
	container?.appendChild(postDiv);
}

function handleEditButton(id: string) {
	const div = document.getElementById(id);
	const contentElement = document.getElementById(`${id}-Content`);
	const editButton = document.getElementById(`${id}-EditButton`);
	const deleteButton = document.getElementById(`${id}-DeleteButton`);

	if (contentElement) {
		const text = contentElement.textContent;
		contentElement.remove();
		editButton?.remove();
		deleteButton?.remove();

		const textArea = DOMUtils.createTextArea({
			text: text!,
			classes: ['post-list-edit-area'],
			id: `${id}-EditArea`
		});
		div?.appendChild(textArea);
		
		const saveEdit = DOMUtils.createButton({
			text: 'Save',
			config: [{event: 'click', handler: function() {handleSaveButton(id)}}],
			classes: ['post-list-edit-save-button']
		});
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

function handleDeleteButton(id: string) {
	posts.deletePost(id);
}