package org.domain.bs.service;

import java.util.List;

import org.domain.bs.application.MasterGameList;
import org.domain.bs.application.MasterIdGenerator;
import org.domain.bs.application.MasterUserList;
import org.domain.bs.metagame.GameRoom;
import org.domain.bs.metagame.Lobby;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("lobbyService")
public class LobbyService {

  @In(create = true)
  private MasterUserList masterUserList;
  @In(create = true)
  private MasterIdGenerator masterIdGenerator;
  @In(create = true)
  private MasterGameList masterGameList;

  public void makeLobbyAndInviteGuests(String host, List<String> users, Class<? extends GameRoom> dest, String message) {
    Lobby lobby = new Lobby(host, dest, message);
    if (!users.contains(host)) {
      users.add(host);
    }
    for (String s : users) {
      masterUserList.getUser(s).joinRoom(lobby);
    }
  }

  public <T extends GameRoom> T makeRoomAndInvitePlayers(Lobby lobby) {
    Long id = masterIdGenerator.getNextId();
    
    Class<T> groomClazz = (Class<T>) lobby.getDestination();
    T gr = null;
    try {
      gr = (T) groomClazz.newInstance();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    gr.Construct(id, lobby.getHostUsername());

    for (String s : lobby.listMembers()) {
      masterUserList.getUser(s).joinRoom(gr);
    }
    gr.startGame();
    for (String s : lobby.listMembers()) {
      masterUserList.getUser(s).leaveRoom(lobby);
    }
    // masterGameList.addGameRoom(gr);// is this necessary?

    return gr;

  }

}
