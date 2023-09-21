package game.kalaha.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class KalahaBoardImpl implements Board{
    /**
     * The rules of the kalaha game should be defined here
     * Implements the board rules of Kalaha game
     * NB: We start our count of index/position(0) from player2 playing Southerly direction to player1 Northerly direction
     */

    private List<Integer> pitList;
    private boolean player2SouthTurn;
    private final Logger logger = LoggerFactory.getLogger(KalahaBoardImpl.class);

    @PostConstruct
    public void init() {
        this.player2SouthTurn = false;
        int totalNumberOfPitsInTheGame = 2 * 6 + 2; //include 2 largePits
        List<Integer> grandTotalOfPits = IntStream
                .generate(() -> 6)
                .limit(totalNumberOfPitsInTheGame)
                .boxed()
                .collect(Collectors.toList());
        grandTotalOfPits.set(grandTotalOfPits.size() / 2 - 1, 0);//initialize south set large pit to zero pebbles
        grandTotalOfPits.set(grandTotalOfPits.size() - 1, 0);//initialize north set large pit to zero pebbles
        this.pitList = grandTotalOfPits;
    }

    public void makeAMove(int startPosition) {
        //retrieve and allocate pebbles
        int pebbles = getPebblesInPit(startPosition);
        logger.info("Retrieved {}:pebbles from postion:{}", pebbles, startPosition);
        int lastPit = startPosition;
        while (pebbles > 0) {
            lastPit = nextPit(lastPit);
            addPebblesInPit(lastPit);
            pebbles--;
        }
        emptyPit(startPosition);
        logger.info("Emptied pit for position:{}", startPosition);
        //Capturing Stones
        captureIfLastPitIsOwnEmptyPit(lastPit);
        //The Game ends
        collectLastPebblesIfGameIsOver();
        //The Game continues, the other players turn if this lastPit is not Large Pit
        switchTurnsIfLastPitIsNotOwnLargePit(lastPit);
        logger.info("Player move has completed at position:{}", lastPit);
    }

    private void switchTurnsIfLastPitIsNotOwnLargePit(final int lastPit) {
        if (isNotOwnLargePit(lastPit)) { //Check if lastPit is not largePit of either player to give the other a chance to play
            setPlayer2SouthTurn(isNotPlayer2SouthTurn());
        }
    }

    private boolean isNotOwnLargePit(final int position) {
        return (isPlayer2SouthTurn() && position != getIndexlargePitPlayer2South())
                || (isNotPlayer2SouthTurn() && position != getIndexLargePitPlayer1North());
    }

    private void collectLastPebblesIfGameIsOver() {
        if (isGameOver()) {
            int lastPebblesPlayer2South = getTotalPebblesInPitsPlayer2South();
            int lastPebblesPlayer1North = getTotalPebblesInPitsPlayer1North();
            collectPebbles(lastPebblesPlayer2South, lastPebblesPlayer1North);//Game has ended collect all the pebbles into largePit
            emptyNormalPits(); //The game has ended empty all pits for the other player into the largePit.
            logger.info("Game is over!");
        }
    }

    private void emptyNormalPits() {//empty all pits for the other player
        IntStream.range(0, getIndexlargePitPlayer2South()).forEach(this::emptyPit);
        IntStream.range(getIndexlargePitPlayer2South() + 1, getIndexLargePitPlayer1North()).forEach(this::emptyPit);
    }

    private void collectPebbles(final int lastPebblesPlayer2South, final int lastPebblesPlayer1North) {
        int newTotalPlayer2South = getPebblesInPit(getIndexlargePitPlayer2South()) + lastPebblesPlayer2South; //add total number of pebbles for player2
        int newTotalPlayer1North = getPebblesInPit(getIndexLargePitPlayer1North()) + lastPebblesPlayer1North; //add total number of pebbles for player1
        setPebblesInPit(getIndexlargePitPlayer2South(), newTotalPlayer2South);//final amount of pebbles in largePit for player2South
        setPebblesInPit(getIndexLargePitPlayer1North(), newTotalPlayer1North);//final amount of pebbles in largePit for player1 North
        logger.info("Sum of all pebbles for player2South:{} and player1North:{}", newTotalPlayer2South, newTotalPlayer1North);
    }

    private void captureIfLastPitIsOwnEmptyPit(final int position) {
        logger.info("Inside captureIfLastPitIsOwnEmptyPit for position:{}", position);
        if (pitContainsOnePebble(position) && isANormalPit(position) && isInOwnPlayersNormalPit(position)) {//The game is over as soon as one player run out of pebbles
            int currentPlayPositionOfPlayer = isPlayer2SouthTurn() ? getIndexlargePitPlayer2South() : getIndexLargePitPlayer1North();
            int pebblesInHand = getPebblesInPit(position) + getPebblesInPit(oppositePit(position));
            setPebblesInPit(currentPlayPositionOfPlayer, getPebblesInPit(currentPlayPositionOfPlayer) + pebblesInHand);
            emptyPit(position);
            emptyPit(oppositePit(position));
            logger.info("Following conditions pitContainsOnePebble,isANormalPit,isInOwnPlayersNormalPit fulfilled");
        }
    }

    private int oppositePit(final int position) {
        if (isPlayer2SouthTurn()) {
            return getPitList().size() / 2 + position;
        } else {
            return position - getPitList().size() / 2;
        }
    }

    private boolean isInOwnPlayersNormalPit(final int position) { //This is generic so has checks for either player if not landing in own Large Pit
        return (isPlayer2SouthTurn() && position < getIndexlargePitPlayer2South()) || (isNotPlayer2SouthTurn() && position < getIndexLargePitPlayer1North());
    }

    private boolean isANormalPit(final int position) { //This checks if current pit is not a Large pit for any player
        return position != getIndexlargePitPlayer2South() && position != getIndexLargePitPlayer1North();
    }

    private boolean pitContainsOnePebble(final int position) {
        return getPebblesInPit(position) == 1;
    }

    private void emptyPit(final int position) { //On each turn of play, a player retrieves all pebbles in the pit
        getPitList().set(position, 0);
    }

    private void addPebblesInPit(final int currentPosition) {
        setPebblesInPit(currentPosition, getPebblesInPit(currentPosition) + 1); //we drop a single pebble each time
    }

    private void setPebblesInPit(final int position, final int value) {
        getPitList().set(position, value);
    }

    private int nextPit(final int currentPosition) {
        int nextIndex = isLastIndex(currentPosition) ? 0 : currentPosition + 1; //Start of game index/position
        return skipLargePitOpponent(nextIndex); //Determine play position
    }

    private int skipLargePitOpponent(final int currentPosition) {
        if (isPlayer2SouthTurn() && currentPosition == getIndexLargePitPlayer1North()) { //Player 2 South turn to play
            logger.info("Player2 South turn");
            return 0;
        }
        if (isNotPlayer2SouthTurn() && currentPosition == getIndexlargePitPlayer2South()) { //Player 1 North turn to play
            logger.info("Player1 North turn");
            return currentPosition + 1;
        }
        return currentPosition;
    }

    private boolean isNotPlayer2SouthTurn() {
        return !isPlayer2SouthTurn();
    }

    private boolean isLastIndex(final int index) {
        return index == getIndexLargePitPlayer1North();
    }


    private int getPebblesInPit(final int currentPosition) {
        return getPitList().get(currentPosition);
    }

    public boolean isGameOver() {
        return getTotalPebblesInPitsPlayer2South() == 0 || getTotalPebblesInPitsPlayer1North() == 0;
    }

    private int getTotalPebblesInPitsPlayer2South() {
        return IntStream.range(0, getIndexlargePitPlayer2South()).map(this::getPebblesInPit).sum();
    }

    private int getTotalPebblesInPitsPlayer1North() {
        return IntStream.range(getIndexlargePitPlayer2South() + 1, getIndexLargePitPlayer1North()).map(this::getPebblesInPit).sum();
    }

    public List<Integer> getPitList() {
        return pitList;
    }

    public int getIndexLargePitPlayer1North() {
        return getPitList().size() - 1; //Large pit
    }

    public int getIndexlargePitPlayer2South() { //For the other player(player2South) Large pit, we divide the total size of pit list by 2
        return getPitList().size() / 2 - 1;
    }

    public boolean isPlayer2SouthTurn() {
        return player2SouthTurn;
    }

    public void setPlayer2SouthTurn(boolean player2SouthTurn) {
        this.player2SouthTurn = player2SouthTurn;
    }

    public boolean isEmpty(int position) {
        return getPebblesInPit(position) == 0;
    }

    public static final class KalahaBoardImplBuilder {
        private List<Integer> pitList;

        public KalahaBoardImplBuilder pitList(final int pitsPerPlayer, final int pebblesPerPit) {
            int totalNumberOfPitsInTheGame = 2 * pitsPerPlayer + 2; //include largePits
            List<Integer> grandTotalOfPits = IntStream
                    .generate(() -> pebblesPerPit)
                    .limit(totalNumberOfPitsInTheGame)
                    .boxed()
                    .collect(Collectors.toList());
            grandTotalOfPits.set(grandTotalOfPits.size() / 2 - 1, 0);//initialize south set large pit to zero pebbles
            grandTotalOfPits.set(grandTotalOfPits.size() - 1, 0);//initialize north set large pit to zero pebbles
            this.pitList = grandTotalOfPits;
            return this;
        }
    }
}
