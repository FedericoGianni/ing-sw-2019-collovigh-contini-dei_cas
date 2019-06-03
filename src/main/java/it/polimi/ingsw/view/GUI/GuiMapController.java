package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Box;


public class GuiMapController {

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }


    @FXML
    BorderPane pane;
    @FXML
    TextArea log;
    @FXML
    GridPane innerMap;

    @FXML
    Button b00,b01,b02,b03,b10,b11,b12,b13,b20,b21,b22,b23;

    @FXML
    public void initialize() {

    }

    public void mapCreator()
    {


        switch(gui.getView().getCacheModel().getMapType()) {
            case 1: {
                innerMap.setStyle("-fx-background-image: url('/images/Map1in.png')");//use this for sample fot he
                buttonCreator();
                 b03.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            unClickAble();
                        }
                 });
                b20.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        unClickAble();
                    }
                });

                break;
            }
            case 2: {
                buttonCreator();
                innerMap.setStyle("-fx-background-image: url('/images/Map2in.png')");//use this for sample fot he maps everytime
                b20.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        unClickAble();
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
        b02.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                spawn();
            }
        });
        b10.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                spawn();
            }
        });
        b23.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                spawn();
            }
        });
    }
    private void unClickAble()
    {
        log.appendText("\n Not in the map");
    }

    private void spawn()
    {
        log.appendText("\n spawn cell");
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
    }

    @FXML
    public void b00Click(ActionEvent event )
    {
        log.appendText("Cliccato cella 00");
    }
    @FXML
    public void b01Click(ActionEvent event)
    {
        log.appendText("\nCliccato cella 01");
    }
    @FXML
    public void b02Click(ActionEvent event)
    {
        log.appendText("\nCliccato cella 02");
    }
    @FXML
    public void b03Click(ActionEvent event)
    {
        log.appendText("\nCliccato cella 03");
    }

    @FXML//secod row
    public void b10Click()
    {
        log.appendText("\nCliccato cella 10");
    }
    @FXML
    public void b11Click()
    {
        log.appendText("\nCliccato cella 11");
    }
    @FXML
    public void b12Click()
    {
        log.appendText("\nCliccato cella 12");
    }
    @FXML
    public void b13Click()
    {
        log.appendText("\nCliccato cella 13");
    }

    @FXML//third row
    public void b20Click()
    {
        log.appendText("\nCliccato cella 20");
    }
    @FXML
    public void b21Click()
    {
        log.appendText("\nCliccato cella 21");
    }
    @FXML
    public void b22Click()
    {
        log.appendText("\nCliccato cella 22");
    }
    @FXML
    public void b23Click()
    {
        log.appendText("\nCliccato cella 23");
    }


    public void loginUpdater(String name, int id, PlayerColor color)
    {
        log.appendText("\nSi è collegato: "+name+" con l'id: "+id+" ed il colore: "+color);
        switch(id)
        {
            case 0:
                b02.setStyle("-fx-background-image: url('/images/player0.png')");
                break;
            case 1:
                b10.setStyle("-fx-background-image: url('/images/player1.png')");
                break;

        }
    }

    public void statsUpdater(Player player)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setContentText("Updated player stats");

        alert.showAndWait();

    }

    @FXML
    public void click1()
    {
        log.appendText("cliccato 1");
    }


}
