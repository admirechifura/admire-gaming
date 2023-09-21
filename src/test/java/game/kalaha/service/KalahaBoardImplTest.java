package game.kalaha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class KalahaBoardImplTest {

    private Board board;

    @BeforeEach
    public void setup() {
        board = KalahaBoardImpl.builder()
                .pitList(6, 6)
                .player2SouthTurn(true)  //start with player2 south
                .build();
    }

    @Test
    public void checkIfBoardHasBeenSetupCorrectly() {
        List<Integer> expectedResults = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        assertEquals(expectedResults, board.getPitList());
        assertEquals(true, board.isPlayer2SouthTurn());
    }

    @Test
    public void whenPlayer2MakeASingleMoveTheCorrectPitListIsReturned() {
        List<Integer> givenPitListForPlayer2 = Arrays.asList(0, 1, 8, 8, 8, 8, 2, 6, 0, 7, 7, 8, 8, 1);
        List<Integer> expectedPitListForPlayer2 = Arrays.asList(0, 0, 9, 8, 8, 8, 2, 6, 0, 7, 7, 8, 8, 1);
        addPitListToBoard(board, givenPitListForPlayer2);

        board.makeAMove(1); //start playing from position 1
        assertEquals(expectedPitListForPlayer2, board.getPitList());
    }

    @Test
    public void whenPlayer1MakeASingleMoveTheCorrectPitListIsReturned() {
        List<Integer> expectedPitListForPlayer1 = Arrays.asList(7, 7, 7, 7, 7, 7, 0, 0, 7, 7, 7, 7, 0, 2);
        board.setPlayer2SouthTurn(false); //Player1 turn to play the game
        board.makeAMove(7);
        board.makeAMove(12);

        assertEquals(expectedPitListForPlayer1, board.getPitList());
    }

    @Test
    public void whenPlayer2SouthMakesMultipleMovesTheCorrectPitListIsReturned() {
        List<Integer> initial = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        board.makeAMove(0); //player2 moves south
        assertEquals(board.getPitList(), Arrays.asList(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0));
        assertTrue(board.isPlayer2SouthTurn());//player2 south turn again

        board.makeAMove(1); //player2 moves south
        assertEquals(board.getPitList(), Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0));
        assertFalse(board.isPlayer2SouthTurn());//now player1 turn next
    }

    @Test
    public void whenPlayer1NorthMakesMultipleMovesTheCorrectPitListIsReturned() {
        List<Integer> initial = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        board.setPlayer2SouthTurn(false);
        board.makeAMove(7); //player1 moves north
        assertEquals(board.getPitList(), Arrays.asList(6, 6, 6, 6, 6, 6, 0, 0, 7, 7, 7, 7, 7, 1));
        assertFalse(board.isPlayer2SouthTurn());//player1 north turn again

        board.makeAMove(8); //player1 moves north
        assertEquals(board.getPitList(), Arrays.asList(7, 7, 6, 6, 6, 6, 0, 0, 0, 8, 8, 8, 8, 2));
        assertTrue(board.isPlayer2SouthTurn());//now player2 turn next
    }

    @Test
    public void player2SouthSkipLargePitOfOpponentSuccessfully() {
        List<Integer> givenPitList = Arrays.asList(6, 6, 6, 6, 6, 8, 0, 6, 6, 6, 6, 6, 6, 0);
        List<Integer> expectedPitList = Arrays.asList(7, 6, 6, 6, 6, 0, 1, 7, 7, 7, 7, 7, 7, 0);
        addPitListToBoard(board, givenPitList);

        board.makeAMove(5);

        assertEquals(expectedPitList, board.getPitList());
    }

    @Test
    public void player1NorthSkipLargePitOfOpponentSuccessfully() {
        List<Integer> givenPitList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 8, 0);
        List<Integer> expectedPitList = Arrays.asList(7, 7, 7, 7, 7, 7, 0, 7, 6, 6, 6, 6, 0, 1);
        addPitListToBoard(board, givenPitList);
        board.setPlayer2SouthTurn(false); //Player1 North turn to play

        board.makeAMove(12);

        assertEquals(expectedPitList, board.getPitList());
    }

    @Test
    @DisplayName("There is no switch turn when last pebble for player2 south lands in own LargePit")
    public void player2NoSwitchTurnsIfLastPitIsOwnLargePit() {
        List<Integer> givenPitList = Arrays.asList(6, 6, 6, 6, 6, 1, 0, 6, 6, 6, 6, 6, 6, 0);
        List<Integer> expectedPitList = Arrays.asList(6, 6, 6, 6, 6, 0, 1, 6, 6, 6, 6, 6, 6, 0);
        addPitListToBoard(board, givenPitList);

        assertTrue(board.isPlayer2SouthTurn()); //Player2 is the one playing

        board.makeAMove(5);

        assertTrue(board.isPlayer2SouthTurn()); //no switch has happened
        assertEquals(expectedPitList, board.getPitList());
    }

    @Test
    @DisplayName("Player2 captures when last pebbles lands in own empty pit and adds to own largePit")
    public void player2CaptureIfLastPitIsOwnEmptyPitSuccessfully() {
        List<Integer> givenPitList = Arrays.asList(6, 6, 6, 6, 1, 0, 0, 6, 6, 6, 6, 6, 6, 0);
        List<Integer> expectedPitList = Arrays.asList(6, 6, 6, 6, 0, 0, 7, 6, 6, 6, 6, 6, 0, 0);
        addPitListToBoard(board, givenPitList);
        board.makeAMove(4);

        assertEquals(expectedPitList, board.getPitList());
    }

    @Test
    @DisplayName("Player1 captures when last pebbles lands in own empty pit and adds to own largePit")
    public void player1CaptureIfLastPitIsOwnEmptyPitSuccessfully() {
        List<Integer> givenPitList = Arrays.asList(1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0);
        List<Integer> expectedPitList = Arrays.asList(1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 2);
        addPitListToBoard(board, givenPitList);
        board.setPlayer2SouthTurn(false);
        board.makeAMove(10);

        assertEquals(expectedPitList, board.getPitList());
    }

    @Test
    @DisplayName("Player1 North collects all his pebbles when Player2 south runs out of stones")
    public void player1NorthCollectPebblesSuccessfully() {
        List<Integer> givenPitList = Arrays.asList(0, 0, 0, 0, 0, 1, 5, 5, 6, 6, 6, 6, 6, 10);
        List<Integer> expectedPitList = Arrays.asList(0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 45);
        addPitListToBoard(board, givenPitList);
        board.makeAMove(5);

        assertTrue(board.isGameOver());
        assertEquals(expectedPitList, board.getPitList());
    }

    @Test
    public void getIndexlargePitPlayer2SouthReturnsCorrectResult() {
        int expectedIndex = 6;
        assertEquals(expectedIndex, board.getIndexlargePitPlayer2South());
    }

    @Test
    public void getIndexLargePitPlayer1NorthReturnsCorrectResult() {
        int expectedIndex = 13;
        assertEquals(expectedIndex, board.getIndexLargePitPlayer1North());
    }

    @Test
    @DisplayName("This should return true if player2 south has no pebbles left")
    public void isGameOverForPlayer2South() {
        List<Integer> givenPitList = Arrays.asList(0, 0, 0, 0, 0, 0, 5, 5, 6, 6, 6, 6, 6, 47);
        addPitListToBoard(board, givenPitList);

        assertTrue(board.isGameOver());
    }

    @Test
    @DisplayName("This should return true if player1 north has no pebbles left")
    public void isGameOverForPlayer1North() {
        List<Integer> givenPitList = Arrays.asList(6, 6, 6, 6, 6, 5, 5, 0, 0, 0, 0, 0, 0, 47);
        addPitListToBoard(board, givenPitList);
        assertTrue(board.isGameOver());
    }

    @Test
    @DisplayName("Returns false if both players still have pebbles in the pits")
    public void isGameOverFails() {
        List<Integer> givenPitList = Arrays.asList(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0);
        addPitListToBoard(board, givenPitList);

        assertFalse(board.isGameOver());
    }

    private void addPitListToBoard(Board board, List<Integer> pitList) {
        IntStream.range(0, pitList.size())
                .forEach(position -> board.getPitList().set(position, pitList.get(position)));
    }

}
