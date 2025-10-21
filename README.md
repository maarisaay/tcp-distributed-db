# Distributed Database Prototype (Java)

This project is a prototype of a simple distributed key-value database implemented in Java. It demonstrates client-server communication using TCP sockets and simulates distributed data management across multiple database nodes.

## Key Features
- Client-server communication over TCP (via `Socket`, `BufferedReader`, `PrintWriter`)
- Key-value data storage with CRUD operations (`set-value`, `get-value`, `find-key`, `get-max`, `get-min`, etc.)
- Node communication and coordination (`join-network`, `leave-network`, `terminate`)
- Modular structure with support for multiple database nodes and client handlers
- Threaded handling of multiple client connections using `ExecutorService`
- Command-line client interface for performing operations

## Project Structure
- `DatabaseNode.java` - core logic and storage of a node in the distributed system
- `ClientHandler.java` - handles individual client connections via threads
- `CommunicationModule.java` - module for inter-node communication
- `DatabaseClient.java` - a terminal-based client for interacting with nodes
- `NodeInfo.java` - helper class storing IP and port for each node
- `Logger.java` - basic logger setup
- `Server.java` - server bootstrap logic

## Sample Commands
```bash
# Starting a node
java DatabaseNode 12345

# Connecting client to the node
java DatabaseClient -gateway localhost:12345 -operation set-value 1:100
