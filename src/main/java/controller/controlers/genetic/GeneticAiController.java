package controller.controlers.genetic;

import board.snake.Snake;
import controller.SnakeController;
import ga.AbstractGeneticAlgorithm;
import ga.config.GaConfig;
import ga.lambda.BeforeEvaluationEvent;
import ga.lambda.observers.OnNewGeneration;
import ga.member.AbstractMember;
import ga.operators.crossover.AbstractCrossover;
import ga.operators.crossover.OnePointCrossover;
import ga.operators.mutation.AbstractMutation;
import ga.operators.mutation.SimpleMutation;
import ga.operators.selection.AbstractSelection;
import ga.operators.selection.TournamentSelection;
import processing.core.PApplet;
import util.Config;

import java.util.Collections;

public class GeneticAiController extends SnakeController {

    public GeneticAiController(PApplet pApplet) {
        super(pApplet);
    }

    @Override
    public void run() {

        int geneLength = SmartSnake.calculateGeneLength(Config.LAYER_SIZE);

        GaConfig gaConfig = GaConfig.initializeWithParameters(
                800,
                1000,
                1,
                geneLength,
                0.0005,
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

        BeforeEvaluationEvent beforeEvaluationEvent = (copyReferencePopulation) -> {
            /* Put the population on the board */
            snakeList.clear();
            for (AbstractMember abstractMember : copyReferencePopulation) {
                short[] gene = abstractMember.getGeneCopy();

                SmartSnake smartSnake = new SmartSnake(super.board.getBoardMatrix(), gene, Config.LAYER_SIZE);

                snakeList.add(smartSnake);
            }

            /* Wait until the all of them are done */
            while (!board.allSnakesFinished()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /* TODO: implement a method in GA to print all scores */

            /* Calculate the score (= fitness) */
            for (int i = 0; i < snakeList.size(); i++) {
                Snake snake = snakeList.get(i);
                AbstractMember abstractMember = copyReferencePopulation.get(i);

                ((Member) abstractMember).setScore(snake.getScore() * 50 + snake.getSteps() / 10 + 1);
                abstractMember.calculateFitness();
            }

        };

        OnNewGeneration onNewGeneration = ((copyPopulation, generation) -> {
            copyPopulation.sort(Collections.reverseOrder());

            System.out.printf("Generation %d. Best fitness %6.2f\n", generation, copyPopulation.get(0).getFitness());
        });

        abstractGeneticAlgorithm.addPopulationObserver(onNewGeneration);

        abstractGeneticAlgorithm.addBeforeEvaluationObserver(beforeEvaluationEvent);

        super.board.start();

        new Thread(abstractGeneticAlgorithm::start).start();
    }
}
