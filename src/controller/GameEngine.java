package controller;

import javafx.application.Application;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import model.Map;

import model.Lane;

import java.net.URISyntaxException;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The type Game engine.
 */
public class GameEngine extends Application {

    /**
     * The Pane that all game related nodes are added to.
     */
    private final Pane pane;

    /**
     * The Map, contains lane objects which generate cars and logs depending on their structure.
     */
    public final Map map;

    /**
     * The bg, which is a rectangle making that acts as a background for the game.
     */
    private Rectangle bg;

    /**
     * The .
     */
    private final ImageView player;

    /**
     * The Player speed.
     */
    public double playerSpeed = 0;
    /**
     * The boolean for whether or not the player is standing on timber.
     */
    boolean onTimber = false;
    /**
     * The Drown count.
     */
    int drownCount = 0;


    /**
     * The Start pos y.
     */
    public final double startPosY;
    private double offsetY;
    /**
     * The Lane offset.
     */
    public int laneOffset = 0;
    private int lastSafeLaneOffset;
    /**
     * The Total lane offset.
     */
    public int totalLaneOffset;
    private final Label laneCount;
    private int laneCounter = 0;
    private int totalLaneCounter;
    /**
     * The constant count.
     */
    public static int count = 0;

    // Spilleren starter med 3 liv
    private static int lives = 3;

    // Bildet som brukes for å vise liv + rektanglene som bildene legges i.
    private final Image hjerte = new Image("resources/images/hjerte1.png");
    private final Rectangle life3 = new Rectangle(60, 1, 20,20);
    private final Rectangle life2 = new Rectangle(40, 1, 20,20);
    private final Rectangle life1 = new Rectangle(20,1,20,20);

    public void start(Stage stage) {

    }

    /**
     * Instantiates a new Game engine.
     */
    public GameEngine() {
        // Lager et nytt pane, og størrelsen defineres.
        pane = new Pane();
        pane.setPrefSize(Game.width, Game.height);

        // Oppretter map, player og lanecount
        map = new Map();
        map.generateRandomLanes(3, 1, 3, 2, 5, 1, 4);
        player = generatePlayer();
        laneCount = new Label();
        startPosY = map.getLaneList().get(0).getY();

        // Legger map og player på pane.
        renderBackground();
        renderMap();
        renderPlayer();
        renderCounter();
        createLivesLeft(true);
    }

    /**
     * Update.
     */
    public void update() {
        checkCollision();
        checkOutOfBounds();
        checkLives();
        moveLanes();
        laneCounter();
        checkIfSafeLane();
        checkIfAtEndOfMap();
    }

