package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * The type Map.
 */
public class Map implements Serializable {

    /**
     * the laneList. Contains a list of lanes on the map.
     */
    private ArrayList<Lane> laneList;

    /**
     * The constant width of the map.
     */
    public static final double width = 645;
    /**
     * The constant height of the map.
     */
    public static final double height = 645;

    private static final int numOfRectanglesX = 15;
    private static final int numOfRectanglesY = 15;

    /**
     * The constant rectangleWidth, which is the width of one square in the "grid".
     */
    public static final double rectangleWidth = width / numOfRectanglesX;
    /**
     * The constant rectangleHeight, which is the height of one square in the "grid".
     */
    public static final double rectangleHeight = height / numOfRectanglesY;

    /**
     * Instantiates a new Map with the default values.
     */
//Bruk generateLanes for hardkodede eller generateRandomLanes for tilfeldige.
    public Map() {
        generateRandomLanes(3, 1, 3, 2, 5, 1, 4);
    }

    /**
     * Gets lane list.
     *
     * @return the lane list
     */
    public ArrayList<Lane> getLaneList() {
        return this.laneList;
    }

    /**
     * Sets lane list.
     *
     * @param list the list
     */
    public void setLaneList(ArrayList<Lane> list) {
        laneList = list;
    }

    /**
     * Generates random lanes with the parameters specified. Uses random numbers within the specified parameters to make
     * the map varied, with certain conditions to make it somewhat balanced. Every 7th lane is a "safe lane", with no cars/logs
     * and a speed of 0. These are where the player respawns on death.
     *
     * @param freqRivers the frequency of rivers
     * @param minLength  the min length of cars
     * @param maxLength  the max length of cars
     * @param minDist    the min distance between cars
     * @param maxDist    the max distance between cars
     * @param minSpeed   the min speed of cars
     * @param maxSpeed   the max speed of cars
     */
    public void generateRandomLanes(int freqRivers, int minLength, int maxLength, int minDist, int maxDist, int minSpeed, int maxSpeed) {
        /**
         * previousspeed  the speed of the previously generated lane.
         */
        double previousSpeed = 0;

        /**
         * previousspeed  whether or not the previous was a car.
         */
        boolean previousCar = false;
        ArrayList<Lane> laneList = new ArrayList<>();

        for(int i = 0; i < 120; i++) {
            String name;
            double speed = 1;
            double y = height - i * rectangleHeight - rectangleHeight;
            String structure = "";

            Random rand = new Random();
            // Safe lane på øverste og hver 7. lane (viktig at 7 kan ganges opp til "i", for eksempel 49).
            if (i == 0 || i % 7 == 0) {
                name = "Safe";
                speed = 0;
                structure = "................................................................";
            }
            // Ikke-safe lane
            else {
                int n = rand.nextInt(10 - 1 + 1) + 1;
                // 2/10 by default
                if (n < freqRivers) {
                    name = "River";
                }
                // 8/10 by default
                else {
                    name = "Road";
                }

                for (int x = 0; x < 200; x++) {
                    if (!previousCar) {
                        previousCar = true;
                        n = rand.nextInt(10 - 1 + 1) + 1;
                        if (n < 7) {
                            for (int m = 0; m < minLength; m++) {
                                structure+= "x";
                            }
                        }
                        else {
                            n = rand.nextInt(maxLength - minLength + 1) + minLength;
                            for (int m = 0; m < n; m++) {
                                structure += "x";
                            }
                        }
                    }
                    else {
                        n = rand.nextInt(maxDist - minDist + 1) + minDist;
                        for (int m = 0; m < n; m++) {
                            structure += ".";
                        }
                        previousCar = false;
                        structure += ".";
                    }

                }

                // Bestemmer først om hastigheten skal være positiv eller negativ. 1 til 2, altså 50/50
                int s = rand.nextInt(1 + 1);
                if (s == 0) {
                    s = rand.nextInt(maxSpeed - minSpeed + 1) + minSpeed;
                }
                else {
                    s = -1 * rand.nextInt(maxSpeed - minSpeed + 1) + minSpeed;
                }
                if (speed != 0) {
                    // Fail safe i tilfelle "s" tilfeldigvis blir 0, så ikke bilene i lanen blir stående stille.
                    if (s == 0) {
                        s = 2;
                    }
                }
                // Sjekker at hastigheten ikke er samme som forrige lane
                if (previousSpeed != s) {
                    speed = s;
                }
                else {
                    // Randomizer til den får en verdi som ikke er samme som forrige lane.
                    for (int b = 0; b < 100; b++) {
                        s = rand.nextInt(4 + 4 + 1) - 4;
                        if (s != previousSpeed) {
                            break;
                        }
                    }
                    speed = s;
                }
                previousSpeed = speed;
            }
            // Kutter stringen til 64 characters, slik at matten ikke trenger å være perfekt.
            structure = structure.substring(0, 63);
            // Legger til "." som siste character slik at biler på begynnelsen og slutten av structure ikke blir sammenhengende.
            structure += ".";
            laneList.add(new Lane(name, speed, y, structure));
        }
        // Forsikrer at det finnes en bil på nest nederste lane som kan brukes for å beregne kamera
        laneList.get(1).structure = "x" + laneList.get(1).structure.substring(1,64);
        this.laneList = laneList;
    }

    /**
     * Prints the relevant info of all the lanes in the laneList. For testing purposes.
     *
     * @param list the list
     */
    public void testLanes(ArrayList<Lane> list) {
        for (Lane lane : list) {
            System.out.println("Name: " + lane.name + ", Length: " + lane.getStructure().length() + ", Structure: " + lane.getStructure() + ", Speed: " + lane.speed);
        }
    }
}
