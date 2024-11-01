### Checking out WebSockets w/ Springboot w/o STOMP ###

I wanted to check out WebSockets. Unfortunately all tutorials use STOMP what I decided not to use in this simple scenario.
After some trial and error I came with the solution like in the `websocket` source folder.
Since I'm using the serverside `thymeleaf` to render the pages, websockets are used only to signal that 
there is a new post and the browser should reload. Works surprisingly good, but in the end a one page javascript app is more suitable for websockets.

since im using h2 in memory database all posts are lost at shut down of.

This project is not ment for production. For production at least is missing:
- security
- persistent db
- proper javascript websocket client app implementation 
- not using aop

#### Frameworks ####
+ java 23
+ h2 in memory database
+ spring-boot
+ spring jpa
+ spring-boot-security
+ thymeleaf
+ bootstrap css
+ lombok
+ spring boot aop

#### AOP

I use aop for tracing the entry and exit of executed methods. This helps a lot of understanding whot is called when in spring.

see package `com.rsh.probe.chat.aop`.

which methods are traced one can determin in the `application.properties` file.

#### Features ####
+ user registration
+ user authentication using username/password
+ creating posts
+ serving multiple users and showing all posts
+ replication of post to all logged-in users via `websockets`

#### Development, Testing and Deployment ####
this is a standard Maven (see [Maven How To's](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)) project created with spring boot initializer.
use `./mvnw compile` etc.

During development, you can use `./mvnw spring-boot:run`.
