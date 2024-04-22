package dev.mochahaulier.springbackendtest1.services;

import static java.util.UUID.randomUUID;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import dev.mochahaulier.springbackendtest1.model.LetterState;
import dev.mochahaulier.springbackendtest1.model.GameData;
import dev.mochahaulier.springbackendtest1.model.GameResponse;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;
import static dev.mochahaulier.springbackendtest1.model.LetterState.CORRECT;
import static dev.mochahaulier.springbackendtest1.model.LetterState.ABSENT;
import static dev.mochahaulier.springbackendtest1.model.LetterState.PRESENT;
import static dev.mochahaulier.springbackendtest1.model.GameState.LOSS;
import static dev.mochahaulier.springbackendtest1.model.GameState.WIN;
import static dev.mochahaulier.springbackendtest1.model.GameState.INVALID;
import static dev.mochahaulier.springbackendtest1.model.GameState.VALID;

import static java.util.Optional.of;

@Scope(value = SCOPE_SINGLETON)
@Service
public class GameService {
    ConcurrentHashMap<String, GameData> games = new ConcurrentHashMap<>();

    public String addGame(String solution) {
        String userKey = randomUUID().toString();
        games.put(userKey, new GameData(solution, 0));
        return userKey;
    }

    public GameResponse checkGuess(String guess, String userToken, DictionaryService dictService) {
        GameData userGame = of(games.get(userToken))
                .orElseThrow(() -> new RuntimeException("Session doesn't exist"));

        int turn = userGame.getTurn();

        // is word long enough
        if (guess.length() < 5) {
            games.replace(userToken, userGame);
            return new GameResponse(turn, INVALID, null, null);
        }
        // check word validity
        if (!(dictService.isValidWord(guess))) {
            games.replace(userToken, userGame);
            return new GameResponse(turn, INVALID, null, null);
        }
        // check word duplicity
        if (userGame.getGuesses().contains(guess)) {
            games.replace(userToken, userGame);
            return new GameResponse(turn, INVALID, null, null);
        }

        userGame.setTurn(++turn);
        String solution = userGame.getSolution();

        // save all non correctly placed letters
        List<Character> present = IntStream.range(0, solution.length())
                .filter(i -> guess.charAt(i) != solution.charAt(i))
                .mapToObj(solution::charAt)
                .collect(Collectors.toCollection(ArrayList::new));

        Map<Integer, LetterState> letterState = new LinkedHashMap<>();

        for (int i = 0; i < solution.length(); i++) {
            if (guess.charAt(i) == solution.charAt(i)) {
                letterState.put(i, CORRECT);
            } else if (present.remove((Character) guess.charAt(i))) {
                letterState.put(i, PRESENT);
            } else {
                letterState.put(i, ABSENT);
            }
        }

        userGame.addGuess(guess, letterState);

        if (solution.equalsIgnoreCase(guess)) {
            games.remove(userToken);
            return new GameResponse(turn, WIN, letterState, guess);
        } else if (turn > 5) {
            games.remove(userToken);
            return new GameResponse(turn, LOSS, letterState, solution);
        } else {
            games.replace(userToken, userGame);
            return new GameResponse(turn, VALID, letterState, null);
        }
    }
}
