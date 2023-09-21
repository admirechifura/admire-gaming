package game.kalaha.web;

import game.kalaha.service.KalahaGame;
import game.kalaha.web.domain.KalahaBoardHTMLDisplayData;
import org.springframework.stereotype.Component;

@Component
public class KalahaGameUI {

    public KalahaBoardHTMLDisplayData getBoardHtmlDataFromGame(final KalahaGame kalahaGame){
        return KalahaBoardHTMLDisplayData.builder()
                .rowSouth(kalahaGame.getPitListSouth())
                .rowNorth(kalahaGame.getPitListNorth())
                .largePitSouth(kalahaGame.getPebblesLargePitSouth())
                .largePitNorth(kalahaGame.getPebblesLargePitNorth())
                .build();
    }
}
