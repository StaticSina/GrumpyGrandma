package menu;

import controller.Game;
import controller.GameEngine;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;

import static controller.Game.setRunning;

/**
 * The type Game menu.
 */
public class GameMenu extends Application {

    /**
     * The Root.
     */
    public final Pane root;
    /**
     * The Grumpy bg.
     */
    private final Rectangle grumpyBG;
    /**
     * The Button container.
     */
    private final VBox buttonContainer;

    /**
     * Instantiates a new Game menu.
     *
     * @param engine the engine
     */
    public GameMenu(GameEngine engine) {

        // Setter avstanden mellom hvert element(knapp)
        buttonContainer = new VBox(10);

        // buttonContainer er hovedboksen som inneholder alle knappene
        // her settes posisjonen til hovedboksen
        buttonContainer.setTranslateX(50);
        buttonContainer.setTranslateY(100);

        // Label med spillnavn
        Label gameName = new Label("GRUMPY GRANDMA");
        gameName.setId("gameTitle");

        // Knapper for starting av spill
        Button btnStart = new Button("START");
        Button btnLoadGame = new Button("LOAD GAME");
        Button btnSetting = new Button("MAP SETTINGS");
        Button btnLB = new Button("LEADERBOARDS");
        Button btnExit = new Button("EXIT");


        // Starte med kontakt med knapp
        btnStart.setOnAction(event -> {
            Game.playMusic = true;
            Game.Grumpy.setScene(Game.gameScreen);
            GameEngine.resetLives(3);
            GameEngine.resetCounter();
            setRunning(true, true);
        });

        /*
          setOnKeyPressed er lagt til da det er trøbbel på MAC ved bruk av setOnAction.
          Løsningen vår var da å legge inn en keylistener for ENTER knappen.
         */

        btnStart.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.playMusic = true;
                Game.setGameScreen();
                GameEngine.resetLives(3);
                GameEngine.resetCounter();
                setRunning(true, true);
                Game.playMusic = true;
            }
        });

        btnLoadGame.setOnAction(event -> {
            Game.Grumpy.setScene(Game.gameScreen);
            engine.loadGame();
            setRunning(true, true);
        });

        btnLoadGame.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.setGameScreen();
                engine.loadGame();
                setRunning(true, true);
            }
        });

        btnSetting.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.setSettingsScreen();
            }
        });


        btnLB.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                Popup.displayLeaderBoards();
            }
        });

        // Programmet avsluttes ved klikk på exit
        btnExit.setOnMouseClicked(event -> System.exit(0));
        btnExit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.exit(0);
            }
        });

        for (Button buttons : Arrays.asList(btnStart, btnLoadGame, btnSetting, btnLB, btnExit)) {
            buttons.setPrefSize(300, 60);
            buttons.setMaxSize(300, 60);
            buttons.setId("menuButtons");
        }

        // Hovedboksen lages og alle knappene legges inn
        buttonContainer.getChildren().addAll(gameName, btnStart, btnSetting, btnLoadGame, btnLB, btnExit);

        // Bakgrunnen til hele vinduet styres herfra, bakgrunnsfarge osv
        grumpyBG = new Rectangle(Game.width, Game.height);
        grumpyBG.setFill(Color.GREY);
        grumpyBG.setOpacity(0);


        root = new Pane();
        root.setPrefSize(Game.width, Game.height);
        root.getChildren().addAll(buttonContainer, grumpyBG);
        root.setId("pane");

    }
    public void start(Stage stage) {
}
}
