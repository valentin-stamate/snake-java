import controller.SinglePlayerController;
import controller.SnakeController;
import controller.TwoPlayersController;
import processing.core.PApplet;
import util.Config;

public class Main extends PApplet {

    public void settings() {
        size(Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT);
    }

    public void setup() {
        background(25);
        frameRate(Config.FRAMERATE);
        strokeCap(ROUND);

        SnakeController snakeController = new TwoPlayersController(this);
        snakeController.run();
    }

    public void draw(){ }

    public static void main(String... args){
        PApplet.main("Main");
    }
}
