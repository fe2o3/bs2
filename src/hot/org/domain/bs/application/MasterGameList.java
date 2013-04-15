package org.domain.bs.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.domain.bs.metagame.GameRoom;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("masterGameList")
@Scope(ScopeType.APPLICATION)
public class MasterGameList {

  private Map<Long, GameRoom> listOfGames = new ConcurrentHashMap<Long, GameRoom>();



  synchronized public void addGameRoom(GameRoom g) {

    listOfGames.put(g.getId(), g);
  }

  public GameRoom getGameRoom(Long id) {
    return listOfGames.get(id);
  }

}
