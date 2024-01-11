# Distributed map


## How does the system work

Implemented  a system that is intended for accepting and storing the Map<K, V> objects in distributed way.
Each command from client is transferred to server in plain text, validation is on the server side.

messages.MessageHandler.handler transform the text message from client to the Message object.

client.TcpLcient.getChannel is distribution function for messages.


# Extra-adds to the task specification:
1. In case of wrong command the server sends "Unsupported operation" message.
2. In case of wrong key/value format of PUT command the server sends "Correct format for PUT command is PUT key:value" message.
3. In case of wrong key/value format of GET command the server sends "Correct format for GET command is GET key" message.
4. In case of successful connection to the server message "Successfully connected to host:port" is shown for each worker.
5. In case of unsuccessful connection to the server message "Error while connecting to host:port. Connection to this worker was refused." is shown.
6. In case of SHUTDOWN command each client shows message from each server.

## Connection properties

# File application.yml contains list of servers for client run
Example format for one worker: 
  - host: hostN
    port: portN
# File docker-compose.yml contains list of servers for server run
Example format for one worker: 
  workerN:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "portN:8080"

## Documentation, websites, papers used
https://www.baeldung.com/java-dockerize-app
https://docs.docker.com/language/java/build-images/
https://github.com/eugenp/tutorials/tree/master/libraries-server/src/main/java/com/baeldung/netty
4 whole days was spend on this exercise
