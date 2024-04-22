package dev.mochahaulier.springbackendtest1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameData {
    private int turn;
    private String solution;
    private List<Map<Integer, LetterState>> letterStates = new ArrayList<>();
    private List<String> guesses = new ArrayList<String>();

    public GameData(String solution, int turn) {
        this.solution = solution;
        this.turn = turn;
    }

    public void addGuess(String guess, Map<Integer, LetterState> letterStates) {
        this.guesses.add(guess);
        this.letterStates.add(letterStates);
    }
}
