package controller.controlers.genetic;

import board.snake.Snake;
import ga.conversion.RangeDoubleToInterval;
import neural_network.NeuralNetwork;

public class SmartSnake {
    private final Snake snake;

    private final NeuralNetwork neuralNetwork;
    private final short[] gene;

    /* I know, I know, uppercase */
    private static final double start = -10;
    private static final double end = 10;
    private static final int precision = 3;

    public SmartSnake(Snake snake, short[] gene, int[] layerSize) {
        this.snake = snake;
        this.gene = gene;

        double[] weights = RangeDoubleToInterval.toDoubleVector(gene, start, end, precision);
        double[][][] brain = GeneticAiUtil.vectorToBrain(weights, layerSize);

        this.neuralNetwork = new NeuralNetwork(brain, layerSize);
    }

    public SmartSnake(Snake snake, int[] layerSize) {
        this.snake = snake;
        this.neuralNetwork = new NeuralNetwork(layerSize);
        double[][][] brain = neuralNetwork.getBrainReference();

        double[] weights = GeneticAiUtil.brainToVector(brain, layerSize);

        this.gene = RangeDoubleToInterval.toBitMapVector(weights, start, end, precision);
    }

    public Snake getSnake() {
        return snake;
    }

    /* 8 directions: for each one sees the distance between the walls, tail and food, in total 24 */
    public void predictNext() {
        double[] input;
        double[] output;
    }

    public static int calculateGeneLength(int[] layerSize) {
        int elements = 0;

        for (int i = 0; i < layerSize.length - 1; i++) {
            elements += layerSize[i] * layerSize[i + 1];
        }

        int lengthPerElement = RangeDoubleToInterval.calculateBitPointLength(end - start, precision);

        return elements * lengthPerElement;
    }

}
