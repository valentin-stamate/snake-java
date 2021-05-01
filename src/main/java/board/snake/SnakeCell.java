package board.snake;

class SnakeCell {
    private final int i;
    private final int j;

    SnakeCell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    /* GETTERS AND SETTERS */
    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public SnakeCell getCopy() {
        return new SnakeCell(i, j);
    }

    /* USED FOR MAPPING THE SNAKE TAIL */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SnakeCell snakeCell = (SnakeCell) o;

        if (i != snakeCell.i) return false;
        return j == snakeCell.j;
    }

    @Override
    public int hashCode() {
        int result = i;
        result = 31 * result + j;
        return result;
    }

}
