package controller.controlers.genetic;

import board.snake.Snake;
import controller.SnakeController;
import ga.AbstractGeneticAlgorithm;
import ga.config.GaConfig;
import ga.lambda.BeforeEvaluationEvent;
import ga.member.AbstractMember;
import ga.operators.crossover.AbstractCrossover;
import ga.operators.crossover.OnePointCrossover;
import ga.operators.mutation.AbstractMutation;
import ga.operators.mutation.SimpleMutation;
import ga.operators.selection.AbstractSelection;
import ga.operators.selection.TournamentSelection;
import neural_network.NeuralNetwork;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class GeneticAiController extends SnakeController {

    public GeneticAiController(PApplet pApplet) {
        super(pApplet);
    }

    @Override
    public void run() {

        int[] layerSize = new int[]{3, 4, 5, 3};
        int geneLength = SmartSnake.calculateGeneLength(layerSize);

        NeuralNetwork neuralNetwork = new NeuralNetwork(layerSize);

        GaConfig gaConfig = GaConfig.initializeWithParameters(
                100,
                1000,
                1,
                geneLength,
                0.0015,
                0.02
        );

        AbstractMutation abstractMutation = new SimpleMutation();
        AbstractCrossover abstractCrossover = new OnePointCrossover();
        AbstractSelection abstractSelection = new TournamentSelection();

        AbstractGeneticAlgorithm abstractGeneticAlgorithm = new GeneticAlgorithm(
                gaConfig,
                abstractMutation,
                abstractCrossover,
                abstractSelection
        );

        List<SmartSnake> smartSnakes = new ArrayList<>();

        BeforeEvaluationEvent beforeEvaluationEvent = (population) -> {
            for (AbstractMember abstractMember : population) {
                short[] gene = abstractMember.getGeneCopy();

                Snake snake = new Snake(super.board.getBoardMatrix());
                SmartSnake smartSnake = new SmartSnake(snake, gene, layerSize);

                smartSnakes.add(smartSnake);

                snakeList.clear();
                snakeList.add(smartSnake.getSnake());
            }

            while (!board.allSnakesFinished()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < snakeList.size(); i++) {
                Snake snake = snakeList.get(i);
                AbstractMember abstractMember = population.get(i);

                ((Member) abstractMember).setScore(snake.getScore());
            }

        };

        new Thread(abstractGeneticAlgorithm::start).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.board.start();

        while (!board.allSnakesFinished()) {
            for (SmartSnake smartSnake : smartSnakes) {
                Snake snake = smartSnake.getSnake();

                if (!snake.isFinished()) {
                    smartSnake.predictNext();
                }
            }
        }

    }
}
