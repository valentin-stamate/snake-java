package controller.controlers.genetic;

import ga.member.AbstractMember;
import java.util.Arrays;

public class Member extends AbstractMember {

    public Member(int geneLength) {
        super(geneLength);
    }

    public Member(short[] gene) {
        super(gene);
    }

    @Override
    public void calculateFitness() {
        super.fitness = score;
    }

    @Override
    public void calculateScore() {

    }

    public void setScore(double score) {
        super.score = score;
    }

    @Override
    public AbstractMember getCopy() {
        Member member = new Member(Arrays.copyOf(gene, gene.length));

        member.setScore(score);
        member.calculateFitness();

        return member;
    }
}
