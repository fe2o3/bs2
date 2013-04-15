package org.domain.bs.bean;

import java.util.ArrayList;
import java.util.List;

import org.domain.bs.application.MasterUserList;
import org.domain.bs.bscardgame.BSCardGameRoom;
import org.domain.bs.metagame.GameRoom;
import org.domain.bs.metagame.IRoom;
import org.domain.bs.metagame.Lobby;
import org.domain.bs.service.LobbyService;
import org.domain.bs.session.UserBean;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("homeBean")
// @Scope(ScopeType.SESSION)
public class HomeBean {

  private List<String> selectedUsers = new ArrayList<String>();
  @In(create = true)
  private LobbyService lobbyService;

  /**
   * @return the selectedUsers
   */
  public List<String> getSelectedUsers() {
    return selectedUsers;
  }

  /**
   * @param selectedUsers
   *          the selectedUsers to set
   */
  public void setSelectedUsers(List<String> selectedUsers) {
    this.selectedUsers = selectedUsers;
  }


  @In(create = true)
  private MasterUserList masterUserList;
  @In(create = true)
  private UserBean userBean;


  public List<String> getAvailableUsers() {

    List<String> r = new ArrayList<String>();
    for (String s : masterUserList.getUsernames()) {
      if (!s.equals(userBean.getUser().getUsername()) && masterUserList.getUser(s).getInGame() == null)
        r.add(s);
    }

    return r;

  }

  public void inviteToBS() {
    if (selectedUsers.isEmpty()) {

      return;
    }
    lobbyService.makeLobbyAndInviteGuests(userBean.getUser().getUsername(), selectedUsers, BSCardGameRoom.class, null);

    selectedUsers = null;
  }

  public void setReady(Lobby lobby) {
    lobby.setReady(userBean.getUser().getUsername());
    // check if the lobby is all ready
    if (lobby.getReadyCount() == lobby.listMembers().size()) {
      lobbyService.makeRoomAndInvitePlayers(lobby);
    }
  }

  public List<GameRoom> getGames() {
    List<GameRoom> games = new ArrayList<GameRoom>();
    for (IRoom room : userBean.getUser().getRooms()) {
      if (!Lobby.LOBBY_ROOMTYPE.equals(room.roomType())) {
        games.add((GameRoom) room);
      }
    }
    return games;
  }
  public List<Lobby> getLobbies() {
    List<Lobby> lobbies = new ArrayList<Lobby>();
    for (IRoom room : userBean.getUser().getRooms()) {
      if (Lobby.LOBBY_ROOMTYPE.equals(room.roomType())) {
        lobbies.add((Lobby) room);
      }
    }
    return lobbies;
  }


  public String goToGame(GameRoom gr) {
    if (userBean.getUser().setInGame(gr)) {
      return "game";
    }
    return null;
  }

  public void quitGame(IRoom gr) {
    userBean.getUser().leaveRoom(gr);
  }

  // public boolean getOpponentsChanged() {
  // List<String> last = new ArrayList<String>();
  // for (int i = 0; i < _lastAvailableUsers.size(); i++) {
  // last.add(_lastAvailableUsers.get(i));
  // }
  // List<String> curr = getAvailableUsers();
  // return !curr.containsAll(last) || !last.containsAll(curr);
  // }
}
