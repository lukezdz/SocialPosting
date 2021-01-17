# Social Posting

## Overview
This is a project created for Internet Services' Architectures course on Gdansk University of Technology, Facult of Electronics, Telecommunication and Informatics.

It represents a simplified version of e.g. Twitter. There are users, who can post some thoughts. Each post contains text. Each user can create many different posts. Users can follow each other.

## Architecture
This project has been split to Monolithic and Microservices directories. Monolithic contains files of service before it was split to individual microservices - User Microservice, Post microservice and API Gateway, which is routing requests to Social Posting service to proper microservices.

## Usage
To start Social Posting application:
1. **Build** APIGateway, UserMicroservice and  PostMicroservice, by using script provided in scripts directory
	```bash
	bash scripts/build-microservices.sh
	```
	**Make sure to build all 3 projects.**  
2. **Build** frontend, by navigating to SocialPostingFrontend and running
	```bash
	npm install
	npm run build
	```
3. **Deploy** application by using provided `docker-compose.yml`, by running
	```bash
	docker-compose up
	```

This will generate 2 docker networks (frontend and backend) and 5 docker containers - frontend web server, reverse proxy, backend API gateway and 2 containers for each micro service running in the backend along with 2 containers running MySQL databases, one for each backend microservice. Microservices, databases and APIGateway are in network called `backend` and frontend server is in network called `frontend`. They communicate through reverse proxy, which is in both networks. User microservice additionaly uses docker volume to store all users' profile pictures. Volume is mounted in /images directory in container and located in ./images folder in directory in which this README is located.

Frontend web server is reachable on `localhost:8084`. You can also make HTTP requests directly to API gateway through reverse proxy, which is running on `localhost:8080`.
When making requests to backend make sure to use exposed port (`localhost:8080`), not internal docker name, as host is unable to resolve this name.

## API reference

### GET requests
| Request | Explanation |
|---|---|
| `/api/users` | Returns all users from the Social Posting service |
| `/api/users/{email}` | Returns info about user who uses `{email}` |
| `/api/posts/` | Returns all posts from the Social Posting service |
| `/api/posts/{post_id}` | Returns info about post with id = `{post_id}` |
| `/api/posts/by/{email}` | Returns all posts whose author is using `{email}` |
| `/api/posts/followed/{email}` | Returns all posts whose authors are users followed by user using `{email}` |


### POST requests
All post requests in Social Posting service use JSON Body of POST request to create desired resources. Paramters needed for given request are specified in the following table:  

| Request | Explanation | Parameters |
|---|---|---|
| `/api/users` | Creates new user | `name`, `surname`, `email`, `birthDate` (YYYY-MM-DD), `password` |
| `/api/posts` | Creates new post | `content`, `authorsEmail` |

### PUT requests
Similarly to POST requests PUT requests use JSON Body of PUT request. Parameters in the following table, are optional, you can use some of them or all of them. You can even not specify any of these parameters, in this case PUT request will not have any impact on the Social Posting service.

| Request | Explanation | Parameters |
|---|---|---|
| `/api/users/{email}` | Updates user, which uses `{email}` | `name`, `surname`, `birthDate` |
| `/api/users/password/{email}` | Update password of user, which uses `{email}` | `password`
| `/api/users/follow` | Follows specified user | `email` (of user who follows), `toFollow` (email of user being followed) |
| `/api/users/unfollow` | Unfollows specified user | `email`, `toUnfollow` (analogical to follow request) |
| `/api/posts/{id}` | Updates post with `{post_id}` with new content | `content` |

### DELETE requests
| Request | Explanation |
|---|---|
| `/api/posts/{id}` | Deletes post with id = `{post_id}` |
| `/api/users/{email}` | Deletes user using `{email}` and all his/her posts |
