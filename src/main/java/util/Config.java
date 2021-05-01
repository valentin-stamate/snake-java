package util;

public class Config {
    /* CANVAS */
    public static final int FRAMERATE = 60;
    public static final int REFRESH_RATE = 5;

    /* BOARD */
    public static final int CANVAS_WIDTH = 720;
    public static final int CANVAS_HEIGHT = 644;

    public static final int BOARD_WIDTH = 540;
    public static final int BOARD_HEIGHT = CANVAS_HEIGHT;

    public static final int BOARD_ROWS = 32;
    public static final int CELL_SIZE = BOARD_HEIGHT / BOARD_ROWS;
    public static final int BOARD_COLUMNS = BOARD_WIDTH / CELL_SIZE;

    public static final int BOARD_X = 100;
    public static final int BOARD_Y = 2;
}