    /**
     * Gets pane.
     *
     * @return the pane
     */
    public Pane getPane() {
        return this.pane;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public ImageView getPlayer() {
        return this.player;
    }

    private void renderMap() {
        for (Lane lane : map.getLaneList()) {
            if (lane.getBackground() != null) {
                pane.getChildren().add(lane.getBackground());
            }
            pane.getChildren().addAll(lane.getLaneObjects());
        }
    }

    private void renderBackground() {
        bg = new Rectangle();
        bg.setX(0);
        bg.setHeight(675);
        bg.setWidth(675);
        bg.setFill(Color.GRAY);
        pane.getChildren().add(bg);
    }

    private void renderPlayer() {
        pane.getChildren().add(this.player);
    }

    private void renderCounter() {
        pane.getChildren().add(laneCount);
    }

    /**
     * Reset lives.
     *
     * @param i the
     */
//Brukes i GameOVerWindow for å sette liv til 3 igjen når spillet starter på nytt
    public static void resetLives(int i){
        lives = i;
    }


    // Sjekker om spilleren er i kontakt med biler, og om de er i vannet i river-lanes.
    private void checkCollision() {
        for (Lane lane : map.getLaneList()) {
            for (ImageView imageView : lane.getLaneObjects()) {


                Rectangle rect = new Rectangle(imageView.getX(), imageView.getY(), imageView.getBoundsInParent().getWidth(), imageView.getBoundsInParent().getHeight());

                if (lane.name.equals("Road")) {
                    rect.setFill(Color.BLACK);
                }

                if (lane.name.equals("River")) {
                    rect.setFill(Color.BLUE);
                }


                Rectangle playerrect = new Rectangle(player.getX(), player.getY(), player.getBoundsInParent().getWidth(), player.getBoundsInParent().getHeight());

                Shape intersect = Shape.intersect(playerrect, rect);

                if ((rect.getFill().equals(Color.BLACK) && intersect.getBoundsInParent().getWidth() > 0)) {
                    onDeath();
                }

                // Sjekker om spilleren befinner seg på en tømmerstokk
                if (lane.name.equals("River") && player.getY() == lane.getY()) {

                    // Dersom drownCount er større enn 20 aktiveres onDeath().
                    if (drownCount > 20) {
                        onDeath();
                    }
                    // Dersom spilleren er i kontakt med en tømmerstokk får man samme hastighet som lanen.
                    else if (rect.getFill().equals(Color.BLUE) && intersect.getBoundsInParent().getWidth() > 0) {
                        playerSpeed = lane.speed;
                        onTimber = true;
                        //System.out.println("On");
                    }
                    // Det tar litt tid for intersect-funksjonen å merke at spilleren er i kontakt med en tømmerstokk,
                    // derfor aktiveres ikke onDeath() før drownCount når 20.
                    else if (!onTimber) {
                        drownCount++;
                    }
                }
            }
        }
    }

    // Sjekker om spilleren er utenfor skjermen, og aktiverer onDeath() dersom spilleren i tillegg er på en river-lane.
    private void checkOutOfBounds() {
        for (Lane lane : map.getLaneList()) {
            if (lane.name.equals("River") && player.getY() == lane.getY()) {
                if (player.getX() > Map.width - player.getBoundsInParent().getWidth()) {
                    onDeath();
                }
                else if (player.getX() < 0) {
                    onDeath();
                }
            }
        }
        // Dersom spilleren ikke er på river-lane og havner utenfor skjermen, blir man plassert helt ytterst på siden man er på.
        // Dette forhindrer at man kan bevege seg litt utenfor skjermen som følge av at river tar deg utenfor 15x15-rutenettet.
        if (player.getX() > 645 - player.getBoundsInParent().getWidth()) {
            player.setX(645 - player.getBoundsInParent().getWidth());
        }
        else if (player.getX() < 0) {
            player.setX(0);
        }
    }

    /**
     * On death.
     */
// Med respawn
    public void onDeath() {
        playSound("crash", 1);
        playSound("scream", 1);
        // mister liv når spilleren krasjer
            lives--;
            playerSpeed = 0;
            laneOffset = lastSafeLaneOffset;
            totalLaneOffset = laneOffset;
            double diff = offsetY - laneOffset;
            updateCameraView(diff);
            player.setX(Map.width / 2 - Map.rectangleWidth / 2);
            player.setY(Map.height - Map.rectangleHeight);
    }

    //Beveger alt som går av seg selv (biler, tømmerstokker)
    private void moveLanes() {
        for (Lane lane : map.getLaneList()) {
            for (ImageView car : lane.getLaneObjects()) {
                // Sjekker om objektene i laneObjects har nådd enden av listen, og endrer x-verdien til hvert objekt.
                // Bruker differansen mellom car.x og positiveEnd/negativeEnd for å gjøre opp for hvor mye de overstiger grenseverdien før de flyttes.
                double positiveEnd = car.getBoundsInParent().getWidth() * (62);
                double negativeEnd = 0 - car.getBoundsInParent().getWidth() * (64 - 17);
                if (car.getX() > positiveEnd) {
                    car.setX(0 - car.getBoundsInParent().getWidth() * 2 + (car.getX() - positiveEnd));
                }
                else if (car.getX() < negativeEnd) {
                    car.setX(car.getBoundsInParent().getWidth() * 17 - (negativeEnd + Math.abs(car.getX())));
                }
                car.setX(car.getX() + lane.getSpeed());
            }
        }
        // Flytter spilleren dersom de har en speed, som kun forekommer dersom de står på en tømmerstokk i en "river"-lane.
        player.setX(player.getX() + playerSpeed);
    }

    /**
     * Reset counter.
     */
    public static void resetCounter() {
        count = 0;
    }

    private void checkLives() {
        if (lives == 2){
             life3.setVisible(false);
         }
         if (lives == 1){
             life2.setVisible(false);
         }
         if (lives < 1) {
             life1.setVisible(false);
         }
    }

    private void laneCounter() {
        int rectangleHeight = (int)Map.rectangleHeight;
        laneCounter = laneOffset/rectangleHeight;
        totalLaneCounter = totalLaneOffset/rectangleHeight;
        if (totalLaneCounter > count) {
            count = totalLaneCounter;
        }
        laneCount.textProperty().set(Integer.toString(count));
    }


    private void createLivesLeft(boolean first) {
        life1.setFill(new ImagePattern(hjerte));
        life2.setFill(new ImagePattern(hjerte));
        life3.setFill(new ImagePattern(hjerte));

        life1.setVisible(true);
        life2.setVisible(true);
        life3.setVisible(true);

         if (!first) {
            pane.getChildren().removeAll(life1, life2, life3);
        }

        pane.getChildren().addAll(life1, life2, life3);
    }

    /**
     * Updates camera view based on player movement.
     *
     * @param amt the amount to add to the {@link #offsetY}.
     */
//
    public void updateCameraView(double amt) {
        offsetY -= amt;

        for (Lane lane : map.getLaneList()) {
            double newValue = lane.getY();
            newValue -= amt;
            if (lane.getBackground() != null) {
                lane.getBackground().setY(newValue);
            }
            boolean first = true;
            for (ImageView car : lane.getLaneObjects()) {
                newValue = car.getY();
                newValue -= amt;
                if (first) {
                    lane.y = newValue;
                    first = false;
                }
                car.setY(newValue);
            }
        }
    }

    /**
     * Generates the player's image view and defines it's width, height, X-position, Y-position.
     *
     * @return the image view
     */
    private ImageView generatePlayer() {
        int width = 28;
        int height = (int)Map.rectangleHeight;

        Image grandma = new Image("resources/images/grandma.png");
        ImageView imageView = new ImageView(grandma);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setX(Map.width/2-Map.rectangleWidth/2);
        imageView.setY(Map.height-Map.rectangleHeight);

        return imageView;
    }

    /**
     * Save the player's score to a text file.
     *
     * @param name  the name
     * @param score the score
     */
    public static void saveScore(String name, int score) {

        try {
            FileWriter saveScore = new FileWriter("src/resources/scores.txt", true);
            BufferedWriter swriter = new BufferedWriter(saveScore);
            PrintWriter spwriter = new PrintWriter(swriter);
            spwriter.println(name + ";" + score + "");
            spwriter.close();
        } catch (IOException e) {
            System.out.printf("ERROR: %s\n", e);
        }
    }

    /**
     * The constant sort. Sorts the array in {@link #listHighscores()}
     */
// brukes for å sortere array i listHighscores()
    private static final Comparator<? super String> sort = (Comparator<String>) (score1, score2) -> {
        int place1 = Integer.parseInt(score1.split(";")[1]);
        int place2 = Integer.parseInt(score2.split(";")[1]);

        if(place1 < place2)
            return 1;
        else
            return -1;
    };

    /**
     * Loads highscores from a text file and displays them in a leaderboard.
     */
// Brukes for å lese alle lagrede scores og sorterer de og legger de i filen "highscores.txt".
    public static void listHighscores() {

        try {
            // leser scores.txt
            BufferedReader br = new BufferedReader(new FileReader("src/resources/scores.txt"));
            String line;
            ArrayList<String> array = new ArrayList<>();

            // legger linjene i scores.txt inn i et array "array"
            while(true){
                line = br.readLine();
                if(line != null && line.length() > 1)
                    array.add(line);
                if(line == null)
                    break;
            }

            //kaller på Comparator for å sortere linjene fra høyeste til laveste score
            array.sort(sort);


            File file2 = new File("src/resources/highscores.txt");

            //Skriver inn det sorterte arrayet linje for linje inn i filen highscores.txt
            PrintWriter pr = new PrintWriter(file2);
            for(String string : array) {
                pr.println("" + string);
            }
            pr.close();

        } catch (IOException e) {
            System.out.print(e);
        }
    }


    /**
     * Game over, triggered when the player is out of lives. Stops the game and displays the game over menu.
     */
// GameOver hvis tom for liv, henter GameOver vinduet og setter livesLeft verdien til false.
    public void gameOver() {
        if (lives < 1) {
            Game.setRunning(false, false);
            menu.Popup.displayGameOverMenu();
        }
    }

    private void checkIfSafeLane() {
        if (laneOffset % 7 == 0 && laneOffset > lastSafeLaneOffset) {
            lastSafeLaneOffset = laneOffset;
        }
    }

    /**
     * Saves the map and the players location/score to a text file.
     */
    public void saveGame() {
        try {
            File file = new File("src/resources/savefile.txt");

            if(!file.exists()) {
                file.createNewFile();
            }

            PrintWriter pw = new PrintWriter(file);
            //Player name, lanecount, lives (separert med komma).
            pw.println(totalLaneOffset + "," + count + "," + lives);
            for (Lane lane : map.getLaneList()) {
                pw.println(lane);
            }
            pw.close();
            System.out.println("Game saved!");
        }
        catch(Exception er) {
            er.printStackTrace();
        }
    }

    /**
     * Loads a previously saved map and the players location/score from a text file.
     */
    public void loadGame() {
        ArrayList<Lane> laneList = new ArrayList<>();
        boolean firstLine = true;
        try {
            List<String> lines = Files.readAllLines(new File( "src/resources/savefile.txt").toPath());
            for(String line : lines){
                String[] split = line.split(",");
                if (firstLine) {
                    totalLaneOffset = Integer.parseInt(split[0]);
                    count = Integer.parseInt(split[1]);
                    laneCounter = Integer.parseInt(split[1]);
                    lives = Integer.parseInt(split[2]);
                    firstLine = false;
                }
                else {
                    laneList.add(new Lane(split[0], Double.parseDouble(split[1]), Double.parseDouble(split[2]), split[3]));
                }
            }
            map.setLaneList(laneList);
            Game.setRunning(true, true);
            Game.setGameScreen();
        }
        catch (IOException a) {
            a.printStackTrace();
        }
        System.out.println("Game loaded");
    }

    /**
     * Plays audiofiles for sound effects.
     *
     * @param name the name of the file to be played
     * @param gain the volume of the sound (0-1).
     */
// Tar inn fil-navn og en verdi for "gain", som bestemmer volum.
    public void playSound(String name, double gain) {
        try {
            File file = new File("src/resources/audio/" + name + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if at end of map.
     */
    private void checkIfAtEndOfMap() {
        if (laneCounter >= 100) {
            Game.restart();
            laneCounter = 0;
        }
    }
}
