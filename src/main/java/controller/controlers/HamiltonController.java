package controller.controlers;

import board.snake.Snake;
import controller.SnakeController;
import controller.node.Node;
import observer.OnUpdateObserver;
import processing.core.PApplet;
import util.Config;
import util.Util;
import java.util.*;
import java.util.stream.Collectors;

public class HamiltonController extends SnakeController {

    private final int[] path;
    private final Snake snake;

    public HamiltonController(PApplet pApplet) {
        super(pApplet);
        this.path = getPath();
        this.snake = new Snake(super.board.getBoardMatrix());
        super.snakeList.add(snake);
    }

    @Override
    public void run() {

        super.board.start();
        board.setOnRefreshListener((OnUpdateObserver) () -> {

            int i = snake.getHeadI();
            int j = snake.getHeadJ();

            int currentNode = Util.getNodeCount(i, j);
            int nextNode = path[currentNode];

            int nI = nextNode / Config.BOARD_COLUMNS;
            int nJ = nextNode % Config.BOARD_COLUMNS;

            int[] newDirection = new int[2];
            newDirection[0] = nI - i;
            newDirection[1] = nJ - j;

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

    public int[] getPath() {
        int n = Config.BOARD_ROWS;
        int m = Config.BOARD_COLUMNS;

        int[] path = new int[n * m];

        int[][] halfMST = getHalfMST();
        int[][] walls = convertToWalls(halfMST);

        int[][] graphPath = convertToFullPath(walls);

        Node startNode = new Node(0, 0);
        path[0] = 1;

        Node currentNode = new Node(0, 1);

        int[] direction = new int[]{0, 1};

        while (!currentNode.equals(startNode)) {

            List<Node> neighbours = getGraphPathNeighbours(graphPath, currentNode, direction);

            if (neighbours.size() == 0) {
                /* This should never be displayed */
                System.out.println("Something went wrong");
                break;
            }

            Node nextNode = neighbours.get(0);

            int nodeCount = mapToNormalGraph(currentNode);
            int nextNodeCount = mapToNormalGraph(nextNode);

            path[nodeCount] = nextNodeCount;

            direction[0] = nextNode.getI() - currentNode.getI();
            direction[1] = nextNode.getJ() - currentNode.getJ();

            currentNode = nextNode;
        }

        path[0] = 1;

        return path;
    }

    int mapToNormalGraph(Node node) {
        int i = node.getI();
        int j = node.getJ();

        return mapToNormalIndex(i) * Config.BOARD_COLUMNS + mapToNormalIndex(j);
    }

    private int mapToNormalIndex(int n) {
        if (n % 3 == 0) {
            return (n / 3) * 2;
        }

        return (n / 3) * 2 + (n % 3 + 1) % 2;
    }

    private List<Node> getGraphPathNeighbours(int[][] graphPath, Node node, int[] direction) {
        List<Node> neighbours = new ArrayList<>();

        neighbours.add(getNodeSafely(graphPath, node.getI() + direction[1], node.getJ() - direction[0])); // (y, -x)
        neighbours.add(getNodeSafely(graphPath, node.getI() + direction[0], node.getJ() + direction[1])); // (x, y)
        neighbours.add(getNodeSafely(graphPath, node.getI() - direction[1], node.getJ() + direction[0])); // (-y, x)

        neighbours = neighbours.stream().filter(Objects::nonNull).collect(Collectors.toList());
        neighbours = neighbours.stream().filter(neighbour -> {
            int i = neighbour.getI();
            int j = neighbour.getJ();
            return graphPath[i][j] == 0;
        }).collect(Collectors.toList());

        return neighbours;
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
            reachableNodes = randomizeQueue(reachableNodes);
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

    /* I know it's not time efficient, but it's good for it's purpose */
    private Queue<Node> randomizeQueue(Queue<Node> queue) {
        List<Node> buffer = new ArrayList<>(queue);

        Collections.shuffle(buffer);

        return new LinkedList<>(buffer);
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

    private Node getNodeSafely(int[][] graph, int i, int j) {
        try {
            int n = graph[i][j];
            return new Node(i, j);
        } catch (Exception ignored) { }

        return null;
    }

}
