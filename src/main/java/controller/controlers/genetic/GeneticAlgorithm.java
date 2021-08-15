package controller.controlers.genetic;

import ga.AbstractGeneticAlgorithm;
import ga.config.GaConfig;
import ga.member.AbstractMember;
import ga.operators.crossover.AbstractCrossover;
import ga.operators.mutation.AbstractMutation;
import ga.operators.selection.AbstractSelection;

import java.util.List;

public class GeneticAlgorithm extends AbstractGeneticAlgorithm {
    public GeneticAlgorithm(GaConfig gaConfig, AbstractMutation abstractMutation, AbstractCrossover abstractCrossover, AbstractSelection abstractSelection) {
        super(gaConfig, abstractMutation, abstractCrossover, abstractSelection);
    }

    @Override
    public List<AbstractMember> generatePopulation() {
        return null;
    }

    @Override
    public List<AbstractMember> getAbstractMembersFromGene(List<short[]> list, GaConfig gaConfig, AbstractMutation abstractMutation) {
        return null;
    }
}
