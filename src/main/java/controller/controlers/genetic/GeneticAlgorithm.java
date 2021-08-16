package controller.controlers.genetic;

import ga.AbstractGeneticAlgorithm;
import ga.config.GaConfig;
import ga.conversion.RangeDoubleToInterval;
import ga.member.AbstractMember;
import ga.operators.crossover.AbstractCrossover;
import ga.operators.mutation.AbstractMutation;
import ga.operators.selection.AbstractSelection;
import neural_network.NeuralNetwork;
import util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithm extends AbstractGeneticAlgorithm {
    public GeneticAlgorithm(GaConfig gaConfig, AbstractMutation abstractMutation, AbstractCrossover abstractCrossover, AbstractSelection abstractSelection) {
        super(gaConfig, abstractMutation, abstractCrossover, abstractSelection);
    }

    @Override
    public List<AbstractMember> generatePopulation() {
        List<AbstractMember> population = new ArrayList<>();

        for (int i = 0; i < gaConfig.populationSize; i++) {
            NeuralNetwork neuralNetwork = new NeuralNetwork(Config.LAYER_SIZE);

            double[][][] brain = neuralNetwork.getBrainReference();
            double[] vector = GeneticAiUtil.brainToVector(brain, Config.LAYER_SIZE);
            short[] bitmap = RangeDoubleToInterval.toBitMapVector(vector, Config.START, Config.END, Config.PRECISION);

            population.add(new Member(bitmap));
        }

        return population;
    }

    @Override
    public List<AbstractMember> getAbstractMembersFromGene(List<short[]> list, GaConfig gaConfig, AbstractMutation abstractMutation) {
        List<AbstractMember> members = new ArrayList<>();

        for (short[] gene : list) {
            members.add(new Member(gene));
        }

        return members;
    }

    @Override
    public void selectPopulation(List<AbstractMember> population) {
        List<AbstractMember> bestNThMembers = new ArrayList<>();
        List<AbstractMember> copyPopulation = getPopulationCopy(population);

        super.selectPopulation(population);

        copyPopulation.sort(Collections.reverseOrder());

        int min = Math.min(5, population.size());

        for (int i = 0; i < min; i++) {
            bestNThMembers.add(copyPopulation.get(i));
        }

        population.addAll(bestNThMembers);
    }
}
