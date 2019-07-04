# Prova Finale Ingegneria del Software 2019
## Gruppo AM18

- ###   10522289    Davide Collovigh ([@Dav-11](https://github.com/Dav-11))<br>davide.collovigh@mail.polimi.it
- ###   10538919    Alessandro Contini ([@Contedigital](https://github.com/Contedigital))<br>alessandro3.contini@mail.polimi.it
- ###   10498291    Federico Dei Cas ([@FedericoGianni](https://github.com/FedericoGianni))<br>federico.deicas@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
| Complete rules |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#)|
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

# Adrenaline


## Server
Server.jar

### without parameters 
it starts the server with the default configuration: startUpServerConfig.json, placed in the same folder as the jar.
If this file doesn't exist, it will create one and place it in the same folder. If the file is there, it will read config from the json.
### with parameters
java -jar ./server.jar socket_port rmi_server_port rmi_client_port

the default server ip is set to localhost, default socket port is 22222, rmi default client port is 22220 and client port is 222221.


## Client
Client.jar
### without parameters 
starts the client with the default config read from startUpClientConfig.json, placed in the same folder as the jar.
If this file doesn't exist, it will create one and place it in the same folder. If the file is there, it will read config from the json.

the default server ip is set to localhost, default socket port is 22222, rmi default client port is 22220 and client port is 222221. the default UI is set to gui.

### with parameters
note that you can specify only port and user interface, then it will read server_ip from the json file

#### CLI
java -jar ./client.jar server_ip port -cli (rmi_server_port) (rmi_client_port)

### GUI 
java -jar ./client.jar server_ip port -gui (rmi_server_port) (rmi_client_port)
