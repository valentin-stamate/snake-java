package controller;

import board.CellType;
import board.snake.Snake;
import board.snake.food.Food;
import controller.node.Node;
import observer.OnRefresh;
import processing.core.PApplet;
import util.Config;
import util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class HamiltonController extends SnakeController {

    private final int[][] pathGraph;
    private Snake snake;

    public HamiltonController(PApplet pApplet) {
        super(pApplet);
        this.pathGraph = getPath();
        this.snake = new Snake(super.board.getBoardMatrix());
        super.snakeList.add(snake);
    }

    @Override
    public void run() {

        for (int i = 0; i < pathGraph.length; i++) {
            for (int j = 0; j < pathGraph[0].length; j++) {
                System.out.print(pathGraph[i][j] + " ");
            }
            System.out.println("");
        }

        super.board.start();
        board.setOnRefreshListener(() -> {
            int i = snake.getHeadI();
            int j = snake.getNextJ();

            System.out.println("Current position:" + i + " " + j);

            int pathI = i + (i + 1) / 2;
            int pathJ = j + (j + 1) / 2;

            Node node = new Node(pathI, pathJ);
            int[] direction = snake.getDirection();
            Node nextPosition = getNext(pathGraph, node, direction);

            int[] newDirection = new int[2];
            newDirection[0] = nextPosition.getI() - node.getI();
            newDirection[1] = nextPosition.getJ() - node.getJ();

            if (newDirection[0] == 0 && newDirection[1] == 1) {
                snake.moveRight();
            }

            if (newDirection[0] == 0 && newDirection[1] == -1) {
                snake.moveLeft();
            }

            if (newDirection[0] == -1 && newDirection[1] == 0) {
                snake.moveUp();
            }

            if (newDirection[0] == 1 && newDirection[1] == 0) {
                snake.moveDown();
            }

        });
    }

    private int[][] getPath() {
        int[][] halfMST = getHalfMST();
        int[][] walls = convertToWalls(halfMST);

        return convertToFullPath(walls);
    }

    private int[][] convertToFullPath(int[][] mst) {
        int n = Config.BOARD_ROWS;
        int m = Config.BOARD_COLUMNS;

        int[][] fullPath = new int[n + n / 2][m + m / 2];

        int fN = fullPath.length;
        int fM = fullPath[0].length;

        for (int i = 0; i < fN; i++) {
            for (int j = 0; j < fM; j++) {
                if (i == 0 || i == fN - 1 || j == 0 || j == fM - 1) {
                    continue;
                }

                int mstValue = mst[i - i / 3][j - j / 3];

                fullPath[i][j] = mstValue;
            }
        }

        return fullPath;
    }

    private int[][] convertToWalls(int[][] halfMST) {
        int n = Config.BOARD_ROWS;
        int m = Config.BOARD_COLUMNS;
        int[][] walls = new int[n][m];

        int halfN = n * m / 4;
        int halfM = n * m / 4;

        for (int i = 0; i < halfN; i++) {
            for (int j = 0; j < halfM; j++) {
                if (halfMST[i][j] == 1) {
                    int pointAI = i / (m / 2);
                    int pointAJ = i % (m / 2);

                    int pointBI = j / (m / 2);
                    int pointBJ = j % (m / 2);

                    int currentAI = pointAI * 2 + 1;
                    int currentAJ = pointAJ * 2 + 1;

                    int nextAI = pointBI * 2 + 1;
                    int nextAJ = pointBJ * 2 + 1;

                    int middleAI = (currentAI + nextAI) / 2;
                    int middleAJ = (currentAJ + nextAJ) / 2;

                    walls[currentAI][currentAJ] = 1;
                    walls[middleAI][middleAJ] = 1;
                    walls[nextAI][nextAJ] = 1;

                }
            }
        }

        return walls;
    }

    private int[][] getHalfMST() {
        final int n = Config.BOARD_ROWS;
        final int m = Config.BOARD_COLUMNS;

        Node[][] halfGraph = getGraph(n / 2, m / 2);
        int[][] rawHalfGraph = new int[n * m / 4][n * m / 4];

        Queue<Node> reachableNodes = new LinkedList<>();
        HashSet<Node> visitedNodes = new HashSet<>();

        reachableNodes.add(halfGraph[0][0]);

        while (!reachableNodes.isEmpty()) {
            Node node = reachableNodes.poll();
            int nCount = Util.getNodeCount(node.getI(), node.getJ(), m / 2);

            visitedNodes.add(node);

            List<Node> neighbours = getUnvisitedNeighbours(halfGraph, visitedNodes, node);

            for (Node neighbour : neighbours) {
                if (reachableNodes.contains(neighbour)) {
                    continue;
                }
                reachableNodes.add(neighbour);
                int neighCount = Util.getNodeCount(neighbour.getI(), neighbour.getJ(), m / 2);

                rawHalfGraph[nCount][neighCount] = 1;
                rawHalfGraph[neighCount][nCount] = 1;
            }

        }

        return rawHalfGraph;
    }

    private List<Node> getUnvisitedNeighbours(Node[][] graph, HashSet<Node> visitedNodes, Node node) {
        List<Node> neighbours = new ArrayList<>();

        int i = node.getI();
        int j = node.getJ();

        neighbours.add(getNodeSafely(graph, i - 1, j));
        neighbours.add(getNodeSafely(graph, i, j + 1));
        neighbours.add(getNodeSafely(graph, i + 1, j));
        neighbours.add(getNodeSafely(graph, i, j - 1));

        List<Node> unvisitedNeighbours = new ArrayList<>();
        for (Node neighbour : neighbours) {
            if (neighbour == null) {
                continue;
            }

            if (visitedNodes.contains(neighbour)) {
                continue;
            }

            unvisitedNeighbours.add(neighbour);
        }

        return unvisitedNeighbours;
    }

    private Node getNodeSafely(Node[][] graph, int i, int j) {
        try {
            return graph[i][j];
        } catch (Exception ignored) { }

        return null;
    }

    private Node[][] getGraph(int n, int m) {
        Node[][] graph = new Node[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                graph[i][j] = new Node(i, j);
            }
        }

        return graph;
    }

    private Node getNext(int[][] graph, Node node, int[] direction) {
        int i = node.getI();
        int j = node.getJ();

        List<Node> neighbours = new ArrayList<>();

        neighbours.add(getNodeSafely(graph, i - direction[1], j + direction[0]));
        neighbours.add(getNodeSafely(graph, i + direction[0], j + direction[1]));
        neighbours.add(getNodeSafely(graph, i + direction[1], j - direction[0]));

        neighbours = neighbours.stream().filter(Objects::nonNull).collect(Collectors.toList());

        neighbours = neighbours.stream().filter(neighbour -> {
            int nI = neighbour.getI();
            int nJ = neighbour.getJ();
            return graph[nI][nJ] == 0;
        }).collect(Collectors.toList());

        return neighbours.get(0);
    }

    private Node getNodeSafely(int[][] graph, int i, int j) {
        try {
            int n = graph[i][j];
            return new Node(i, j);
        } catch (Exception ignored) { }

        return null;
    }

}
