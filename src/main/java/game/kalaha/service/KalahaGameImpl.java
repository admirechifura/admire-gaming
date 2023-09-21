package game.kalaha.service;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Component
@RequiredArgsConstructor
public class KalahaGameImpl implements KalahaGame {
    /**
     * Kalaha/Mancala Game is played by 2 players with 6pits/holes infront of them
     * Is a board game
     * Each player also has a larger pit in addition to the 6pits/holes
     * In each pit there are 6pebbles at the start of the game
     */

    private final Board board;
    private final Logger logger = LoggerFactory.getLogger(KalahaGameImpl.class);

    @Override
    public void play(final int startPosition) { //This starts the game
        logger.info("About to make a move from startPosition:{}", startPosition);
        board.makeAMove(startPosition);
    }

    @Override
    public String displayWinner() {
        int player2DifferenceInNumberOfPebbles = getTotalPebblesForPlayer2South() - getTotalPebblesForPlayer1North();
        if (player2DifferenceInNumberOfPebbles == 0) {
            logger.info("It`s a draw!");
            return "It`s a draw!";
        }
        logger.info("If the differenceInNumberOfPebbles:{} is greater than 0 Player2 South has won else Player1 North has won", player2DifferenceInNumberOfPebbles);
        return player2DifferenceInNumberOfPebbles > 0 ? "Player2 South has won!" : "Player1 North has won!";
    }

    @Override
    public int getTotalPebblesForPlayer2South() {
        int positionOfLargePit = board.getIndexlargePitPlayer2South();
        return board.getPitList().get(positionOfLargePit);
    }

    @Override
    public int getTotalPebblesForPlayer1North() {
        int positionOfLargePit = board.getIndexLargePitPlayer1North();
        return board.getPitList().get(positionOfLargePit);
    }

    //Below added for frontend
    @Override
    public void setSouthTurn(final boolean southTurn) {
        board.setPlayer2SouthTurn(southTurn);
    }

    @Override
    public List<Integer> getPitListSouth() {
        return new ArrayList<>(board.getPitList().subList(0, board.getIndexlargePitPlayer2South()));//6
    }

    @Override
    public List<Integer> getPitListNorth() {
        return new ArrayList<>(board.getPitList().subList(board.getIndexlargePitPlayer2South() + 1, board.getIndexLargePitPlayer1North()));
    }

    @Override
    public int getPebblesLargePitSouth() {
        int largePitIndex = board.getIndexlargePitPlayer2South();
        return board.getPitList().get(largePitIndex);
    }

    @Override
    public int getPebblesLargePitNorth() {
        int largePitIndex = board.getIndexLargePitPlayer1North();
        return board.getPitList().get(largePitIndex);
    }

    @Override
    public boolean isSouthTurn() {
        return board.isPlayer2SouthTurn();
    }

    @Override
    public int getOffsetPlayerNorth() {
        return board.getPitList().size() / 2;
    }

    @Override
    public boolean isChosenPitEmpty(final int chosenIndex) {
        return board.isEmpty(chosenIndex);
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }
}
