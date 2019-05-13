# Hard Deadline 14/05/2019  
# Communication

### Introduction
For the Communication protocol design we have chosen to have an async bidirectional connection between the clients and server, so that
a client can send information to the server and the server can send information to the client without a specific sequence.
That way evey time the model, which is inside the server, is updated, it updates a virtual view (server side). The virtual view then passes the informations
throught newtork to udpate the real view that are local to every client.

#### RMI
For the RMI flow:
##### LOGIN:
the clients login and registers itself by calling a function with the IP address and the bounded name as a parameter
##### TURN:
the server has a map bounding the playerId to the correspondent client and calls the functions on the current one
##### UPDATES:
updates are sent like socket with Json objects passed as parameters

#### Socket
Both client and server can send to each other a string formatted in this output: "header\fparam1\fparam2". The string is then sent and desirialized by the receiver.
The receiver splits the String every time it encounters the special character '\f' produces an array of Strings, and look for the function associated to the header. The other parameters are used as parameters to call that function. The list of possible function is stored in a HashMap, one inside client and one inside server. the function called by the clients are then binded to a function invocation inside the controller, which is accessible by a static attribute inside the server.
The socket which connect a single client to the socket server is handled with 4 threads, 2 for each stream of client and server: 2 for input stream and ouput stream for client, and the other 2 for the server. That way a thread for each side can be kept on listening for incoming messages and process them by calling the function associated to that thread inside the HashMap, both sides. The other thread is instead used to send messages to the output stream of the socket. This way both client and server are always listening for incoming messages and can send messages, working in an asyncronus way.
Here we designed the list of messages which will be sent by client and server in the game flow, following the cronological order of the game phases.

##### Login phase
     ┌──────┐                    ┌──────┐
     │Client│                    │Server│
     └──┬───┘                    └──┬───┘
        │          connect          │    
        │ ──────────────────────────>    
        │                           │    
        │            ping           │    
        │ <──────────────────────────    
        │                           │    
        │            pong           │    
        │ ──────────────────────────>    
        │                           │    
        │           login           │    
        │ ──────────────────────────>    
        │                           │    
        │         login\fOK         │    
        │ <──────────────────────────    
        │                           │    
        │ login\fNAME_ALREADY_TAKEN │    
        │ <──────────────────────────    
        │                           │    
        │ login\fCOLOR_ALREADY_TAKEN│    
        │ <──────────────────────────    
        |                           |
        |                           |
        |                           |

After a client connects to the server, every XX milliseconds the server will send a "ping" message to the client to check if it is still connected. once the client receives the "ping", it will reply to the server with a "pong" message to acknowledge the server that it is still online.

In the first phase the client is asked to choose a name and a color to join the Waiting Room, which is a game lobby to gather clients before game starts. The Waiting Room waits for at least 3 clients to connect and let them choose name and color.
After at least 3 clients has been connected, the Waiting Room starts a timer. When it expires the game begins with the number of player who have correctly done the login procedure before the timer end.
If the login has been completed succesfully then the server reply back to the client with a "login\fok" string. If not, it can send back to the client a few messages, like "login\fNAME_ALREADY_TAKEN" or "login\fCOLOR_ALREADY_TAKEN". When those reply messages are read by the client, it invokes a method to retry the login.

##### ping-pong protocol focus:
runs in a separate thread handled by the server, which sends to every client a ping message every XX milliseconds. The client receives the ping message,
looks for a function associated with that string inside its HashMap of function, find the one associated with that header and executes it, replying back to the server
a pong message, to acknowledge the server that it is stil online.
This protocol is useful to handle player disconnections.


     ┌──────┐          ┌──────┐
     │Server│          │Client│
     └──┬───┘          └──┬───┘
        │       ping      │    
        │ ────────────────>    
        │                 │    
        │       pong      │    
        │ <────────────────    
     ┌──┴───┐          ┌──┴───┐
     │Server│          │Client│
     └──────┘          └──────┘




##### Reconnect a player after disconnect
     ┌──────┐               ┌──────┐
     │Client│               │Server│
     └──┬───┘               └──┬───┘
        │  "reconnect\f(nome)" │    
        │ ─────────────────────>    
        │                      │    
        │ "reconnect\fok\f(ID)"│    
        │ <─────────────────────    
        │                      │    
        │   "reconnect\f!ok"   │    
        │ <─────────────────────    
     ┌──┴───┐               ┌──┴───┐
     │Client│               │Server│
     └──────┘               └──────┘
This phase is used to reconnect a player after it has disconnected from the server. The client send a String which contains his name and the server reply back
with ok and the id of the player if the reconnect procedure has been performed succesfully, otherwise it send a !ok parameter in the reply.

