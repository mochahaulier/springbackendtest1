package dev.mochahaulier.springbackendtest1.controller;

import org.springframework.web.bind.annotation.RestController;

import dev.mochahaulier.springbackendtest1.model.GameResponse;
import dev.mochahaulier.springbackendtest1.services.GameService;
import dev.mochahaulier.springbackendtest1.services.DictionaryService;

import lombok.AllArgsConstructor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@CrossOrigin("http://localhost:3000")
@RestController
@AllArgsConstructor
public class GameController {
    private GameService gameService;
    private DictionaryService dictionaryService;

    @GetMapping("/newGame")
    public ResponseEntity<String> newGame() {
        String solution = dictionaryService.getSolution();
        return new ResponseEntity<String>(gameService.addGame(solution),
                HttpStatus.OK);
    }

    @PostMapping("/submitGuess")
    public ResponseEntity<GameResponse> submitGuess(@RequestParam String guess, @RequestParam String userToken) {
        return new ResponseEntity<GameResponse>(gameService.checkGuess(guess, userToken, dictionaryService),
                HttpStatus.OK);
    }
}
