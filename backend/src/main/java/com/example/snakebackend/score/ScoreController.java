package com.example.snakebackend.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/scores")
    public ResponseEntity<Object> getScores() {
        return new ResponseEntity<>(scoreService.getTopScores(), HttpStatus.OK);
    }

    @PostMapping("/score")
    public ResponseEntity<Object> postScore(@RequestBody ScoreModel scoreModel) {
        scoreService.save(scoreModel);
        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
    }

}
