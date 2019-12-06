# Prova Finale Ingegneria del Software 2019
## Gruppo AM18

- ###    Davide Collovigh ([@Dav-11](https://github.com/Dav-11))<br>davide.collovigh@mail.polimi.it
- ###    Alessandro Contini ([@Contedigital](https://github.com/Contedigital))<br>alessandro3.contini@mail.polimi.it
- ###    Federico Dei Cas ([@FedericoGianni](https://github.com/FedericoGianni))<br>federico.deicas@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
| Complete rules |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
<!--| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |-->

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

# Adrenaline


## Server
server.jar


### without parameters 
```
java -jar server.jar
```

it starts the server with the default configuration: startUpServerConfig.json, placed in the same folder as the jar.
If this file doesn't exist, it will start the game with default args, and export the config in the folder.

This is the only way to init the game with custom min players or/and timer

### with 1 parameter
```
java -jar server.jar socket_port
```

this init takes only the socket port and sets all the other values to default

### with 3 parameters
```
java -jar ./server.jar socket_port rmi_server_port rmi_client_port
```

this init takes the socket port, the rmi register port for server and client, and sets the other parameters to default 



## Client
client.jar

### without parameters 
```
java -jar client.jar
```

Starts the client with the default config read from startUpClientConfig.json, placed in the same folder as the jar.
If this file doesn't exist, it will start the game with default args, and export the config in the folder.

**WARNING:** this init method, if there is no json file in the folder, will launch the client w/ server ip localhost

### with 1 parameters
```
java -jar client.jar server_ip
```

This method will take only the server ip and sets all the other param to default

### with 3 parameters
```
java -jar client.jar server_ip socket_port user_interface
```

This method will take only server ip, socket port, ( "-cli" for cli, "-gui" for cli ) and sets all the other param to default

### with 5 parameters
```
java -jar client.jar server_ip socket_port user_interface rmi_server_port rmi_client_port
```

This method will take only server ip, socket port, ( "-cli" for cli, "-gui" for cli ), rmi registry port for server and client, and sets all the other param to default

### toAdd in case of gui

in the case the user wants to launch the program with a gui it is required to add before the parameters specified above :
```
java -jar --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml 
```

changing $PATH_TO_FX to the path to the javafx-sdk-11.0.2/bin folder

the project has been tested on javafx-sdk-11.0.2

## Note

Whatever init chosen the jar will create a json file in the same folder whit the current config, but this will be read only in the startup process, and every modification done to it will not be effective while the program is running.
The file will also only be read if the jar is launched with no args, otherwise the non specified parameters will be set to default

The default param are:
 
* server ip is set to localhost
* default socket port is 22222, 
* rmi register default server port is 22220
* rmi register default client port is 22221
* ui default is gui
* min player default is 3
* default timer is 30 sec
* default timer state is enabled

the timer can be disabled by launching the server jar w/ no args and by setting the relative field in the jar to false

## Persistence

### Saved games
Every game will be saved in the root folder where the executable jar is started, at the end of every turn, with a different ID ( one id for game, every turn the saved file for a given game are overwritten with the current changes).

The game will save 4 json files for game + a sheared file across all to keep the reference to all of them:

for each game started in the folder will be written:

* savegames_game_id_**x**_Map.json
* savegames_game_id_**x**_Players.json
* savegames_game_id_**x**_Controller.json
* savegames_game_id_**x**_CurrentGame.json

**x** will be the game id 

there will also be a shared file:

* savegames_games.json

that will contain the list of the games

### How to load a saved game

To load a saved game, edit the StartUpServerConfig file "game" parameter with the ID of the game you wish to reload from file.

**-1** is the default value to start a new game

Once the server has started, it will wait for at least MIN_PLAYER that were in the saved game to reconnect by typing their name. 

Once every player present in the saved game has reconnected (you can read the saved names in the file: savegamesgame_Id_X_Players.json)
the game will start again from the point it was at the time of save.
