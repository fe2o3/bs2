package org.domain.bs.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.domain.bs.application.MasterGameList;
import org.domain.bs.application.MasterUserList;
import org.domain.bs.bscardgame.BSCardGameManager;
import org.domain.bs.bscardgame.BSCardGameRoom;
import org.domain.bs.gametools.Card;
import org.domain.bs.gametools.Hand;
import org.domain.bs.session.UserBean;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("gameBean")
public class GameBean {


  @In(create = true)
  UserBean userBean;

  @In(create = true)
  MasterGameList masterGameList;

  @In(create = true)
  MasterUserList masterUserList;

  private String error;

  /**
   * @return the error
   */
  public String getError() {
    return error;
  }

  /**
   * @param error
   *          the error to set
   */
  public void setError(String error) {
    this.error = error;
  }

  private List<String> selectedCards = new ArrayList<String>();

  /**
   * @return the selectedCards
   */
  public List<String> getSelectedCards() {
    return selectedCards;
  }

  /**
   * @param selectedCards
   *          the selectedCards to set
   */
  public void setSelectedCards(List<String> selectedCards) {
    this.selectedCards = selectedCards;
  }

  private BSCardGameRoom getGame() {
    return (BSCardGameRoom) userBean.getUser().getInGame();
  }
  public boolean getYourTurn() {
    return getGame().getCurrentPlayer().equals(userBean.getUser().getUsername());

  }

  public void playTurn() {
    error = "";
    if (selectedCards.size() > 4) {
      error = " too many cards selected";
      return;
    }
    try {
      List<Card> selected = new ArrayList<Card>();
      for (String s : selectedCards) {
        selected.add(getMyHand().getCards().get(s));
      }
      BSCardGameManager.takeTurn(getMyHand(), getGame(), selected);
    } catch (Exception e) {
      error = e.getMessage() + e.getCause();
    }
  }

  public void challenge() {
    error = "";
    try {
      BSCardGameManager.challengePrevious(getGame());
    } catch (Exception e) {
      error = e.getMessage() + e.getCause();
    }
  }

  public boolean canChallenge() {
    return getGame().isChallengeAvailable();
  }

  public List<String> getGameLog() {
    return getGame().getGameLog();
  }

  public List<String> getPlayers() {
    return getGame().getPlayers();
  }

  public String getCurrentPlayer() {
    return getGame().getCurrentPlayer();
  }

  public String getCurrentCard() {
    return getGame().getCurrentCardValue();
  }

  public Hand getMyHand() {
    return getGame().getPlayerHand(userBean.getUser().getUsername());
  }

  public List<Card> getMyOrderedHand() {
    Hand h = getGame().getPlayerHand(userBean.getUser().getUsername());
    List<Card> l = new ArrayList<Card>();
    List<String> keys = new ArrayList<String>();

    keys.addAll(h.getCards().keySet());
    Collections.sort(keys);
    // Iterator it = h.getCards().keySet().iterator();
    for (String k : keys) {
      l.add(h.getCards().get(k));
    }
    return l;
  }

  private boolean playerInGame(String player) {
    return getGame().getPlayerHand(player) != null;
  }
  public int getHandSize(String player) {
    if (playerInGame(player)) {
      return getGame().getPlayerHand(player).getCards().keySet().size();
    }
    return 0;
  }

  public int getPileSize()
  {
    return getGame().getDiscard().getSize();
  }

  public String getPreviousPlayer() {
    try {
      return getGame().getPreviousPlayer();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public boolean canPlay() {
    try {
      return getGame().getPlayerHand(getGame().getPreviousPlayer()).getCards().size() > 0;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return true;

  }

  public boolean canDeclareWinner() {

    return getGame().potentialWinnerExists();

  }

  public void declareWinner() {
    error = "";
    try {
      BSCardGameManager.declareVictor(getGame());
    } catch (Exception e) {
      error = e.getMessage() + e.getCause();
    }
  }

  public String getWinner() {
    return getGame().getWinner();
  }
}
