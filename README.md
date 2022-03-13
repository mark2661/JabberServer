# JabberServer
A multithreaded back-end server for "Jabber" a Twitter clone designed for a university project. This server has been designed to be compatible with the client side desktop application "JabberClient".
## What I Learned
* Databse Design using [PostgreSQL](https://www.postgresql.org/)
* API Development using Java Database Connectivity
* Mulithreaded server development with [Java.net](https://docs.oracle.com/javase/7/docs/api/java/net/package-summary.html)
* Automated unit testing using [JUnit 5](https://junit.org/junit5/)
## Features
* Multithreaded server allows for multiple simultaneous client connections
* Ability to login and logout of a personal account (Session history is saved upon logging out / losing connection).
* Ability to register new user accounts.
* Ability to follow a user. Which adds any of their new post to your timeline.
* Ability to like post from other users.
* Users timeline will automatically update when a followed users post "Jabs" (tweet equivalent) or like the posts of a mutually followed user.
## Example Usage (Using the JabberClient desktop application)
```Java
JabberServer/src/com/bham/fsd/assignments/jabberserver/StartServer.java
```
### Enter a new user name to register (or an existing user name to sign-in)
|   |  |
| ------------- | ------------- |
| ![alt text](https://github.com/mark2661/JabberServer/blob/main/Images/sign-in%20screen.PNG)  |![alt text](https://github.com/mark2661/JabberServer/blob/main/Images/register%20success.PNG) |
|   |  |


### Post a message to your timeline for your followers to see
![alt text](https://github.com/mark2661/JabberServer/blob/main/Images/post.PNG)
### Follow other users by clicking the "plus" icon next to their name
![alt text](https://github.com/mark2661/JabberServer/blob/main/Images/follow.PNG)
### Like your friends timeline posts (click the "heart" icon)
![alt text](https://github.com/mark2661/JabberServer/blob/main/Images/like.PNG)
## Future Updates
