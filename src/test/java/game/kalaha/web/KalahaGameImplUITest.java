package game.kalaha.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import game.kalaha.service.KalahaBoardImpl;
import game.kalaha.service.KalahaGameImpl;
import game.kalaha.web.domain.KalahaBoardHTMLDisplayData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class KalahaGameImplUITest {

    private KalahaGameImpl kalahaGameImpl;
    private KalahaGameUI kalahaGameUI;

    @BeforeEach
    public void setup() {
        KalahaBoardImpl kalahaBoardImpl = KalahaBoardImpl.builder()
                .pitList(6, 6)
                .player2SouthTurn(true)
                .build();
        kalahaGameImpl = KalahaGameImpl.builder().board(kalahaBoardImpl).build();
        kalahaGameUI = new KalahaGameUI();
    }

    @Test
    public void verifyGetBoardHTMLDisplayDataIsCorrectBeforePlay() {
        KalahaBoardHTMLDisplayData expected = KalahaBoardHTMLDisplayData.builder()
                .rowSouth(Arrays.asList(6, 6, 6, 6, 6, 6))
                .largePitSouth(0)
                .rowNorth(Arrays.asList(6, 6, 6, 6, 6, 6))
                .largePitNorth(0)
                .build();
        KalahaBoardHTMLDisplayData result = kalahaGameUI.getBoardHtmlDataFromGame(kalahaGameImpl);
        verifyBoardHtmlData(result, expected);
    }

    @Test
    public void verifyGetBoardHTMLDisplayDataReturnsCorrectDataAfterPlay() {
        KalahaBoardHTMLDisplayData expected = KalahaBoardHTMLDisplayData.builder()
                .rowSouth(Arrays.asList(0, 0, 8, 8, 8, 8))
                .largePitSouth(2)
                .rowNorth(Arrays.asList(6, 6, 6, 6, 7, 7))
                .largePitNorth(0)
                .build();

        kalahaGameImpl.play(0);
        kalahaGameImpl.play(1);

        KalahaBoardHTMLDisplayData result = kalahaGameUI.getBoardHtmlDataFromGame(kalahaGameImpl);

        verifyBoardHtmlData(result, expected);
    }

    private void verifyBoardHtmlData(KalahaBoardHTMLDisplayData result, KalahaBoardHTMLDisplayData expected) {
        assertTrue(result.getRowSouth().containsAll(expected.getRowSouth()));
        assertTrue(result.getLargePitSouth().compareTo(expected.getLargePitSouth()) == 0);
        assertTrue(result.getRowNorth().containsAll(expected.getRowNorth()));
        assertTrue(result.getLargePitNorth().compareTo(expected.getLargePitNorth()) == 0);
    }
}
