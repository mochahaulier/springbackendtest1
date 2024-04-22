package dev.mochahaulier.springbackendtest1.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private int turn;
    private GameState status;
    private Map<Integer, LetterState> letterState;
    private String solution;
}
