package com.example.snakebackend.score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreModel, Integer> {

    @Query("SELECT sm FROM ScoreModel sm ORDER BY sm.score DESC")
    List<ScoreModel> getTopScores();

}
