package game.kalaha.api;

import game.kalaha.service.KalahaGame;
import game.kalaha.service.KalahaGameImpl;
import game.kalaha.web.KalahaGameUI;
import game.kalaha.web.domain.KalahaBoardHTMLDisplayData;
import game.kalaha.web.domain.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class KalahaGameController {

    private final KalahaGameUI kalahaGameUI;
    private final KalahaGame kalahaGame;
    private final Logger logger = LoggerFactory.getLogger(KalahaGameController.class);

    @Autowired
    public KalahaGameController(final KalahaGameUI kalahaGameUI, final KalahaGameImpl kalahaGame) {
        this.kalahaGameUI = kalahaGameUI;
        this.kalahaGame = kalahaGame;
    }

    @GetMapping("/")
    ModelAndView getStartPage() {
        return new ModelAndView("index");
    }

    @GetMapping("/board")
    public ModelAndView getBoard(final Model model) {
        addAttributesToModel(model);
        logger.info("getBoard called");
        return new ModelAndView("kalahaboard", model.asMap());
    }

    @GetMapping("/play")
    public ModelAndView performMove(@ModelAttribute final Payload payload, final Model model) {
        logger.info("Received index:{}", payload.getIndex());
        int chosenIndex = payload.getIndex();
        boolean isSouthTurn = payload.getIsSouthTurn();

        int pitListIndex = isSouthTurn ? chosenIndex : chosenIndex + kalahaGame.getOffsetPlayerNorth();
        logger.info("The pitListIndex for the player is:{}", pitListIndex);

        if (kalahaGame.isChosenPitEmpty(pitListIndex)) {
            displayErrorMessageToModel(model, chosenIndex);
        } else {
            play(pitListIndex, isSouthTurn);
        }
        displayGameOverMessageIfGameHasEnded(model);
        addAttributesToModel(model);
        return new ModelAndView("kalahaboard", model.asMap());
    }

    private void play(final int pitListIndex, final boolean southTurn) {
        logger.info("Starting to play the game with southTurn set to:{} and pitListIndex:{}", southTurn, pitListIndex);
        kalahaGame.setSouthTurn(southTurn);
        kalahaGame.play(pitListIndex);
        logger.info("Finished making the move with southTurn set to:{} and pitListIndex:{}", southTurn, pitListIndex);
    }

    private void displayErrorMessageToModel(final Model model, final int index) {
        logger.info("Displaying error message to model chosen pit contains no pebbles");
        model.addAttribute("errorMessage", String.format("The chosen pit %s contains no pebbles "
                + "please select another pit", index + 1));
    }

    private void displayGameOverMessageIfGameHasEnded(final Model model) {
        if (kalahaGame.isGameOver()) {
            model.addAttribute("gameOverMessage", kalahaGame.displayWinner());
        }
    }

    private void addAttributesToModel(final Model model) {
        KalahaBoardHTMLDisplayData kalahaBoardHTMLDisplayData = kalahaGameUI.getBoardHtmlDataFromGame(kalahaGame);
        model.addAttribute("kalahaBoardHtmlDisplayData", kalahaBoardHTMLDisplayData);
        model.addAttribute("southTurn", kalahaGame.isSouthTurn());
    }

}
