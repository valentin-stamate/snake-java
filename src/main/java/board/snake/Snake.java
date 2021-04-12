package board.snake;

import board.CellType;
import genetic_algorithm.GeneticAlgorithmMember;
import neural_network.NeuralNetwork;
import observer.Observer;

import java.util.*;

public class Snake implements GeneticAlgorithmMember<Snake> {

    private final List<Observer> snakeCollisionObserverList;

    private final int[] direction = new int[]{0, 1};

    private final List<SnakeCell> snakeCells;
    private final HashSet<SnakeCell> snakeCellsHashSet;

    private final int[][] boardMatrix;
    private final int rows;
    private final int columns;
    private final int width;
    private final int height;

    private boolean moveConsumed = true;
    private boolean snakeIncreaseConsumed = true;
    private boolean snakeFinished = false;

    private final int[] foodPosition;

    private int score;

    /* Neural Network */
    private final NeuralNetwork snakeBrain;

    /* Genetic Algorithm */
    private final boolean chromosome[];
    private final double vector[];

    private final double intervalStart;
    private final double intervalEnd;
    private static int PRECISION = 5;

    private final int vectorLength;
    private final int pointLength;
    private final int chromosomeLength;
    private final double intervalLength;
    private final int maximumBitValue;

    private String deathCause = "";

    private final double mutationRate = 0.01;

    public Snake(int[][] boardMatrix, int rows, int columns, int width, int height) {
        this.boardMatrix = boardMatrix;
        this.rows = rows;
        this.columns = columns;
        this.snakeCells = new ArrayList<>();
        this.snakeCellsHashSet = new HashSet<>();
        this.snakeCollisionObserverList = new ArrayList<>();
        this.score = 0;
        this.foodPosition = new int[2];
        this.width = width;
        this.height = height;

        this.snakeBrain = new NeuralNetwork(new int[]{24, 16, 4});
        this.vectorLength = snakeBrain.vectorSize();

        this.pointLength = (int)Math.ceil(Math.log(Math.pow(10, PRECISION)));
        this.chromosomeLength = vectorLength * pointLength;
        this.chromosome = new boolean[chromosomeLength];

        this.vector = new double[vectorLength];
        this.snakeBrain.toVector(vector);
        toChromosome(vector);

        this.intervalStart = 0.0;
        this.intervalEnd = 1.0;
        this.intervalLength = intervalEnd - intervalStart;
        this.maximumBitValue = (int)Math.pow(2, pointLength) - 1;


        initialize();
    }

    public void initialize() {
        direction[0] = generateRandom(-1, 2);
        direction[1] = 0;
        if (direction[0] == 0) {
            direction[1] = generateRandom(-1, 2);
        }
        System.out.println(Arrays.toString(direction));
        deleteSnake();

        centerSnakePosition();

        moveConsumed = true;
        snakeIncreaseConsumed = true;
        snakeFinished = false;
        score = 0;
    }

    private void randomSnakePosition() {
        int i = generateRandom(5, rows - 5);
        int j = generateRandom(5, columns - 5);

        int startingLength = 4;

        for (int l = 0; l < startingLength; l++) {
            SnakeCell lastSnakeCell = new SnakeCell(i, j);
            boardMatrix[lastSnakeCell.getI()][lastSnakeCell.getJ()] = CellType.SNAKE_CELL;

            List<SnakeCell> neighbours = getSnakeCellNeighbours(lastSnakeCell);
            snakeCells.add(lastSnakeCell);
            snakeCellsHashSet.add(lastSnakeCell);

            SnakeCell randomCell = null;
            if (neighbours.size() == 0) {
                break;
            }
            randomCell = neighbours.get(generateRandom(0, neighbours.size()));


            i = randomCell.getI();
            j = randomCell.getJ();

            if (l == 0) {
                direction[0] = lastSnakeCell.getI() - randomCell.getI();
                direction[1] = lastSnakeCell.getJ() - randomCell.getJ();
            }
        }
    }

