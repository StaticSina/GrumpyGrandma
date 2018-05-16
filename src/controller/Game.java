package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import menu.GameMenu;
import menu.SettingsMenu;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Game. Main class that handles switching of scenes, updating game logic, and background music.
 */
public class Game extends Application {

    //Game Logic
    private static GameEngine engine;
    private final InputHandler controller;
    private static boolean running = true;
    private static boolean restart = false;

    //Graphics
    /**
     * The constant width of the screen.
     */
    private AnimationTimer timer;

    /**
     * The constant width of the screen.
     */
    private static int totalLaneOffset;

    /**
     * The constant width of the screen.
     */
    public static final double width = 645;
    /**
     * The constant heightof the screen.
     */
    public static final double height = 645;
    /**
     * The constant Grumpy, which is the main stage.
     */
    public static Stage Grumpy;
    /**
     * The constant menuScreen, for the main menu.
     */
    private static Scene menuScreen;
    /**
     * The constant gameScreen, for the game itself.
     */
    public static Scene gameScreen;
    /**
     * The constant settingsScreen, for the map generator settings.
     */

    private static Scene settingsScreen;

    /**
     * The constant settingsScreen, for the map generator settings.
     */
    private GameMenu gameMenu;
    private SettingsMenu settingsMenu;

    /**
     * The Ambient sound.
     */
//Audio
    private MediaPlayer ambientSound;
    /**
     * The constant playMusic.
     */
    public static boolean playMusic = false;

    /**
     * Instantiates a new Game.
     */
    public Game(){
        engine = new GameEngine();
        controller = new InputHandler();
        timer();
    }

    /**
     * Gets running.
     *
     * @return the running
     */
    public static boolean getRunning() {
        return running;
    }

    /**
     * Sets running.
     *
     * @param status the status
     * @param res    the res
     */
    public static void setRunning(boolean status, boolean res) {
        if (res) {
            restart = true;
        }
        running = status;
    }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args){
        launch(args);
    }

        private void startGame (Stage primaryStage){
            Grumpy = primaryStage;

            gameMenu = new GameMenu(engine);
            settingsMenu = new SettingsMenu(engine);
            startAmbientMusic();

            menuScreen = new Scene(gameMenu.root);
            settingsScreen = new Scene(settingsMenu.grid);
            gameScreen = new Scene(engine.getPane());

            primaryStage.setScene(menuScreen);
            primaryStage.setTitle("Grumpy Grandma");
            primaryStage.setResizable(false);
            primaryStage.show();

            // Stylesheet til menyen
            menuScreen.getStylesheets().addAll(this.getClass().getResource("/resources/style.css").toExternalForm());
        }

        //Lager scene med getPane som pane og starter renderer med den.
        @Override
        public void start(Stage primaryStage) {
            startGame(primaryStage);
        }

    /**
     * Sets menu screen.
     */
    public static void setMenuScreen() {
            Grumpy.setScene(menuScreen);

        }

    /**
     * Sets game screen.
     */
    public static void setGameScreen() {
            Grumpy.setScene(gameScreen);


        }

    /**
     * Sets settings screen.
     */
    public static void setSettingsScreen() {
            Grumpy.setScene(settingsScreen);

        }

    /**
     * Restart.
     */
    public static void restart() {
            totalLaneOffset = engine.totalLaneOffset;
            engine = new GameEngine();
            engine.laneOffset = 0;
            engine.totalLaneOffset = totalLaneOffset;

            GameEngine.resetLives(3);
            GameEngine.resetCounter();
            setRunning(true, true);
                Grumpy.setScene(new Scene(engine.getPane()));
            }

    /**
     * Start ambient music.
     */
    private void startAmbientMusic() {
        try {
            Path path = Paths.get(GameEngine.class.getResource(".").toURI());
            path = path.getParent();
            Media media = new Media(path.toUri().toURL().toString() + "/resources/audio/traffic.wav/");
            ambientSound = new MediaPlayer(media);

        }
        catch(URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Animation Timer, kjører 60 ganger i sekundet
    private void timer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running){
                    //Sjekker kollisjon, sjekker userInput, oppdaterer spillogikk i engine.
                    controller.userInput(engine);
                    engine.update();
                    engine.gameOver();
                    if(restart){
                        restart();
                        restart=false;
                    }
                    if(playMusic){
                        ambientSound.play();
                    }
                }
                else {
                    ambientSound.pause();
                }
            }
        };
        timer.start();
    }
}



