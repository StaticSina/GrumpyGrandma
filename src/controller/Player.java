package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The type Player.
 */
//Brukes i Popup.displayLeaderBoards i TableView.
public class Player {

    /**
     * The Name.
     */
    private final StringProperty name = new SimpleStringProperty();
    /**
     * The Score.
     */
    private final StringProperty score = new SimpleStringProperty();

    /**
     * Name property string property.
     *
     * @return the string property
     */
    public final StringProperty nameProperty() {
        return this.name;
    }

    /**
     * Sets name.
     *
     * @param playername the playername
     */
    public void setName(String playername) {
        name.set(playername);
    }

    /**
     * Score property string property.
     *
     * @return the string property
     */
    public final StringProperty scoreProperty() {
        return this.score;
    }

    /**
     * Sets score.
     *
     * @param playerscore the playerscore
     */
    public void setScore(String playerscore) {
        score.set(playerscore);
    }
}
