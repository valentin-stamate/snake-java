import board.Board;
import board.GameType;
import processing.core.PApplet;

public class Main extends PApplet {

    private static final int CANVAS_WIDTH = 720;
    private static final int CANVAS_HEIGHT = 644;

    private static final int BOARD_WIDTH = 540;
    private static final int BOARD_HEIGHT = CANVAS_HEIGHT;

    private static final int BOARD_ROWS = 32;
    private static final int CELL_SIZE = BOARD_HEIGHT / BOARD_ROWS;
    private static final int BOARD_COLUMNS = BOARD_WIDTH / CELL_SIZE;

    private Board board;

    private int gameType = 0;

    public void settings() {
        size(CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    public void setup() {
        background(25);
        frameRate(30);
        strokeCap(ROUND);

        board = new Board(this, 100, 2, BOARD_ROWS, BOARD_COLUMNS, CELL_SIZE, GameType.SNAKE_AI);
    }

    public void draw(){ }

    public static void main(String... args){
        PApplet.main("Main");
    }
}