#### Start Game
     ┌──────┐           ┌──────┐
     │Server│           │Client│
     └──┬───┘           └──┬───┘
        │ "gamestarted\fid"│    
        │ ─────────────────>    
        │                  │    
        │ "gamestarted\ok" │    
        │ <─────────────────    
     ┌──┴───┐           ┌──┴───┐
     │Server│           │Client│
     └──────┘           └──────┘

When the Waiting Room timer expires (if there are 3 clients connected) or 5 clients have succesfully completed the login phase, the Server will send to every connected client a String which informs that the game is started, followed by the ID of every player.    

##### Client receives JSON representing info which has to be saved locally in the simplified model
     ┌──────┐           ┌──────┐
     │Server│           │Client│
     └──┬───┘           └──┬───┘
        │ "JSON[model]"    |
        │ ─────────────────>    
        │                  │    
        │                  |
        │                  |
     ┌──┴───┐           ┌──┴───┐
     │Server│           │Client│
     └──────┘           └──────┘
After the game has started every client will receive a JSON file including the information needed to populate the class which stores a simplified
version of the model. The JSON file will contain info useful before starting the game flow, like the ammo placed inside map and the weapons avaiable
in the spawn cells. This protocol also works every time the model is updated: as the model is observable, a remote view for each client stored in the server
is informed every time an observable class of the model is updated. The remote view then handles this update by informing, via the network, the update to the
real view (the client). The update is passed as a json since it is easier to serialize class elements this way and it is more language indipendent.

Here's an example of how a json file containing information about the model would be implemented. The client will receive those json, deserialize it and
populate its simplifed model class accordingly to the data.
{"score":10,"deaths":2,"marks":[0],"dmgTaken":[0,0,1],"currentPosition":{"x":1,"y":1}}

#### TURN
The turn is divided in 6 phases. The server informs the current player that his turn has started by sending a String "yourTurn\fphase0". That function will then
invoke a method of the user interface class to display a console asking the player what he wants to do.

##### phase 0 pickPowerUp to determin spawn point place
     ┌──────┐                                                          ┌──────┐
     │Server│                                                          │Client│
     └──┬───┘                                                          └──┬───┘
        │                        "yourTurn\fphase0"                       │    
        │ ────────────────────────────────────────────────────────────────>    
        │                                                                 │    
        │ "NOTIFY "updates simplifed model of the client"                 │    
        │ ────────────────────────────────────────────────────────────────>    
        │                                                                 │    
        │    "yourTurn\fphase0\fnomepupdascartare\fcolorepupdascartare"   │    
        │ <────────────────────────────────────────────────────────────────    
     ┌──┴───┐                                                          ┌──┴───┐
     │Server│                                                          │Client│
     └──────┘                                                          └──────┘

##### phase1 If client wants to use pickPowerUp
     ┌──────┐                                                 ┌──────┐
     │Server│                                                 │Client│
     └──┬───┘                                                 └──┬───┘
        │                   "yourTurn\fphase1"                   │    
        │ ───────────────────────────────────────────────────────>    
        │                                                        │    
        │ "yourTurn\fphase1\fuse\fnomepupda\fcolorepupdascartare"│    
        │ <───────────────────────────────────────────────────────    
        │                                                        │    
        │                "yourTurn\fphase1\f!use"                │    
        │ <───────────────────────────────────────────────────────    
     ┌──┴───┐                                                 ┌──┴───┐
     │Server│                                                 │Client│
     └──────┘                                                 └──────┘

For the Action phase we want to use a single JSON file to encode the various different types of actions which can be performed by a client, instead of
having a header string for every different action. That way the server will receive and deserialize the action, perform it on the controller and if valid
(there will still be some validation inside the client) reply back ok, otherwise it will reply with !ok, invoking a method inside the user interface to let
him redo the action (there will be a time limit for every turn).

##### phase2 [ACTIONS] possible actions coded into a JSON file.
     ┌──────┐                 ┌──────┐
     │Server│                 │Client│
     └──┬───┘                 └──┬───┘
        │   "yourTurn\fphase2"   │    
        │ ───────────────────────>    
        │                        │    
        │     "JSON[ACTION]"     │    
        │ <───────────────────────    
        │                        │    
        │ "yourTurn\fphase2\fok" │    
        │ ───────────────────────>    
        │                        │    
        │ "yourTurn\fphase2\f!ok"│    
        │ ───────────────────────>    
     ┌──┴───┐                 ┌──┴───┐
     │Server│                 │Client│
     └──────┘                 └──────┘

