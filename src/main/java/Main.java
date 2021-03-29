import board.Board;
import processing.core.PApplet;

public class Main extends PApplet {

    private Board board;
    private final int cellSize = 40;

    public void settings() {
        size(720, 640);
    }

    public void setup() {
        background(25);
        frameRate(60);
        strokeCap(ROUND);

        board = new Board(this, 0, 0, 16, 16, cellSize);
    }

    public void draw(){ }

    public static void main(String... args){
        PApplet.main("Main");
    }
}
