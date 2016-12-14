# SPRING BOOT SAMPLE

The task: using Java & Spring Boot, create a RESTful, JSON-based web service that
keeps a String state per person (per browser).

The initial state for new user is empty String ""

## requirements:

- do not use cookies
- each operation logs what it is doing in a log/console, e.g. userID:  “ab85c56a”, added: “a”, 2 times
- two different browsers from single computer are two different users
- provide an instruction how to install your solution (including dependencies) and start it (on linux or mac)

## supported operations:

- GET /state - returns the current state

- GET /sum - sums all numbers in a string, e.g. “5abc141def” returns 146, if
there are no numbers return 0

- GET /chars - shows the current state without numbers, e.g. “5abc141def”
returns abcdef

- POST /chars - adds the character/s to the string state, e.g. with JSON
input {“character”:”a”,”amount”:3} adds “aaa” to the state string

- DELETE /chars/<character> - deletes the last occurrence of the character in
the state string

## web services requirements:

- return 400 if the POST request contains invalid JSON
- character in DELETE has to be a single alphanumeric character, otherwise return 400
- character in POST request has to be just one alphanumeric character and amount a number from 1 to 9, otherwise return 400
- if the length of the string state will exceed 200 characters after the POST request, do not change the state and return 400
- wrong url returns 404
- each valid JSON response (200 class) includes a user hash/id

## bonus points:

- put your solution on GitHub (personal account)
- write unit tests
- use a database to store the state

## Notes on this implementation
- Fairly simple unit testing only.  In order to allow automatic testing with curl, the property setting
   application.uniqueness=IPaddress allows multiple connections from one machine.  This is in
   violation of the specification, so should only be used for testing.
- No database.  A database is crucial for production because it allows the server to be updated without
   loss of state.
- No validation/sanitisation of input.
- I could have just followed https://stormpath.com/blog/tutorial-crud-spring-boot-20-minutes but
   only found it when my solution was near complete.

## Installation and running on Gentoo Linux
- sudo emerge -q1 maven-bin
- git clone spring-sample
- cd spring-sample
- mvn spring-boot:run

then connect from a browser to http://localhost:8080 or run unit-test.sh
