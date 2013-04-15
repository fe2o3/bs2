package org.domain.bs.metagame;

import java.util.ArrayList;
import java.util.List;

public abstract class GameRoom implements IRoom {

  public abstract Long getId();

  public abstract void Construct(Long id, String host);

  public GameRoom() {
  }

  public abstract void startGame();

  protected List<String> gameLog = new ArrayList<String>();

  /**
   * @return the gameLog
   */
  public List<String> getGameLog() {
    return gameLog;
  }

  public abstract String getWinner();

}