here's some example of how a json file containing an action would be:
{"type":"MOVE","moves":["n","n","w"],"finalPos":{"x":2,"y":2}}

##### phase3
same as phase1, but if the previous action was shoot, the target can use takeback granade and the shooter can continue
with the phase3 of his turn, in which he decide if use or not a powerup.

     ┌──────┐                                                 ┌──────┐
     │Server│                                                 │Client│
     └──┬───┘                                                 └──┬───┘
        │                   "yourTurn\fphase3"                   │    
        │ ───────────────────────────────────────────────────────>    
        │                                                        │    
        │ "yourTurn\fphase3\fuse\fnomepupda\fcolorepupdascartare"│    
        │ <───────────────────────────────────────────────────────    
        │                                                        │    
        │                "yourTurn\fphase3\f!use"                │    
        │ <───────────────────────────────────────────────────────    
     ┌──┴───┐                                                 ┌──┴───┐
     │Server│                                                 │Client│
     └──────┘                                                 └──────┘


At the same time the shooted player will receive a string message that informs the client that he can use a powerup.

     ┌──────┐          ┌──────┐
     │Server│          │Client│
     └──┬───┘          └──┬───┘
        │    "granade"    │    
        │ ────────────────>    
        │                 │    
        │  "granade\fok"  │    
        │ <────────────────    
        │                 │    
        │  "granade\f!ok" │    
        │ <────────────────    
     ┌──┴───┐          ┌──┴───┐
     │Server│          │Client│
     └──────┘          └──────┘

##### phase4

     ┌──────┐                 ┌──────┐
     │Server│                 │Client│
     └──┬───┘                 └──┬───┘
        │   "yourTurn\fphase4"   │    
        │ ───────────────────────>    
        │                        │    
        │     "JSON[AZIONE]"     │    
        │ <───────────────────────    
        │                        │    
        │ "yourTurn\fphase4\fok" │    
        │ ───────────────────────>    
        │                        │    
        │ "yourTurn\fphase4\f!ok"│    
        │ ───────────────────────>    
     ┌──┴───┐                 ┌──┴───┐
     │Server│                 │Client│
     └──────┘                 └──────┘

##### phase5

     ┌──────┐                                                 ┌──────┐
     │Server│                                                 │Client│
     └──┬───┘                                                 └──┬───┘
        │                   "yourTurn\fphase5"                   │    
        │ ───────────────────────────────────────────────────────>    
        │                                                        │    
        │ "yourTurn\fphase5\fuse\fnomepupda\fcolorepupdascartare"│    
        │ <───────────────────────────────────────────────────────    
        │                                                        │    
        │                "yourTurn\fphase5\f!use"                │    
        │ <───────────────────────────────────────────────────────    
     ┌──┴───┐                                                 ┌──┴───┐
     │Server│                                                 │Client│
     └──────┘                                                 └──────┘

At the same time the shooted player will receive a string message that informs the client that he can use a powerup.

     ┌──────┐          ┌──────┐
     │Server│          │Client│
     └──┬───┘          └──┬───┘
        │    "granade"    │    
        │ ────────────────>    
        │                 │    
        │  "granade\fok"  │    
        │ <────────────────    
        │                 │    
        │  "granade\f!ok" │    
        │ <────────────────    
     ┌──┴───┐          ┌──┴───┐
     │Server│          │Client│
     └──────┘          └──────┘

##### phase6 RELOAD up to 3 times (max 3 weapons)
uso reload\f!ok used also to end the player's turn

     ┌──────┐                            ┌──────┐
     │Server│                            │Client│
     └──┬───┘                            └──┬───┘
        │              "reload"             │    
        │ ──────────────────────────────────>    
        │                                   │    
        │ "reload\fok\fNUM.armadaricaricare"│    
        │ <──────────────────────────────────    
        │                                   │    
        │          "reload\f!ok\f"          │    
        │ <──────────────────────────────────    
     ┌──┴───┐                            ┌──┴───┐
     │Server│                            │Client│
     └──────┘                            └──────┘


Alternative. have 6 encoding string which represent the different actions which can be done by that player.
##### Movement

     ┌──────┐          ┌──────┐
     │Client│          │Server│
     └──┬───┘          └──┬───┘
        │   "move\fx\fy"  │    
        │ ────────────────>    
        │                 │    
        │    "move\fOK"   │    
        │ <────────────────    
        │                 │    
        │   "move\f!OK"   │    
        │ <────────────────    
     ┌──┴───┐          ┌──┴───┐
     │Client│          │Server│
     └──────┘          └──────┘
