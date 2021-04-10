package genetic_algorithm;

import java.util.List;

public class GeneticAlgorithm<T extends GeneticAlgorithmMember<T>> {
    private static double MUTATION_RATE = 0.02;
    private static int POPULATION_SIZE = 10;
    private static int MAX_GENERATIONS = 1000;
    private static double CROSSOVER_PROBABILITY = 0.2;

    private final List<T> population;

    private int generationNumber = 0;

    public GeneticAlgorithm(double intervalStart, double intervalEnd, int points, List<T> population) {
        this.population = population;
        evaluate();
    }


    public void loopGeneration() {
        generationNumber++;
        select();
        mutate();
        crossover();
        evaluate();
    }

    private void mutate() {
        for (T member : population) {
            member.mutate();
        }
    }

    private void crossover() {

    }

    private void select() {

    }

    private void evaluate() {
        for (T member : population) {
            member.evaluate();
        }
    }


}
