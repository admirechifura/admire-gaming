package game.kalaha.service;

import java.util.List;

public interface Board {
    /**
     * The rules of the kalaha game should be defined here
     */
    void makeAMove(int startPosition);
    boolean isGameOver();
    List<Integer> getPitList();
    int getIndexLargePitPlayer1North();
    int getIndexlargePitPlayer2South();
    boolean isPlayer2SouthTurn();
    void setPlayer2SouthTurn(boolean player2SouthTurn);
    boolean isEmpty(int position);
}
