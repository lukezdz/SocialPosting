# Social Posting

## Overview
This is a project created for Internet Services' Architecture course on Gdansk University of Technology, 
Faculty of Electronics, Telecommunications and Informatics.

It represents a simplified version of e.g. Twitter. There are users, who can post anything they want. Each post
contains text. Each user can create many different posts, but each post can be created by
only one user. Users can follow each other.

## CLI
### Commands available to guest users (not logged in)
| Command | Description |
|---|---|
| `logIn` | logs you in if you give the correct password |
| `createAccount` | creates an account and logs you in |
| `viewPosts` | views all posts of all users |
| `viewUsers` | views all users |
| `exit` | exits this application |

### Commands available to logged in users
| Command | Description |
|---|---|
| `logOut` | logs you out |
| `viewPosts` | views all posts of all users |
| `viewUsers` | views all users |
| `viewMyPosts` | views all your posts |
| `viewFollowed` | views all users you follow |
| `viewFollowedUsersPosts` | views all posts of all users you follow |
| `follow` | follow user |
| `unfollow` | unfollow user |
| `addPost` | views all posts of all users |
| `deletePost` | deletes your post with given post-id |
| `deleteAccount` | deletes your account, deletes all your posts and logs you out |
| `whoami` | shows all information about you |
| `exit` | exits this application |
