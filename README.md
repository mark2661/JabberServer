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
## Future Updates
