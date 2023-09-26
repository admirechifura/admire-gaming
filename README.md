# Kalaha/Mancala Game

I tried to follow SOLID principles which are object-oriented design concepts relevant to software development.

Single responsibility,Open-closed,Liskov substitution,interface segregation and dependency inversion principles where i found applicable.

This springboot application has java backend and simple frontend(mustache)
- To start the game run local instance of application using this command `mvn spring-boot:run`
- Open your browser and go to the url: http://localhost:8081/
- Login using username and password stored in application.properties file
## Build

- Compile: `mvn compile`
- Compile and run tests: `mvn package`
- Run specific test: `mvn package -Dtest=TestClassName#methodName`
- Run and install: `mvn clean install`
- Run local instance of application `mvn spring-boot:run`
- Login using username and password stored in application.properties file

## Security

Kalaha Game has login page with username and password configured in the properties file

## Improvements to be considered in backlog

-Add storage for the results of each player

-Add admin page to administer the game by adding players

-Add leading scores and player name

-Add swagger documentation secured with jwt tokens for endpoints where there is need for restricted access

-Encrypt password for user in application.properties file e.g using jasypt
