# Social Posting Frontend
## Overview
This is frontend for Social Posting service. It is written in typescript and uses webpack to compile .ts files into one bundled .js file for browser to use.  

It enables user to perform all actions he/she would like to do. User can create account, log in to his/her account, log out of account, delete it. Users can also follow and unfollow each other, add posts, edit posts and delete them later.  

It currently uses none secure login system, which is very easy to break - just add proper email to session storage in browser to hack into someone's account.  


## Usage
This repository is missing all node modules so make sure to run `npm install` when in SocialPostingFrontend folder.  

To build this project into javascript that browser can handle run `npm run build`. To host a server for development and testing run `npm run dev`.  

If you want to make debugging of typescript code easier change `mode: 'production'` to `mode: 'development'` in `webpack.config.js`

## Possible future improvements
* Add sorting of posts by creation time (oldest last)
* Change login system to be actually secure
* Add feed of posts from followed users on main page, when user is logged in