    private void centerSnakePosition() {
        int currentI = rows / 2;
        int currentJ = columns / 2;

        int startingLength = 4;

        for (int i = 0; i < startingLength; i++) {
            snakeCells.add(new SnakeCell(currentI, currentJ));
            currentI -= direction[0];
            currentJ -= direction[1];
        }

    }

    private List<SnakeCell> getSnakeCellNeighbours(SnakeCell snakeCell) {
        List<SnakeCell> neighbours = new ArrayList<>();

        int i = snakeCell.getI();
        int j = snakeCell.getJ();

        if (i > 0) {
            if (boardMatrix[i - 1][j] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i - 1, j));
            }
        }

        if (j > 0) {
            if (boardMatrix[i][j - 1] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i, j - 1));
            }
        }

        if (i + 1 < rows) {
            if (boardMatrix[i + 1][j] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i + 1, j));
            }
        }

        if (j + 1 < columns) {
            if (boardMatrix[i][j + 1] == CellType.EMPTY_CELL) {
                neighbours.add(new SnakeCell(i, j + 1));
            }
        }

        return neighbours;
    }

    public void setFoodPosition(int i, int j) {
        foodPosition[0] = i;
        foodPosition[1] = j;
    }

    public void deleteSnake() {
        snakeCells.clear();
    }

    public String getDeathCause() {
        return deathCause;
    }

    public void setSnakeFinished() {
        snakeFinished = true;
    }

    public void onCrashListener(Observer observer) {
        snakeCollisionObserverList.add(observer);
    }

    public void makeStep() {
        if (snakeFinished) {
            return;
        }

        int newI = getNextI();
        int newJ = getNextJ();

        if (newI >= rows || newJ >= columns || newI < 0 || newJ < 0) {
            deathCause = "Snake wall collision";
            onSnakeCollide();
            return;
        }

        if (checkTailCollision(newI, newJ)) {
            deathCause = "Snake tail collision";
            onSnakeCollide();
            return;
        }

        SnakeCell head = snakeCells.get(0);
        boardMatrix[head.getI()][head.getJ()] = CellType.SNAKE_CELL;

        SnakeCell endTail = snakeCells.get(snakeCells.size() - 1);
        SnakeCell copyEndTail = endTail.getCopy();

        snakeCells.remove(endTail);
        snakeCellsHashSet.remove(endTail);

        endTail = new SnakeCell(newI, newJ);

        snakeCells.add(0, endTail);
        snakeCellsHashSet.add(endTail);

        boardMatrix[getHeadI()][getHeadJ()] = CellType.SNAKE_HEAD_CELL;

        if (!snakeIncreaseConsumed) {
            snakeCells.add(copyEndTail);
            snakeCellsHashSet.add(copyEndTail);
            snakeIncreaseConsumed = true;
        } else {
            boardMatrix[copyEndTail.getI()][copyEndTail.getJ()] = CellType.EMPTY_CELL;
        }

        moveConsumed = true;
        updateSnakeTail();

        /* Neural Network Prediction */
        predictNextMove();
    }

    private void updateSnakeTail() {
        for (int i = 0; i < snakeCells.size(); i++) {
            SnakeCell snakeCell = snakeCells.get(i);
            boardMatrix[snakeCell.getI()][snakeCell.getJ()] = i == 0 ? CellType.SNAKE_HEAD_CELL : CellType.SNAKE_CELL;
        }
    }

    private void onSnakeCollide() {
        deleteSnake();
        setSnakeFinished();
        snakeCollisionObserverList.forEach(Observer::update);
    }

    public boolean haveMoveConsumed() {
        return moveConsumed;
    }

    public void moveUp() {
        if (direction[0] == 1) {
            return;
        }

        direction[0] = -1;
        direction[1] = 0;
        moveConsumed = false;
    }

    public void moveRight() {
        if (direction[1] == -1) {
            return;
        }

        direction[0] = 0;
        direction[1] = 1;
        moveConsumed = false;
    }

    public void moveLeft() {
        if (direction[1] == 1) {
            return;
        }

        direction[0] = 0;
        direction[1] = -1;
        moveConsumed = false;
    }

    public void moveDown() {
        if (direction[0] == -1) {
            return;
        }

        direction[0] = 1;
        direction[1] = 0;
        moveConsumed = false;
    }

    public int getNextI() {
        return snakeCells.get(0).getI() + direction[0];
    }

    public int getNextJ() {
        return snakeCells.get(0).getJ() + direction[1];
    }

    public int getHeadI() {
        return snakeCells.get(0).getI();
    }

    public int getHeadJ() {
        return snakeCells.get(0).getJ();
    }

    public void setIncrease() {
        snakeIncreaseConsumed = false;
        score += 3;
    }

    public int getScore() {
        return score;
    }

    private int generateRandom(int start, int end) {
           return (((int)(Math.random() * 10000)) % (end - start)) + start;
    }

    public boolean isFinished() {
        return snakeFinished;
    }

    /* Neural Network */
    /** 8 directions and each one sees the distance between:
     * 1. wall
     * 2. tail
     * 3. food
     * 8 * 3 = 24 input size */
    private void predictNextMove() {
        double[] input = new double[24];
        Arrays.fill(input, 0.01);

        getDistancePack(1, 0, input, 0);
        getDistancePack(1, 1, input, 3);
        getDistancePack(0, 1, input, 6);
        getDistancePack(-1, 1, input, 9);
        getDistancePack(-1, 0, input, 12);
        getDistancePack(-1, -1, input, 15);
        getDistancePack(0, -1, input, 18);
        getDistancePack(1, -1, input, 21);

        double[] output = snakeBrain.getOutput(input);

        if (output != null) {
            makeMoveFromOutput(output);
        } else {
            System.out.println("Output is null");
        }

    }

    private void makeMoveFromOutput(double[] output) {
        int n = output.length;

        int iMax = -1;
        double maxOutput = 0;

        for (int i = 0; i < n; i++) {
            if (output[i] > maxOutput) {
                iMax = i;
                maxOutput = output[i];
            }
        }

        if (maxOutput > 0.9D) {
            if (iMax == 0) {
                moveUp();
            } else if (iMax == 1) {
                moveRight();
            } else if (iMax == 2) {
                moveDown();
            } else if (iMax == 3) {
                moveLeft();
            }
        }
    }

    private void getDistancePack(int dirX, int dirY, double[] buffer, int offset) {

        boolean wallDistanceSet = false;
        boolean tailDistanceSet = false;
        boolean foodDistanceSet = false;

        int headI = getHeadI();
        int headJ = getHeadJ();

        for (int i = headI, j = headJ; (i >= 0 && i < rows) && (j >= 0 && j < columns); i += dirY, j += dirX) {
            if (!wallDistanceSet && (i == 0 || j == 0 || i == rows - 1 || j == columns - 1)) {
                buffer[offset] = NeuralNetwork.normalizeOutput(1.0 - distanceBetween(i, j, headI, headJ));
                wallDistanceSet = true;
            }

            if (!tailDistanceSet && checkTailCollision(i, j)) {
                buffer[offset + 1] = NeuralNetwork.normalizeOutput(1.0 - distanceBetween(i, j, headI, headJ));
                tailDistanceSet = true;
            }

            if (!foodDistanceSet && (dirX == 0 || dirY == 0) && foodPosition[0] == i && foodPosition[1] == j) {
                buffer[offset + 2] = NeuralNetwork.normalizeOutput(1.0 - distanceBetween(i, j, headI, headJ));
                foodDistanceSet = true;
            }
        }
    }

    private double distanceBetween(double aX, double aY, double bX, double bY) {
        double distanceX = Math.abs(aX - bX);
        double distanceY = Math.abs(aY - bY);

        /* Normalizing the distance to be between 0 - 1 */
        distanceX /= (1.0 * height);
        distanceY /= (1.0 * width);

        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    private boolean checkTailCollision(int headI, int headJ) {
        return snakeCellsHashSet.contains(new SnakeCell(headI, headJ));
    }

    @Override
    public double fitness() {
        return score();
    }

    @Override
    public double score() {
        return score;
    }

    /* Point to chromosome and vice versa */
    private void toPoint(boolean[] chromosome, double buffer[]) {

        for (int i = 0; i < vectorLength; i++) {
            buffer[i] = chromosomeToPoint(i, chromosome);
        }

    }

    private boolean[] toChromosome(double[] X) {
        boolean[] chromosome = new boolean[chromosomeLength];

        for (int i = 0; i < vectorLength; i++) {
            pointToBitmap(i, X[i], chromosome);
        }

        return chromosome;
    }

    private double chromosomeToPoint(int position, boolean[] chromosome) {
        int bitPointValue = 0;

        int pStart = pointLength * position;
        int pEnd = pointLength * (position + 1) - 1;

        for (int i = pStart; i <= pEnd; i++) {
            bitPointValue = bitPointValue * 2 + (chromosome[i] ? 1 : 0);
        }

        return intervalStart + (1.0 * bitPointValue) * (intervalLength / maximumBitValue);
    }

    private void pointToBitmap(int position, double value, boolean[] chromosome) {

        int pStart = pointLength * position;
        int pEnd = pointLength * (position + 1) - 1;

        int valueToCode = (int)(((value - intervalStart) * maximumBitValue) / intervalLength);

        for (int i = pEnd; i >= pStart; i--) {
            chromosome[i] = (valueToCode % 2 == 1);
            valueToCode /= 2;
        }
    }

    private void updateBrain() {
        toPoint(chromosome, vector);
        snakeBrain.update(vector);
    }

    private void updateChromosome(boolean[] newChromosome) {
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = newChromosome[i];
        }

        updateBrain();
    }

    @Override
    public void mutate() {
        for (int i = 0; i < chromosome.length; i++) {
            double r = generateRandom(0.0, 1.0);

            if (r < mutationRate) {
                chromosome[i] = !chromosome[i];
            }
        }

        updateBrain();
    }

    @Override
    public void evaluate() {
        this.score = score;
    }

    @Override
    public List<Snake> makeOffspring(Snake snake) {
        List<Snake> offspring = new ArrayList<>(2);

        boolean[] chromosomeA = this.chromosome;
        boolean[] chromosomeB = snake.chromosome;

        int n = chromosome.length;

        int randomPivot = generateRandom(1, n - 1);

        boolean[] offspringChromosomeA = new boolean[n];
        boolean[] offspringChromosomeB = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (i <= randomPivot) {
                offspringChromosomeA[i] = chromosomeA[i];
                offspringChromosomeB[i] = chromosomeB[i];
                continue;
            }
            offspringChromosomeA[i] = chromosomeB[i];
            offspringChromosomeB[i] = chromosomeA[i];
        }

        Snake snake1 = new Snake(boardMatrix, rows, columns, width, height);
        Snake snake2 = new Snake(boardMatrix, rows, columns, width, height);

        offspring.add(snake1);
        offspring.add(snake2);

        snake1.updateChromosome(offspringChromosomeA);
        snake2.updateChromosome(offspringChromosomeB);

        return offspring;
    }

    private double generateRandom(double start, double end) {
        Random random = new Random();
        return start + (end - start) * random.nextDouble();
    }


    @Override
    public int compareTo(Snake snake) {
        double difference = this.fitness() - snake.fitness();
        if (difference < 0) {
            return -1;
        }

        if (difference > 0) {
            return 1;
        }

        return 0;
    }

    public int[] getDirection() {
        return direction;
    }
}
