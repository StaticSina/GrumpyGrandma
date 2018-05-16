package menu;

import controller.Game;
import controller.GameEngine;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


/**
 * The type Settings menu. Edits parameters for the random map generator through sliders. Some of the labels are used to
 * visualize changes, with for example "x...x" to represent there being 3 squares of space between every car.
 */
public class SettingsMenu extends Application {

    /**
     * The Grid.
     */
    public final GridPane grid;

    /**
     * Instantiates a new Settings menu.
     *
     * @param engine the engine object of GameEngine
     */
    public SettingsMenu(GameEngine engine) {
        // Add a scene
        grid = new GridPane();
        grid.setPrefSize(Game.width, Game.height);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        // Distance between cars/logs
        final Label distance = new Label("Space between cars/logs (1-10):");
        distance.setFont(Font.font("Calibri", FontWeight.BOLD, 12));

        final Label desc = new Label("     Mininum");
        final Slider slider = new Slider(0, 9, 0);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(1);
        final Label label = new Label("");
        label.textProperty().setValue("x.x");
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(distance, 0, 0);
        GridPane.setConstraints(desc, 0, 1);
        GridPane.setConstraints(slider, 1, 1);
        GridPane.setConstraints(label, 2, 1);

        final Label desc2 = new Label("     Maximum");
        final Slider slider2 = new Slider(0, 9, 0);
        slider2.setMajorTickUnit(5);
        slider2.setMinorTickCount(1);
        final Label label2 = new Label("");
        label2.textProperty().setValue("x.x");
        label2.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(distance, 0, 0);
        GridPane.setConstraints(desc2, 0, 2);
        GridPane.setConstraints(slider2, 1, 2);
        GridPane.setConstraints(label2, 2, 2);

        // Length of cars
        final Label carLength = new Label("Length of cars (1-5):");
        carLength.setFont(Font.font("Calibri", FontWeight.BOLD, 12));

        final Label desc3 = new Label("     Mininum");
        final Slider slider3 = new Slider(0, 4, 0);
        slider3.setMajorTickUnit(5);
        slider3.setMinorTickCount(1);
        final Label label3 = new Label("");
        label3.textProperty().setValue("x");
        label3.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(carLength, 0, 3);
        GridPane.setConstraints(desc3, 0, 4);
        GridPane.setConstraints(slider3, 1, 4);
        GridPane.setConstraints(label3, 2, 4);


        final Label desc4 = new Label("     Maximum");
        final Slider slider4 = new Slider(0, 4, 0);
        slider4.setMajorTickUnit(5);
        slider4.setMinorTickCount(1);
        final Label label4 = new Label("");
        label4.textProperty().setValue("x");
        label4.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(desc4, 0, 5);
        GridPane.setConstraints(slider4, 1, 5);
        GridPane.setConstraints(label4, 2, 5);

        // Speed
        final Label speed = new Label("Speed of cars(1-10):");
        speed.setFont(Font.font("Calibri", FontWeight.BOLD, 12));

        final Label desc5 = new Label("     minimum");
        final Slider slider5 = new Slider(1, 10, 1);
        slider5.setMajorTickUnit(5);
        slider5.setMinorTickCount(1);
        final Label label5 = new Label("");
        label5.textProperty().setValue("1");
        label5.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(speed, 0, 6);
        GridPane.setConstraints(desc5, 0, 7);
        GridPane.setConstraints(slider5, 1, 7);
        GridPane.setConstraints(label5, 2, 7);

        final Label desc6 = new Label("     maximum");
        final Slider slider6 = new Slider(1, 10, 1);
        slider6.setMajorTickUnit(5);
        slider6.setMinorTickCount(1);
        final Label label6 = new Label("");
        label6.textProperty().setValue("1");
        label6.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(desc6, 0, 8);
        GridPane.setConstraints(slider6, 1, 8);
        GridPane.setConstraints(label6, 2, 8);


        // Frequency
        final Label frequency = new Label("Frequency of... (1-10):");
        frequency.setFont(Font.font("Calibri", FontWeight.BOLD, 12));

        final Label desc7 = new Label("     Rivers");
        final Slider slider7 = new Slider(1, 10, 1);
        slider5.setMajorTickUnit(5);
        slider5.setMinorTickCount(1);
        final Label label7 = new Label("");
        label7.textProperty().setValue("1");
        label7.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

        GridPane.setConstraints(frequency, 0, 9);
        GridPane.setConstraints(desc7, 0, 10);
        GridPane.setConstraints(slider7, 1, 10);
        GridPane.setConstraints(label7, 2, 10);

        // Buttons
        Button setMap = new Button("SET MAP");
        setMap.setTranslateX(10);
        Button defaultValues = new Button("DEFAULT VALUES");
        Button back = new Button("GO BACK");
        final Label label8 = new Label("");
        label8.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        final Label label9 = new Label("CONFIRM WITH ENTER");
        label9.setFont(Font.font("Calibri", FontWeight.BOLD, 20));


        GridPane.setConstraints(setMap, 0, 12);
        GridPane.setConstraints(label8, 0, 13);
        GridPane.setConstraints(defaultValues, 1, 12);
        GridPane.setConstraints(back, 2, 12);
        GridPane.setConstraints(label8, 0, 13);
        GridPane.setConstraints(label9, 0, 15);

        /**
         * setMap. Sets the map with the specified settings.
         */
        setMap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                engine.map.generateRandomLanes((int)slider7.getValue(), (int)slider3.getValue()+1, (int)slider4.getValue()+1, (int)slider.getValue()+1,
                        (int)slider2.getValue()+1, (int)slider5.getValue(), (int)slider6.getValue());
                label8.textProperty().setValue("Map successfully set.");
                engine.map.testLanes(engine.map.getLaneList());
            }
        });

        /**
         * back. Goes back to the main menu screen.
         */
        back.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Game.setMenuScreen();
                label8.textProperty().setValue("");
            }
        });

        /**
         * defaultValues. Sets the slider to the default values used for map generation.
         */
        defaultValues.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                slider7.setValue(3);
                slider3.setValue(0);
                slider4.setValue(0);
                slider.setValue(1);
                slider2.setValue(4);
                slider5.setValue(0);
                slider6.setValue(3);
            }
        });

        slider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            String structure = "x.";
            for (int x = 0; x < (int) slider.getValue(); x++) {
                structure += ".";
            }
            structure += "x";
            label.textProperty().setValue(structure);
            if (label2.getText().length() <= label.getText().length()) {
                slider2.setValue(slider.getValue());
                label2.textProperty().setValue(structure);
            }
        });

        slider2.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            String structure = "x.";

            for (int x = 0; x < (int) slider2.getValue(); x++) {
                structure += ".";
            }
            structure += "x";
            label2.textProperty().setValue(structure);
            if (label.getText().length() >= label2.getText().length()) {
                slider.setValue(slider2.getValue());
                label.textProperty().setValue(structure);
            }
        });

        slider3.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            String structure = "x";
            for (int x = 0; x < (int) slider3.getValue(); x++) {
                structure += "x";
            }
            label3.textProperty().setValue(structure);
            if (label4.getText().length() <= label3.getText().length()) {
                slider4.setValue(slider3.getValue());
                label4.textProperty().setValue(structure);
            }
        });

        slider4.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            String structure = "x";

            for (int x = 0; x < (int) slider4.getValue(); x++) {
                structure += "x";
            }
            label4.textProperty().setValue(structure);
            if (label3.getText().length() >= label4.getText().length()) {
                slider3.setValue(slider4.getValue());
                label3.textProperty().setValue(structure);
            }
        });

        slider5.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            if (slider6.getValue() <= slider5.getValue()) {
                slider6.setValue(slider5.getValue());
            }
            label5.textProperty().setValue(Integer.toString((int)(slider5.getValue())));
        });

        slider6.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
            if (slider5.getValue() >= slider6.getValue()) {
                slider5.setValue(slider6.getValue());
            }
            label6.textProperty().setValue(Integer.toString((int)(slider6.getValue())));
        });

        slider7.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> label7.textProperty().setValue(Integer.toString((int)(slider7.getValue()))));

        grid.getChildren().addAll(distance, carLength, speed, frequency, desc, desc2, desc3, desc4, desc5, desc6, desc7, slider,
                slider2, slider3, slider4, slider5, slider6, slider7, label, label2, label3, label4, label5, label6, label7, label8, label9, setMap, defaultValues, back);

    }

    @Override
    public void start(Stage primaryStage) {
    }
}
