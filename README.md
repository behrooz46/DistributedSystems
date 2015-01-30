#Lab 0: Infrastructure           Behrouz Rabiee

## Design
Each node (MessageParser) listens on a specific port for incoming connections. In the same time it tries to connect to all other nodes.
Basically each node **listnes** and **speaks** at the same time.

If two node try to connect at the same time, they both won't accept the incoming connection and wait an arbitrary amount of time to try again.

This approach will give us the flexibility, that even if a node disconnects and connects again all other nodes will be notified and our distributed systems resume its work.

## File Structure
    .
    |- app (Appliction)
    |
    |- infrastructure (MessageParser & Core Connections)
      | - config (config file & rules)
      | - handshake (keeping our distributed system connected)
      | - message (Message & Header)

## How to run
Run application.java
* Either type the node name & file address.
* Or pass them as arg[0] & arg[1] to the program

Then type "help" for list of commands available, or:
* help
* send node type data
* get
* whoami
* exit
