package controller;

import javafx.scene.image.ImageView;
import menu.Popup;

/**
 * The type Input handler.
 */
class InputHandler {
    private double bottomLaneY;
    private double startPosY;

    /**
     * User input.
     *
     * @param engine the engine, used to control the player object and various variables in engine.
     */
    public void userInput(GameEngine engine) {
        ImageView player = engine.getPlayer();
        bottomLaneY = engine.map.getLaneList().get(1).getLaneObjects().get(0).getY() + player.getBoundsInParent().getHeight();
        startPosY = engine.startPosY;
        player.getScene().setOnKeyPressed(event -> {
            engine.playerSpeed = 0;
            engine.onTimber = false;
            engine.drownCount = 0;

            // Checks whether or not running is true, to determine if ESCAPE should start or stop the game, and to limit movement while the game is paused.
            if (Game.getRunning()) {
                switch (event.getCode()) {
                    case W:
                    case UP:
                        engine.playSound("goUp", 0.3);
                        engine.laneOffset +=player.getBoundsInParent().getHeight();
                        engine.totalLaneOffset +=player.getBoundsInParent().getHeight();

                        // Setter playerSpeed = 0 hver gang man går opp eller ned, slik at man ikke beholder speed fra river
                        if (player.getY() == Game.height - player.getBoundsInParent().getHeight() * 4) {
                            engine.updateCameraView(-player.getBoundsInParent().getHeight());
                        }
                        else {
                            player.setY(player.getY() - player.getBoundsInParent().getHeight());
                        }
                        break;
                    case S:
                    case DOWN:
                        engine.playSound("goDown", 0.3);
                        // Denne forhindrer laneOffset fra å gå i minus dersom man trykker "down" og allerede er nederst på skjermen.
                        if (player.getY() != Game.height - player.getBoundsInParent().getHeight()) {
                        engine.laneOffset -=player.getBoundsInParent().getHeight();
                        engine.totalLaneOffset -=player.getBoundsInParent().getHeight();

                        }
                        if (bottomLaneY >= startPosY + player.getBoundsInParent().getHeight()) {
                            engine.updateCameraView(player.getBoundsInParent().getHeight());
                        }
                        else if (bottomLaneY == startPosY && player.getY() != bottomLaneY) {
                            player.setY(player.getY() + player.getBoundsInParent().getHeight());
                        }
                        break;
                    case A:
                    case LEFT:
                        if (player.getX() > 0) {
                            player.setX(player.getX() - player.getBoundsInParent().getWidth());
                        }
                        break;
                    case D:
                    case RIGHT:
                        if (player.getX() < Game.width - player.getBoundsInParent().getWidth()) {
                            player.setX(player.getX() + player.getBoundsInParent().getWidth());
                        }
                        break;

                    case ESCAPE:
                        Game.setRunning(false, false);
                        Popup.displayPauseMenu(engine);
                        break;
                    case Q:
                        engine.onDeath();
                    default:
                        break;
                }
            }
            else {
                switch (event.getCode()) {
                    case ESCAPE:
                        Game.setRunning(true, false);
                        Popup.displayPauseMenu(engine);
                        break;
                default:
                    break;
                }}

        });
    }
}
