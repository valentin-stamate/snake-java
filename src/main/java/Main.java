import controller.HamiltonController;
import controller.SinglePlayerController;
import controller.SnakeController;
import controller.TwoPlayersController;
import processing.core.PApplet;
import util.Config;
import util.GameType;

public class Main extends PApplet {

    public void settings() {
        size(Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT);
    }

    public void setup() {
        background(25);
        frameRate(Config.FRAMERATE);
        strokeCap(ROUND);

        SnakeController snakeController = null;

        int gameType = Config.GAME_TYPE;

        switch (gameType) {
            case GameType.SINGLE_PLAYER:
                snakeController = new SinglePlayerController(this);
                break;
            case GameType.TWO_PLAYERS:
                snakeController = new TwoPlayersController(this);
                break;
            default:
                snakeController = new HamiltonController(this);

        }

        snakeController.run();
    }

    public void draw(){ }

    public static void main(String... args){
        PApplet.main("Main");
    }
}
