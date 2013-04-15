package org.domain.bs;

import java.util.ArrayList;
import java.util.List;

import org.domain.bs.metagame.GameRoom;
import org.domain.bs.metagame.IRoom;

public class User {

  private String username;
  private GameRoom inGame = null;// your location. null== home

  private List<IRoom> rooms = new ArrayList<IRoom>();

  public void joinRoom(IRoom room) {
    rooms.add(room);
    room.enter(username);
  }

  public GameRoom getInGame() {
    return inGame;
  }

  public boolean setInGame(GameRoom gr) {
    if (rooms.contains(gr)) {
      inGame = gr;
      return true;
    }
    return false;

  }
  public void leaveRoom(IRoom room) {
    room.leave(username);
    rooms.remove(room);
    if (room == inGame) {
      inGame = null;
    }
  }

  public void leaveAllRooms() {
    List<IRoom> roomstoLeave = new ArrayList<IRoom>();
    for (IRoom room : rooms) {
      roomstoLeave.add(room);

    }
    for (IRoom room : roomstoLeave) {
      leaveRoom(room);
    }
  }

  public List<IRoom> getRooms() {
    return new ArrayList<IRoom>(rooms);
  }
  public User(String name) {
    username = name;
  }


  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

}
