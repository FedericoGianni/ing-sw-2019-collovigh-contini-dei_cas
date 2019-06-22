package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.GrabAction;
import it.polimi.ingsw.view.actions.Move;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;



import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GuiMapController {

    private static Gui gui;
    private int rows=3,col=4;
    private  VBox map[][]=new VBox[rows][col];
    public static void setGui(Gui g) {
        gui = g;
    }
    private ArrayList <Directions> movementDirections;
    private int validMove=-1;

    @FXML
    BorderPane pane;
    @FXML
    TextArea log;
    @FXML
    GridPane innerMap;

    @FXML
    VBox b00,b01,b02,b03,b10,b11,b12,b13,b20,b21,b22,b23;
    @FXML
    ImageView powerUp1,powerUp2,powerUp3,weapon1,weapon2,weapon3;

    @FXML
    Button stopMov,moveButton,grabButton;


    //-------------------------------------------------------MAP CREATION and gestion methods
    @FXML
    public void initialize() {

    }

    private Point getPlayerPosFromServer(int id)
    {
        return new Point(gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosX(),gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosY());


    }

    public void mapCreator()
    {


        switch(gui.getView().getCacheModel().getMapType()) {
            case 1: {
                innerMap.setStyle("-fx-background-image: url('/images/Map1in.png')");//use this for sample fot he
                buttonCreator();
                 b03.setOnMouseClicked(new EventHandler<MouseEvent>(){
                     @Override
                     public void handle(MouseEvent mouseEvent) {

                     }
                 });
                b20.setOnMouseClicked(new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });

                break;
            }
            case 2: {
                buttonCreator();
                innerMap.setStyle("-fx-background-image: url('/images/Map2in.png')");//use this for sample fot he maps everytime
                b20.setOnMouseClicked(new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });
                break;
            }
            case 3: {
                buttonCreator();
                innerMap.setStyle("-fx-background-image: url('/images/Map3in.png')");//use this for sample fot he
                break;
            }

        }
        weaponSeeEventEnabler();
    }
    private void weaponSeeEventEnabler()
    {
        b02.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {

                spawnCellWeaponShow(0,2);
            }
        });



        b10.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {

                spawnCellWeaponShow(1,0);
            }
        });
        b23.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {

                spawnCellWeaponShow(2,3);
            }
        });
    }
    private void buttonCreator()
    {
        for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS) ; // allow row to grow
            rc.setFillHeight(true); // ask nodes to fill height for row

            innerMap.getRowConstraints().add(rc);
        }
        for (int colIndex = 0; colIndex < 4; colIndex++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS) ; // allow column to grow
            cc.setFillWidth(true); // ask nodes to fill space for column

            innerMap.getColumnConstraints().add(cc);
        }

        b00.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b01.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b02.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b03.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        b10.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b11.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b12.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b13.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        b20.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b21.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b22.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b23.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        map[0][0]=b00;
        map[0][1]=b01;
        map[0][2]=b02;
        map[0][3]=b03;

        map[1][0]=b10;
        map[1][1]=b11;
        map[1][2]=b12;
        map[1][3]=b13;

        map[2][0]=b20;
        map[2][1]=b21;
        map[2][2]=b22;
        map[2][3]=b23;
    }

    @FXML
    public void actionButtonsEnabler()
    {
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move();
            }
        });

        grabButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                grabHere();
            }
        });
    }

    @FXML
    public void actionButtonDisable()//disable action buttons
    {
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        stopMov.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        mapEventDeleter();
    }

    private void mapEventDeleter()//disable map events except for spawn see, it renables it
    {
        for(int i=0;i<rows;i++)//reset buttons on the map to do nothing
        {
            for(int j=0;j<col;j++)
            {
                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

                    }

                });
            }
        }
        weaponSeeEventEnabler();
    }

    public void printLog(String s)
    {
        log.appendText("\n"+s);
    }

    //------------------------------------------------------------Weapons show methods
    private void spawnCellWeaponShow(int r,int c)
    {
        //prima di tutto quali sono le immagini
        weapon1.setFitHeight(156);
        weapon1.setFitWidth(100);
        weapon2.setFitHeight(156);
        weapon2.setFitWidth(100);
        weapon3.setFitHeight(156);
        weapon3.setFitWidth(100);
        for(int i=0;i<((CachedSpawnCell)gui.getView().getCacheModel().getCachedMap().getCachedCell(r,c)).getWeaponNames().size();i++)
        {
            String url=fromWNameToUrl(((CachedSpawnCell)gui.getView().getCacheModel().getCachedMap().getCachedCell(r,c)).getWeaponNames().get(i));
            weaponDisplayer(url,i);
        }
    }

    private String fromWNameToUrl(String name)
    {
        System.out.println(name);
        switch(name)
        {
            case "LOCK RIFLE":
                return "/images/weapons/distruttore.png";
            case "MACHINE GUN":
                return "/images/weapons/mitragliatrice.png";
            case "ELECTROSCYTHE":
                return "/images/weapons/falceProtonica.png";
            case "TRACTOR BEAM":
                return "/images/weapons/raggioTraente.png";
            case "T.H.O.R.":
                return "/images/weapons/torpedine.png";
            case "PLASMA GUN":
                return "/images/weapons/fucilePlasma.png";
            case "WHISPER":
                return "/images/weapons/fucilePrecisione.png";
            case "VORTEX CANNON":
                return "/images/weapons/cannoneVortex.png";
            case "FURNACE":
                return "/images/weapons/vulcanizzatore.png";
            case "HEAT SEEKER":
                return "/images/weapons/razzoTermico.png";
            case "HELLION":
                return "/images/weapons/raggioSolare.png";
            case "FLAMETHROWER":
                return "/images/weapons/lanciaFiamme.png";
            case "GRENADE LAUNCHER":
                return "/images/weapons/lanciaGranate.png";
            case "ROCKET LAUNCHER":
                return "/images/weapons/lanciaRazzi.png";
            case "RAILGUN":
                return "/images/weapons/fucileLaser.png";
            case "CYBERBLADE":
                return "/images/weapons/spadaFotonica.png";
            case "ZX-2":
                return "/images/weapons/zx2.png";
            case "SHOTGUN":
                return "/images/weapons/fucilePompa.png";
            case "POWER GLOVE":
                return "/images/weapons/cyberGuanto.png";
            case "SHOCKWAVE":
                return "/images/weapons/ondaUrto.png";
            case "SLEDGEHAMMER":
                return "/images/weapons/martelloIonico.png";

        }
        return null;
    }

    private void  weaponDisplayer(String url,int weapon)
    {
        Image img=new Image(url);

        switch (weapon)
        {
            case 0:
                weapon1.setImage(img);
                break;
            case 1:
                weapon2.setImage(img);
                break;
            case 2:
                weapon3.setImage(img);
                break;
        }

    }



    //-------------------------------------------------------------movements things
    @FXML
    private void move()
    {

        movementDirections=new ArrayList<>();
        int x=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosX();
        int y=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosY();
        handleMovement(x,y,3,movementDirections);
    }

    private void mapPos(int r,int c,int id)
    {
        //System.out.println("riga: "+r+"colonna :"+c);
        ;
        boolean found=false;
        if( map[r][c].getChildren().size()!=0 && ((HBox)map[r][c].getChildren().get(0)).getChildren()!=null)
        {for(int j=0;j<((HBox)map[r][c].getChildren().get(0)).getChildren().size();j++)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
        {
            if(((HBox)map[r][c].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id))==0)
            {
                found=true;
            }

        }
            if(found)return;}
        fromIDtoIMG(id, map[r][c]);
        log.appendText("\n Placed player "+id+" in cell "+r+c);


        //eliminating the powerups effects after the beginning
        powerUp1.setOnMouseClicked((e) -> {

        });
        powerUp2.setOnMouseClicked((e) -> {

        });
        powerUp3.setOnMouseClicked((e) -> {

        });

        //afetr move i delete moving things
        mapEventDeleter();

    }

    private void handleMovement(int x,int y,int m,ArrayList<Directions> movementDirections)//called from move,do stuff for real
    {
        Alert a=new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Move the Pawn in an adjacent cell click STOP on the left to stop the movements\n Remembre you have "+m+" moves left");
        a.show();
        int M=m-1;
        //enable button events

        stopMov.setOnAction(new EventHandler<ActionEvent>() {//stop button
            @Override public void handle(ActionEvent e) {
                gui.getView().doAction(new Move(movementDirections,new Point(x,y)));
                actionButtonDisable();
            }
        });

        //buttons here enable the movements in adjacent cells
        if(y<3)
        {map[x][y+1].setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("EAST",x,y)) {
                    movementDirections.add(Directions.EAST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y+1]);
                    eventMover(x,y+1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x<2)
        {map[x+1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("SOUTH",x,y))
                {   movementDirections.add(Directions.SOUTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x+1][y]);
                    eventMover(x+1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(y-1>=0)
        {map[x][y-1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("WEST",x,y)) {
                    movementDirections.add(Directions.WEST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y-1]);
                    eventMover(x,y-1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x-1>=0)
        {map[x-1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("NORTH",x,y)) {
                    movementDirections.add(Directions.NORTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x-1][y]);
                    eventMover(x-1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
    }


    private void eventMover(int x,int y,int m)
    {
        if(m==0)
        {
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("No more movements left. Moving...");
            a.show();
            gui.getView().doAction(new Move(movementDirections,new Point(x,y)));
            actionButtonDisable();
            return;
        }

        for(int i=0;i<rows;i++)//reset buttons on the map to do nothing
        {
            for(int j=0;j<col;j++)
            {
                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseevent) {

                    }

                });
            }
        }
        int M=m-1;
        //buttons here enable the movements in adjacent cells
        if(y<3)
        {map[x][y+1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("EAST",x,y)) {
                    movementDirections.add(Directions.EAST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y+1]);

                    eventMover(x,y+1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x<2)
        {map[x+1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("SOUTH",x,y))
                {   movementDirections.add(Directions.SOUTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x+1][y]);
                    eventMover(x+1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(y-1>=0)
        {map[x][y-1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("WEST",x,y)) {
                    movementDirections.add(Directions.WEST);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                    fromIDtoIMG(gui.getView().getPlayerId(),map[x][y-1]);
                    eventMover(x,y-1,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}
        if(x-1>=0)
        {map[x-1][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseevent) {
                if(moveValidator("NORTH",x,y)) {
                    movementDirections.add(Directions.NORTH);
                    playerRemover(gui.getView().getPlayerId(),x,y);
                   fromIDtoIMG(gui.getView().getPlayerId(), map[x-1][y]);
                    eventMover(x-1,y,M);
                }
                else{
                    eventMover(x,y,m);
                }
            }
        });}

    }
    /**
     * takes the id returns the string of the image
     * @param id
     * @return
     */
    private void fromIDtoIMG(int id,VBox b)
    {
        if(b.getChildren().size()==0 )
        {
            Platform.runLater(() ->  {
                b.getChildren().add(new HBox());

                inserter(id, (HBox) b.getChildren().get(0));

            });
            return;
        }
        if( ((HBox)b.getChildren().get(0)).getChildren().size()==3)
        {
            Platform.runLater(() ->  {
                b.getChildren().add(new HBox());
                inserter(id, (HBox) b.getChildren().get(1));
            });
            return;
        }

        if(((HBox)b.getChildren().get(0)).getChildren().size()<=3)
        {
            Platform.runLater(() ->  {inserter(id, (HBox) b.getChildren().get(0));});
            return;
        }
        Platform.runLater(() ->  {inserter(id, (HBox) b.getChildren().get(1));});

    }

    private void playerRemover(int id,int x,int y)
    {
        if(map[x][y].getChildren().size()==1)//primo HBOX
        {
            int j=0;

            while(((HBox)map[x][y].getChildren().get(0)).getChildren().get(j).getId().compareTo(Integer.toString(id))!=0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
            {
                j++;
            }
            ((HBox)map[x][y].getChildren().get(0)).getChildren().remove(j);
        }else{//secondo HBox stessa procedure di prima
            int j=0;

            while(((HBox)map[x][y].getChildren().get(1)).getChildren().get(j).getId().compareTo(Integer.toString(id))!=0)//devo rimuovere il giocatore che ha quell'id e allora lo cerco
            {
                j++;
            }
            ((HBox)map[x][y].getChildren().get(1)).getChildren().remove(j);
        }
    }
    private void inserter(int id,HBox h)
    {
        ImageView img=new ImageView();
        Image image;
        switch (id) {
            case 0:
                image=new Image("/images/player0.png");
                img.setImage(image);
                img.setId("0");
                h.getChildren().add(img);

                break;
            case 1:
                image=new Image("/images/player0.png");
                img.setImage(image);
                h.getChildren().add(img);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    public void setValidMove(int validMove) {
       this.validMove=validMove;
        synchronized(this) {
            this.notifyAll();
        }
    }
    private boolean moveValidator(String dir,int x,int y)//x and y are the arrive postions of the move dir is the direction
    {
         validMove =-1;
        gui.getView().askMoveValid(x, y, Directions.valueOf(dir));
        while(validMove == -1){
            try
            {
                synchronized(this) {
                    //System.out.println("Waiting to receive validMove reply...");
                    this.wait();
                }

            } catch (InterruptedException e) {

            }

        }

        if(validMove == 1) {
            log.appendText("\n Direzione valida!");
            return true;

        }else {//validmove=0
            Alert a =new Alert(Alert.AlertType.INFORMATION);
            log.appendText("\n direzione non vlida");
            return false;
        }

    }


//-------------------------------------------------------------loign methods and matche beginning
    @FXML
    public void loginUpdater(String name, int id, PlayerColor color)
    {
        log.appendText("\nSi è collegato: "+name+" con l'id: "+id+" ed il colore: "+color);
    }

    public void statsUpdater(int id)
    {//the player is removed from its postion before the update

        if(!gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getOnline())
        {
            log.appendText("\nIl giocatore "+id+" si è scollegato.");

            return;
        }
        log.appendText("\nUpdated stats del player: "+id);
        int r=gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosX();
        int c=gui.getView().getCacheModel().getCachedPlayers().get(id).getStats().getCurrentPosY();
        mapPos(r,c,id);
        stopMov.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) { }
        });


    }

    public void startSpawn()
    {
        //diplaye the powerUps in the player's screen
        powerUpDisplayer();

        Alert a=new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Choose a powerUp (on the right) to discard for the swpawn location");
        a.show();
        powerUp1.setOnMouseClicked((e) -> {//eliminate the effect
            powerUp1.setImage(null);
            Color c=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(0));
            mapPos(colorToCord(c).x,colorToCord(c).y,gui.getView().getPlayerId());
        });
        powerUp2.setOnMouseClicked((e) -> {
            powerUp2.setImage(null);
            Color c=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(1));
            mapPos(colorToCord(c).x,colorToCord(c).y,gui.getView().getPlayerId());
        });
        powerUp3.setOnMouseClicked((e) -> {
            powerUp3.setImage(null);
            Color c=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2).getColor();
            gui.getView().spawn(gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(2));
            mapPos(colorToCord(c).x,colorToCord(c).y,gui.getView().getPlayerId());
        });
    }

    //------------------------------------------------------------powerUp  gestion
    private Point colorToCord(Color c)
    {
        Point p=new Point();
           if(c==Color.BLUE)
           {
               p.x=0;
               p.y=2;
               return p;
           }
           if(c==Color.RED)
           {
               p.x=1;
               p.y=0;
               return p;
           }
           if(c==Color.YELLOW)
           {
               p.x=2;
               p.y=3;
               return p;
           }return p;
    }

    public void powerUpDisplayer()
    {
        powerUp1.setImage(null);
        powerUp2.setImage(null);
        powerUp3.setImage(null);
        PowerUpType pt;
        ArrayList <ImageView>powerUps;
        powerUps=new ArrayList<>();

        powerUps.add(powerUp1);
        powerUps.add(powerUp2);
        powerUps.add(powerUp3);
        powerUp1.setFitHeight(156);
        powerUp1.setFitWidth(100);
        powerUp2.setFitHeight(156);
        powerUp2.setFitWidth(100);
        powerUp3.setFitHeight(156);
        powerUp3.setFitWidth(100);
        Image image;

        Color c;
        int i=0;
        //iterates the poweUps of the player for letting themload on the guiMap
        while(i<gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().size()) {
            pt = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getType();
            c = gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getPowerUpBag().getPowerUpList().get(i).getColor();
            if (pt == PowerUpType.TELEPORTER) {
                if (c== Color.BLUE) {
                    image=new Image("/images/powerUp/teleportBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c==Color.RED) {
                     image=new Image("/images/powerUp/teleportRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/teleportYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else if (pt== PowerUpType.NEWTON) {
                if (c== Color.BLUE) {
                     image=new Image("/images/powerUp/kineticBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c==Color.RED) {
                    image=new Image("/images/powerUp/kineticRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/kineticYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else if (pt==PowerUpType.TAG_BACK_GRENADE) {
                if (c== Color.BLUE) {
                     image=new Image("/images/powerUp/venomBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c==Color.RED) {
                     image=new Image("/images/powerUp/venomRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/venomYellow.png");
                    powerUps.get(i).setImage(image);
                }
            } else//TARGETING_SCOPE
            {
                if (c== Color.BLUE) {
                     image=new Image("/images/powerUp/aimBlue.png");
                    powerUps.get(i).setImage(image);
                } else if (c== Color.RED) {
                     image=new Image("/images/powerUp/aimRed.png");
                    powerUps.get(i).setImage(image);
                } else//YELLOW
                {
                     image=new Image("/images/powerUp/aimYellow.png");
                    powerUps.get(i).setImage(image);
                }
            }
            i++;
        }
    }

    //--------------------------------------------------------------ammo gestion
    public void ammoPlacer()
    {
        //neeed to remove everything before!!!
        for(int r=0;r<rows;r++)
        {
            for(int c=0;c<col;c++)
            {
                if(gui.getView().getCacheModel().getCachedMap().getCachedCell(r,c)!=null)
                {
                    if(gui.getView().getCacheModel().getCachedMap().getCachedCell(r,c).getCellType().equals(CellType.AMMO))
                    {
                        final int rr=r,cc=c;
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                if(!containsAmmo(map[rr][cc]))
                                    placer(((CachedAmmoCell) gui.getView().getCacheModel().getCachedMap().getCachedCell(rr,cc)).getAmmoList(),map[rr][cc] );
                            }
                        });


                    }

                }
            }
        }
    }

    private void imageCreator(String imgUrl,HBox h)//ammo Id="ammo"
    {
        ImageView img=new ImageView();

        Image image = new Image(imgUrl);
        img.setImage(image);
        img.setId("ammo");
        h.getChildren().add(img);
    }

    private boolean containsAmmo(VBox b)
    {
        for(int i=0;i<b.getChildren().size();i++)
        {
            for(int j=0;j<((HBox)b.getChildren().get(i)).getChildren().size();j++)
            {

                if(((HBox)b.getChildren().get(i)).getChildren().get(j).getId().compareTo("ammo")==0)
                    return true;
            }
        }
        return false;
    }
    private void placer ( List<Color> a, VBox b)
    {
        String url;
        url=fromAmmoCubetoIMG(a);
        if(b.getChildren().size()==0  )//if i don't have the hbox
        {

                b.getChildren().add(new HBox());
                System.out.println("Creo il primo HBox ");
                imageCreator(url, (HBox) b.getChildren().get(0));
                System.out.println("contenuto primo hbox in runlater : "+b.getChildren().size());


            System.out.println("contenuto primo hbox dopo metodo runlater : "+b.getChildren().size());
            return;
        }

        if(((HBox)b.getChildren().get(0)).getChildren().size()==3){ //if the first Hbox is full

                b.getChildren().add(new HBox());
                System.out.println("creo seconddo Hbox");
                imageCreator(url, (HBox) b.getChildren().get(1));

            return;
        }
        if(((HBox)b.getChildren().get(0)).getChildren().size()<=3) //use the second HBox
        {
            imageCreator(url, (HBox) b.getChildren().get(0));
            return;
        }
       imageCreator(url, (HBox) b.getChildren().get(1));
    }
    //maybe qui fa casino
    private String fromAmmoCubetoIMG(List<Color> a)//idea, nome è sigla: crb.png=cartaRossoBlu
    {
        ArrayList <Color> card=new ArrayList<>();
        card.add(Color.BLUE);
        card.add(Color.RED);
        card.add(Color.RED);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/brr.png";
        }
        card.removeAll(card);

        card.add(Color.BLUE);
        card.add(Color.YELLOW);
        card.add(Color.YELLOW);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/byy.png";
        }
        card.removeAll(card);


        card.add(Color.BLUE);
        card.add(Color.BLUE);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/cbb.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.BLUE);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/crb.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.RED);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/crr.png";
        }
        card.removeAll(card);


        card.add(Color.YELLOW);
        card.add(Color.BLUE);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/cyb.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.RED);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/cyr.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.YELLOW);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/cyy.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.BLUE);
        card.add(Color.BLUE);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/rbb.png";
        }
        card.removeAll(card);

        card.add(Color.RED);
        card.add(Color.YELLOW);
        card.add(Color.YELLOW);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/ryy.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.BLUE);
        card.add(Color.BLUE);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/ybb.png";
        }
        card.removeAll(card);

        card.add(Color.YELLOW);
        card.add(Color.RED);
        card.add(Color.RED);
        if(a.equals(card))//brr type
        {
            return "/images/ammo/yrr.png";
        }
        card.removeAll(card);


        return null;
    }

    //------------------------------------------------------------ grab
    private void grabHere()//----called from the button grab
    {
        //se questa cella è spawn o ammo
        int x=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosX();
        int y=gui.getView().getCacheModel().getCachedPlayers().get(gui.getView().getPlayerId()).getStats().getCurrentPosY();
        if(gui.getView().getCacheModel().getCachedMap().getCachedCell(x,y).getCellType()==CellType.AMMO)
        {
            //se ammo aggiungi quelle munizie alle nostre/ powerUp
            grabAmmoCard(x,y);
        }
        else{//spawn cell
            //se arma : abilita il click su un arma, se puoi pagare bella, sennò mandalo a fare in culo
            //grabWeapon();
        }


    }

    private void grabAmmoCard(int x,int y)
    {
        if(!containsAmmo(map[x][y]))
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Non ci sono munizioni in questa cella, scegli un'altra azione");
            alert.show();
            return;
        }
        VBox b=map[x][y];
        for(int i=0;i<b.getChildren().size();i++)
        {
            for(int j=0;j<((HBox)b.getChildren().get(i)).getChildren().size();j++)
            {
                if(((HBox)b.getChildren().get(i)).getChildren().get(j).getId().compareTo("ammo")==0)
                {
                    ((ImageView)((HBox)b.getChildren().get(i)).getChildren().get(j)).setImage(null);//remove the ammoImage
                    List <Directions> dir=new ArrayList<>();//empty directions
                    gui.getView().doAction(new GrabAction(dir));
                }
            }
        }

    }


    //------------------------------------------------------------ move and grab

}
