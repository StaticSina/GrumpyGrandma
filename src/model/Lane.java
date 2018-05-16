package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Lane implements Serializable {

    public final String name;
    public final double speed;
    public double y;
    public String structure;
    private final ArrayList<ImageView> laneObjects;
    private Rectangle bg;

    public Lane(String name, double speed, double y, String structure) {
        this.name = name;
        this.speed = speed;
        this.y = y;
        this.structure = structure;
        this.laneObjects = setLaneObjects();
        if (name.equals("River")) {
           setBackground();
        }
    }
    private void setBackground() {
        bg = new Rectangle();
        bg.setX(0);
        bg.setY(y);
        bg.setHeight(Map.rectangleHeight);
        bg.setWidth(675);
        bg.setFill(Color.rgb(64, 164, 223));

    }

    public Rectangle getBackground() {
        return bg;
    }

    public double getY() {
        return y;
    }

    public String getStructure() {
        return structure;
    }

    public double getSpeed() {
        return speed;
    }

    public ArrayList<ImageView> getLaneObjects() {
        return laneObjects;
    }

    // Lager en ny ArrayList. Looper gjennom structure og lager et svart rektangel for hver "x" og et grått rektangel for hver "."
    private ArrayList<ImageView> setLaneObjects() {
        ArrayList<ImageView> laneObjects = new ArrayList<>();
        for (int i = 0; i < structure.length(); i++) {
            char c = structure.charAt(i);
            switch (c) {
                case 'x':
                    String string;
                    String car;
                        if (i > 35) {
                            car = "car";
                        } else {
                            car = "car2";
                        }

                    if( speed < 0) {
                        string = "resources/images/" + car + ".png";
                    } else {
                        string = "resources/images/" + car + "andrevei.png";
                    }
                    if (name.equals("Road")) {

                        Image imgblack = new Image(string);
                        ImageView rectBlack = new ImageView(imgblack);
                        rectBlack.setX(0 + i * Map.rectangleWidth);
                        rectBlack.setY(this.getY());
                        rectBlack.setFitWidth(Map.rectangleWidth);
                        rectBlack.setFitHeight(Map.rectangleHeight);
                        laneObjects.add(rectBlack);
                        break;
            } else {
                    Image imgbrown = new Image("resources/images/timber.png");
                    ImageView rectBrown = new ImageView(imgbrown);
                    rectBrown.setX(0 + i * Map.rectangleWidth);
                    rectBrown.setY(this.getY());
                    rectBrown.setFitWidth(Map.rectangleWidth);
                    rectBrown.setFitHeight(Map.rectangleHeight);
                    laneObjects.add(rectBrown);
                }
            }
        }
        return laneObjects;
    }

    public String toString() {
        // Lane name, speed, y, structure (separert med komma).
        String string = name + "," + speed + "," + y + "," + structure;
        return string;
    }
}