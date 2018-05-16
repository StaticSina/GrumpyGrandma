package menu;

import controller.Game;
import controller.GameEngine;
import controller.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Map;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Popup {

    final public static TextField tfName = new TextField();

    // PAUSE POPUP MENY
    public static void displayPauseMenu(GameEngine engine) {
        Stage menuPause = new Stage();
        menuPause.initModality(Modality.APPLICATION_MODAL);
        menuPause.initStyle(StageStyle.UNDECORATED);

        Label paused = new Label("GAME PAUSED");
        paused.setId("pauseHeader");

        Button resumeGame = new Button("KEEP CROSSING!");
        Button saveGameState = new Button("SAVE GAME");
        Button restartGame = new Button("RESTART GAME");
        Button toMainMenu = new Button("EXIT TO MAIN MENU");

        resumeGame.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Game.setRunning(true, false);
                menuPause.close();
            } else {
                if (event.getCode() == KeyCode.ENTER) {
                    Game.setRunning(true, false);
                    menuPause.close();
                }
            }
        });

        saveGameState.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                engine.saveGame();
                Game.setRunning(false, false);
                paused.setText("GAME SAVED!");
            }
        });


        // når Start Again klikkes restarter spillet med 3 liv.
        restartGame.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.restart();
                menuPause.close();
            }
        });

        toMainMenu.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.setMenuScreen();
                menuPause.close();
            }
        });

        VBox layoutPause = new VBox();
        layoutPause.setAlignment(Pos.CENTER);
        layoutPause.getChildren().addAll(paused, resumeGame, saveGameState, restartGame, toMainMenu);
        layoutPause.setId("layoutPause");


        // Gir knappene størrelse og ID slik at de kan styles i css koden
        for (Button buttons : Arrays.asList(resumeGame, restartGame, saveGameState, toMainMenu)) {
            buttons.setPrefSize(300, 50);
            buttons.setMaxSize(300, 50);
            buttons.setId("menuButtons");
        }

        layoutPause.getStylesheets().add("/resources/style.css");

        Scene pauseMenu = new Scene(layoutPause, 350, 450);
        menuPause.setScene(pauseMenu);
        menuPause.showAndWait();
    }


    // GAME OVER MENY
    public static void displayGameOverMenu(){
        Stage menuGameOver = new Stage();
        menuGameOver.setTitle("GAME OVER");
        menuGameOver.setMaxWidth(Map.width);
        menuGameOver.setResizable(false);
        menuGameOver.initStyle(StageStyle.UNDECORATED);

        Label labelGameOver = new Label("G A M E  O V E R");
        labelGameOver.setId("gameOverLabel");

        Label labelSaveScore = new Label("YOU CROSSED " + GameEngine.count + " LANES!");
        labelSaveScore.setId("saveScoreLabel");

        Button btnSaveScore = new Button("SAVE SCORE");
        Button btnRestartGame = new Button("RESTART");
        Button btnToMainMenu = new Button("BACK TO MAIN MENU");

        // når Start Again klikkes restarter spillet med 3 liv.
        btnRestartGame.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                Game.restart();
                menuGameOver.close();
            }
        });

        btnSaveScore.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                displaySaveScore();
            }
        });

        // når Start Again klikkes restarter spillet med 3 liv.
        btnRestartGame.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                menuGameOver.close();
                Game.restart();
            }
        });

        btnToMainMenu.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.setRunning(false, false);
                menuGameOver.close();
                Game.setMenuScreen();
            }
        });

        // VBox for å holde på alle knapper
        VBox vboxGameOver = new VBox();
        vboxGameOver.setAlignment(Pos.CENTER);
        vboxGameOver.setPrefSize(500,500);
        vboxGameOver.getChildren().addAll(labelGameOver, labelSaveScore, btnSaveScore, btnRestartGame, btnToMainMenu);
        vboxGameOver.setId("gameOver");

        for (Button buttons : Arrays.asList(btnSaveScore, btnRestartGame, btnToMainMenu)) {
            buttons.setPrefSize(300, 30);
            buttons.setMaxSize(300, 30);
            buttons.setId("menuButtons");
        }

        Scene GameOver = new Scene(vboxGameOver);
        menuGameOver.setScene(GameOver);
        menuGameOver.show();
        GameOver.getStylesheets().addAll("/resources/style.css");
    }

    // LEADERBOARDS
    public static void displayLeaderBoards() {

        //Lager en ny stage for leaderboard-vinduet
        Stage menuLeaderBoards = new Stage();
        menuLeaderBoards.setMaxWidth(Map.width);
        menuLeaderBoards.setResizable(false);
        menuLeaderBoards.initStyle(StageStyle.UNDECORATED);

        Label scores = new Label("HIGHSCORES");

        Button closeLeaderBoard = new Button("Close");

        closeLeaderBoard.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                menuLeaderBoards.close();
            }
        });

        // Henter inn informasjon fra highscores.txt og legger til Score og Navn i en ny Player "player"
        Collection<Player> list = null;
        try {
            list = Files.readAllLines(new File("src/resources/highscores.txt").toPath())
                    .stream()
                    .map((String line) -> {
                        String[] details = line.split(";");
                        Player player = new Player();
                        player.setScore(details[1]);
                        player.setName(details[0]);
                        return player;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.printf("ERROR: %s\n", e);
        }

        ObservableList<Player> showdata = FXCollections.observableArrayList(list);

        //Lager en tabell
        TableView<Player> table = new TableView<>();

        //Lager kolonner til tabellen "table"
        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        TableColumn<Player, String> scoreCol = new TableColumn<>("Score");

        table.getColumns().addAll(nameCol, scoreCol);

        //Henter at i nameCol skal verdien være name fra Player, og i scoreCol skal verdien være score.
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        table.getItems().size();
        table.setItems(showdata);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(500, 500);

        vbox.getChildren().addAll(closeLeaderBoard, scores, table);

        Scene scene = new Scene(vbox);
        menuLeaderBoards.setScene(scene);
        menuLeaderBoards.show();
    }

    // SAVE-GAME
    public static void displaySaveScore() {

        Stage menuSaveScore = new Stage();
        menuSaveScore.setResizable(false);
        menuSaveScore.initStyle(StageStyle.UNDECORATED);

        Label labelSaveScore = new Label("ENTER YOUR NAME TO SAVE SCORE:");
        labelSaveScore.setId("saveScoreLabel");

        Button btnSave = new Button("SAVE SCORE");
        Button btnReturn = new Button("BACK TO MENU");

        for (Button buttons : Arrays.asList(btnSave, btnReturn)) {
            buttons.setPrefSize(300, 30);
            buttons.setMaxSize(300, 30);
            buttons.setId("menuButtons");
        }

        // Tekstfelt for skriving av navn til spiller
        tfName.setPromptText("Enter your name:");
        tfName.setPrefSize(290, 30);
        tfName.setMaxWidth(290);
        tfName.getText();
        tfName.setVisible(true);

        btnSave.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                tfName.setVisible(true);
                if (!tfName.getText().isEmpty()) {
                    labelSaveScore.setText("YOUR SCORE WAS SAVED!");
                    GameEngine.saveScore(tfName.getText(), GameEngine.count);
                    GameEngine.listHighscores();
                }

            }
        });

        btnReturn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                menuSaveScore.close();
            }
        });

        // VBox for å holde på alle knapper
        VBox vboxSaveScore = new VBox();
        vboxSaveScore.setAlignment(Pos.CENTER);
        vboxSaveScore.setPrefSize(500,500);
        vboxSaveScore.getChildren().addAll(labelSaveScore, tfName, btnSave, btnReturn);
        vboxSaveScore.setId("saveScore");

        Scene SaveScore = new Scene(vboxSaveScore);
        menuSaveScore.setScene(SaveScore);
        menuSaveScore.show();
        SaveScore.getStylesheets().addAll("/resources/style.css");


    }
}
