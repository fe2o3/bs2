package org.domain.bs.bscardgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.domain.bs.gametools.Card;
import org.domain.bs.gametools.Deck;
import org.domain.bs.gametools.DiscardPile;
import org.domain.bs.gametools.Hand;
import org.domain.bs.metagame.GameRoom;

public class BSCardGameRoom extends GameRoom {

  private Long id;
  private List<String> playerOrder = new ArrayList<String>();
  private Map<String, Hand> playersHands = new HashMap<String, Hand>();


  private String hostUser;

  private Deck deck = new Deck();
  private DiscardPile discard = new DiscardPile();

  private int previousPlayQuantity;

  private boolean challengeAvailable = false;

  public boolean isChallengeAvailable() {
    return challengeAvailable;
  }

  public boolean potentialWinnerExists() {
    return playerWithNoCards() != null;
  }

  public String playerWithNoCards() {
    Iterator<String> iter = playersHands.keySet().iterator();
    while(iter.hasNext()) {
      String player = iter.next();
      if (playersHands.get(player).getCards().isEmpty()) {
        return player;
      }
    }
    return null;
  }
  public Deck getDeck() {
    return deck;
  }

  public void currentPlayerFailedChallenge() throws Exception {
    List<Card> l = discard.drawWholePile();
    playersHands.get(getCurrentPlayer()).drawIntoHand(l);
    challengeAvailable = false;
  }

  public void previousPlayerFailedChallenge() throws Exception {
    List<Card> l = discard.drawWholePile();
    playersHands.get(getPreviousPlayer()).drawIntoHand(l);
    challengeAvailable = false;
  }

  public DiscardPile getDiscard() {
    return discard;
  }
  private int turn = 0;

  public String getPreviousCardValue() throws Exception {
    if (turn == 0)
      throw new Exception("There was no previous card");
    return Deck.STANDARDCARDORDER[(turn - 1) % Deck.STANDARDCARDORDER.length];
  }
  public String getCurrentCardValue() {
    return Deck.STANDARDCARDORDER[turn % Deck.STANDARDCARDORDER.length];
  }

  public String getPreviousPlayer() throws Exception {
    if (turn == 0)
      throw new Exception("There was no previous player");
    return playerOrder.get((turn - 1) % playerOrder.size());
  }
  public String getCurrentPlayer() {
    return playerOrder.get(turn % playerOrder.size());
  }

  public Hand getPlayerHand(String name) {
    return playersHands.get(name);
  }
  public void incrementTurn() {
    challengeAvailable = true;
    turn++;
  }
  /**
   * @return the id
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the players
   */
  public List<String> getPlayers() {
    return playerOrder;
  }





  /**
   * @return the hostUser
   */
  public String getHostUser() {
    return hostUser;
  }

  /**
   * @param hostUser
   *          the hostUser to set
   */
  public void setHostUser(String hostUser) {
    this.hostUser = hostUser;
  }

  /**
   * @return the turn
   */
  public int getTurn() {
    return turn;
  }

  @Override
  public void startGame() {

    playerOrder = new ArrayList<String>(playersHands.keySet());
    deck.shuffleRemaining();

    dealWholeDeck();
  }

  @Override
  public void Construct(Long i, String host) {

    id = i;
    hostUser = host;

  }
  private void dealWholeDeck() {
    int playercounter = 0;
    while(!deck.isEmpty()) {
      Card c = deck.draw();
      String player = playerOrder.get(playercounter % playerOrder.size());
      playersHands.get(player).drawIntoHand(c);
      playercounter++;
    }
  }

  public BSCardGameRoom() {
  }

  /**
   * @return the previousPlayQuantity
   */
  public int getPreviousPlayQuantity() {
    return previousPlayQuantity;
  }

  /**
   * @param previousPlayQuantity
   *          the previousPlayQuantity to set
   */
  public void setPreviousPlayQuantity(int previousPlayQuantity) {
    this.previousPlayQuantity = previousPlayQuantity;
  }

  @Override
  public String roomType() {
    return "BS Card Game";
  }

  @Override
  public void enter(String username) {
    if (!playersHands.containsKey(username)) {
      playersHands.put(username, new Hand());
    }
  }

  @Override
  public void leave(String username) {
    if (playersHands.containsKey(username)) {
      playersHands.remove(username);
      playerOrder.remove(username);
    }
  }

  @Override
  public List<String> listMembers() {
    return new ArrayList<String>(playersHands.keySet());
  }

  private String winner = null;

  @Override
  public String getWinner() {
    return winner;
  }

  public void setWinner(String win) {
    winner = win;
  }

}
