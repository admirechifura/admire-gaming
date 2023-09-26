package game.kalaha.service;

import java.util.List;

public interface KalahaGame {
    void play(int startPosition);

    String displayWinner();

    int getTotalPebblesForPlayer2South();

    int getTotalPebblesForPlayer1North();

    void setSouthTurn(boolean southTurn);

    List<Integer> getPitListSouth();

    List<Integer> getPitListNorth();

    int getPebblesLargePitSouth();

    int getPebblesLargePitNorth();

    boolean isSouthTurn();

    int getOffsetPlayerNorth();

    boolean isChosenPitEmpty(int chosenIndex);

    boolean isGameOver();

    Board getBoard();
}
