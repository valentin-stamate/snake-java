package board.snake;

class SnakeCell {
    private int i;
    private int j;

    SnakeCell() {}

    SnakeCell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    protected void updatePosition(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

}
