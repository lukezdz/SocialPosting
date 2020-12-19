# Social Posting

## Overview
This is a project created for Internet Services' Architectures course on Gdansk University of Technology, Facult of Electronics, Telecommunication and Informatics.

It represents a simplified version of e.g. Twitter. There are users, who can post some thoughts. Each post contains text. Each user can create many different posts. Users can follow each other.

## Architecture
This project has been split to Monolithic and Microservices directories. Monolithic contains files of service before it was split to individual microservices - User Microservice, Post microservice and API Gateway, which is routing requests to Social Posting service to proper microservices.

## Usage
To deploy Social Posting application use provided `docker-compose.yml`, by running
```bash
docker-compose up
```
when in SocialPosting directory. This will generate docker network and 4 docker containers - frontend web server, backend API gateway and 2 containers for each micro service running in the backend.

Frontend web server is reachable on `localhost:8084`. You can also make HTTP requests directly to API gateway through reverse proxy, which is running on `localhost:8080`.
When making requests to backend make sure to use exposed port (`localhost:8080`), not internal docker name, as host is unable to resolve this name.

If you want to make any changes to any of the backend services make sure to later package it to jar.
```bash
mvn package
```

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