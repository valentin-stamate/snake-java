package util;

public abstract class Config {
    public static final int GAME_TYPE = GameType.SINGLE_PLAYER;

    /* CANVAS */
    public static final int FRAMERATE = 60;
    public static final int REFRESH_RATE = 2;

    /* BOARD */
    public static final int BOARD_WIDTH = 650;
    public static final int BOARD_HEIGHT = 650;

    public static final int CANVAS_WIDTH = BOARD_WIDTH;
    public static final int CANVAS_HEIGHT = BOARD_HEIGHT;


    public static final int BOARD_ROWS = 14 * 2;
    public static final int BOARD_COLUMNS = 14 * 2;
    public static final int CELL_SIZE = BOARD_HEIGHT / BOARD_ROWS;
    public static final int BOARD_SIZE = 4 + BOARD_WIDTH;

    public static final int BOARD_X = 2;
    public static final int BOARD_Y = 2;
}
