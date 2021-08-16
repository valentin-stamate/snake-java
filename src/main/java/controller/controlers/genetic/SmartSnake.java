package controller.controlers.genetic;

import board.snake.Snake;
import ga.conversion.RangeDoubleToInterval;
import neural_network.NeuralNetwork;
import neural_network.activation.ActivationFunction;
import neural_network.activation.SigmoidFunction;
import neural_network.activation.TanhFunction;
import neural_network.bias.BiasInit;
import neural_network.bias.NullBias;
import neural_network.cost.CostFunction;
import neural_network.cost.SimpleCost;
import neural_network.weights.WeightsInit;
import neural_network.weights.XavierWeightsInit;
import util.Config;

import java.util.Arrays;

public class SmartSnake extends Snake {
    private final NeuralNetwork neuralNetwork;

    private static ActivationFunction activationFunction = new SigmoidFunction();
    private static WeightsInit weightsInit = new XavierWeightsInit();
    private static BiasInit biasInit = new NullBias();
    private static CostFunction costFunction = new SimpleCost();
//    private static LossFunction lossFunction = new MeanSquaredError();

    private int consecutiveSteps = 0;
    private int oldScore = 0;

    public SmartSnake(int[][] board, short[] gene, int[] layerSize) {
        super(board);

        double[] weights = RangeDoubleToInterval.toDoubleVector(gene, Config.START, Config.END, Config.PRECISION);
        double[][][] brain = GeneticAiUtil.vectorToBrain(weights, layerSize);

        this.neuralNetwork = new NeuralNetwork(brain, layerSize, activationFunction, weightsInit, biasInit, costFunction);

        oldScore = score;
    }

    /* 8 directions: for each one sees the distance between the walls, tail and food, in total 24 */
    public void predictNext() {
        double[] input = getNormalizedVision();
        double[] output = neuralNetwork.feedForward(input);

        double maxProb = GeneticAiUtil.getMax(output);

//        System.out.println("Input: " + Arrays.toString(input));
//        System.out.println("Probs: " + Arrays.toString(output));

        /* UP */
        if (maxProb == output[0]) {
            moveUp();
        }

        /* LEFT */
        if (maxProb == output[1]) {
             moveLeft();
        }

        /* DOWN */
        if (maxProb == output[2]) {
             moveDown();
        }

        /* RIGHT */
        if (maxProb == output[3]) {
            moveRight();
        }

    }

    public static int calculateGeneLength(int[] layerSize) {
        int elements = 0;

        for (int i = 0; i < layerSize.length - 1; i++) {
            elements += layerSize[i] * layerSize[i + 1];
        }

        int lengthPerElement = RangeDoubleToInterval.calculateBitPointLength(Config.END - Config.START, Config.PRECISION);

        return elements * lengthPerElement;
    }

    @Override
    public void makeStep() {
        predictNext();
        super.makeStep();

        if (oldScore != score) {
            consecutiveSteps = 0;
            oldScore = score;
        }

        if (consecutiveSteps == 28 * 28) {
            snakeFinished = true;
        }

        consecutiveSteps++;
    }

}
