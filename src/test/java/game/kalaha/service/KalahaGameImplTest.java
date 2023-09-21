package game.kalaha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class KalahaGameImplTest {
    private KalahaGameImpl kalahaGameImpl;

    @BeforeEach
    public void setup() {
        KalahaBoardImpl board = KalahaBoardImpl.builder()
                .pitList(6, 6)
                .player2SouthTurn(true)
                .build();
        kalahaGameImpl = new KalahaGameImpl(board);
    }

    @Test
    public void whenPlayIsCalledExpectTheBoardToBeUpdated() {
        List<Integer> givenPitList = Arrays.asList(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
        List<Integer> expectedPitList = Arrays.asList(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0);
        int startPosition = 0;
        //before game is played
        assertEquals(givenPitList, kalahaGameImpl.getBoard().getPitList());
        //Now play the game
        kalahaGameImpl.play(startPosition);

        assertEquals(expectedPitList, kalahaGameImpl.getBoard().getPitList());
    }

    @Test
    public void displayPlayer2SouthWinnerMessage() {
        String expectedWinnerMessage = "Player2 South has won!";
        List<Integer> givenPitList = Arrays.asList(0, 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 0, 0, 6);
        IntStream.range(0, givenPitList.size())
                .forEach(position -> kalahaGameImpl.getBoard().getPitList().set(position, givenPitList.get(position)));
        assertEquals(expectedWinnerMessage, kalahaGameImpl.displayWinner());
    }

    @Test
    public void displayPlayer1NorthWinnerMessage() {
        String expectedWinnerMessage = "Player1 North has won!";
        List<Integer> givenPitList = Arrays.asList(0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 45);
        IntStream.range(0, givenPitList.size())
                .forEach(position -> kalahaGameImpl.getBoard().getPitList().set(position, givenPitList.get(position)));
        assertEquals(expectedWinnerMessage, kalahaGameImpl.displayWinner());
    }

    @Test
    public void displayNoWinnerMessage() {
        String expectedWinnerMessage = "It`s a draw!";
        List<Integer> givenPitList = Arrays.asList(0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 6);
        IntStream.range(0, givenPitList.size())
                .forEach(position -> kalahaGameImpl.getBoard().getPitList().set(position, givenPitList.get(position)));
        assertEquals(expectedWinnerMessage, kalahaGameImpl.displayWinner());
    }

}
