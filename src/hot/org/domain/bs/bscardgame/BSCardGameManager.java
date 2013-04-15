package org.domain.bs.bscardgame;

import java.util.List;

import org.domain.bs.gametools.Card;
import org.domain.bs.gametools.Hand;



public class BSCardGameManager {


  public static void takeTurn(Hand hand, BSCardGameRoom gameState, List<Card> cardsPlayed) throws Exception {
    if (gameState == null)
      throw new Exception("Null Game");
    if (hand == null)
      throw new Exception("Null hand");

    hand.discard(cardsPlayed);

    gameState.getDiscard().discardIntoPile(cardsPlayed);
    int qty = cardsPlayed.size();
    gameState.getGameLog().add(gameState.getCurrentPlayer() + " played " + qty + " " + gameState.getCurrentCardValue() + "s.");

    // gameState.getGameLog().add("(actually it was " + listCards(cardsPlayed) +
    // ")");

    gameState.setPreviousPlayQuantity(qty);

    gameState.incrementTurn();

  }

  private static String listCards(List<Card> cards) {
    String actual = "";
    for (int i = 0; i < cards.size(); i++) {
      actual += " " + cards.get(i).getKey();
    }
    return actual;
  }

  public static void declareVictor(BSCardGameRoom gameState) throws Exception {
    if (gameState == null)
      throw new Exception("Null Game");

    if (!gameState.potentialWinnerExists())
      throw new Exception("Cant challenge");

    gameState.setWinner(gameState.playerWithNoCards());
    gameState.getGameLog().add(gameState.playerWithNoCards() + " wins.");

  }
  public static void challengePrevious(BSCardGameRoom gameState) throws Exception {
    if (gameState == null)
      throw new Exception("Null Game");



    if (!gameState.isChallengeAvailable())
      throw new Exception("Cant challenge");

    int n = gameState.getPreviousPlayQuantity();

    String val = gameState.getPreviousCardValue();
    List<Card> top = gameState.getDiscard().viewTopNCards(n);

    if (allCardsAreValue(top, val)) {
      // the previous player was honest. current player fails.
      gameState.currentPlayerFailedChallenge();
      gameState.getGameLog().add(gameState.getCurrentPlayer() + " challenged " + gameState.getPreviousPlayer() + " and lost.");
      if (gameState.potentialWinnerExists()) {
        declareVictor(gameState);
      }
    } else {
      //
      gameState.previousPlayerFailedChallenge();
      gameState.getGameLog().add(gameState.getCurrentPlayer() + " challenged " + gameState.getPreviousPlayer() + " and won.");
    }

    // gameState.getGameLog().add("supposed to be " + val +
    // ", he actually played " + listCards(top));

  }

  private static boolean allCardsAreValue(List<Card> cards, String val) {
    for (int i = 0; i < cards.size(); i++) {
      if (!cards.get(i).getValue().equals(val))
        return false;
    }
    return true;
  }

}
