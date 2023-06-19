# VSY Project - Chat Client

This part of the project is a thin client, that automatically connects to a live chat server and provides a simple GUI. 
After authentication, the UI presents the user with a contact list and a menu bar providing options for contact addition or removal, as well as a tabbed window containing active chats and a box for user text input for the currently active chat. 
As long as the client does not close the GUI, the client won't stop trying to connect to a chat server, if the connection is lost.
The logical core of the client is a thread called PacketProcessingService that processes incoming packets, updating local data and updates the GUI if needed.
# Motivation

This is the client side of a project created to acquire an attestation required for admittance to the VSY (de: verteilte Systeme;en: distributed systems) exam at Niederrhein University of Applied Sciences.
The programming language Java was chosen for training purposes.
# Code style

The code style used is the Google's Java Style.
# Tech/framework used

**Built with**
* [Log4J2](https://logging.apache.org/log4j/2.x/)  
* [JUnit 5](https://junit.org/junit5/)
**Built using**
* [Maven](https://maven.apache.org/)
****
# Tests

Most tests are written as black box tests, checking the server's correct behaviour for certain inputs. The tests are separated by packages, indicating the amount of servers that need to be manually started for the tests to work correctly.
# How to use?

The chat server, once built with maven, can be started by calling java -jar server-{version}-SNAPSHOT-jar-with-dependencies.jar.
# License

None at the moment.
