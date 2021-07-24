package window;

import controller.HamiltonController;
import controller.SinglePlayerController;
import controller.SnakeController;
import controller.TwoPlayersController;
import processing.core.PApplet;
import util.Config;
import util.GameType;

public class MainWindow extends PApplet {


    public void settings() {
        size(Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT);
    }


    public void setup() {
        surface.setLocation(displayWidth / 2, displayHeight / 2 - height / 2);
        surface.setTitle("Conquering The Snake");

        background(25);
        frameRate(Config.FRAMERATE);
        strokeCap(ROUND);

        SnakeController snakeController = null;

        int gameType = Config.GAME_TYPE;

        snakeController = switch (gameType) {
            case GameType.SINGLE_PLAYER -> new SinglePlayerController(this);
            case GameType.TWO_PLAYERS -> new TwoPlayersController(this);
            default -> new HamiltonController(this);
        };

        snakeController.run();
    }


    public void draw(){ }
}
