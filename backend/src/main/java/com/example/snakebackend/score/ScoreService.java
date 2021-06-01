package com.example.snakebackend.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    @Autowired
    ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public List<ScoreModel> getTopScores() {
        List<ScoreModel> scores = scoreRepository.getTopScores();
        int len = scores.size();
        scores = scores.subList(0, Math.min(len, 10));
        return scores;
    }

    public void save(ScoreModel scoreModel) {
        scoreRepository.save(scoreModel);
    }
}
