package org.domain.bs.metagame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;
public class Lobby implements IRoom {

  private Map<String, MutableBoolean> usernamesReady = new HashMap<String, MutableBoolean>();

  private String hostUsername;
  private String message;
  private Class<? extends GameRoom> destination;

  public Lobby(String host, Class<? extends GameRoom> dest, String m) {
    hostUsername = host;
    destination = dest;
    message = m;
  }

  public int getReadyCount() {
    int tot = 0;
    Iterator<String> iter = usernamesReady.keySet().iterator();
    while(iter.hasNext()) {
      String username = iter.next();
      if (usernamesReady.get(username).booleanValue() == true) {
        tot++;
      }
    }

    return tot;
  }

  public void setReady(String user) {
    if (usernamesReady.containsKey(user)) {
      usernamesReady.get(user).setValue(true);
    }
  }

  public boolean getReady(String user) {
    if (usernamesReady.containsKey(user)) {
      return usernamesReady.get(user).booleanValue();
    }
    return false;
  }

  public String getHostUsername() {
    return hostUsername;
  }

  public String getMessage() {
    return message;
  }

  public Class<? extends GameRoom> getDestination() {
    return destination;
  }

  @Override
  public void leave(String username) {
    if (usernamesReady.containsKey(username)) {
      usernamesReady.remove(username);

    }

  }

  @Override
  public List<String> listMembers() {
    return new ArrayList<String>(usernamesReady.keySet());
  }

  @Override
  public void enter(String username) {
    if (!usernamesReady.containsKey(username)) {
      usernamesReady.put(username, new MutableBoolean(false));
    }

  }

  public static final String LOBBY_ROOMTYPE = "Lobby";

  @Override
  public String roomType() {
    return LOBBY_ROOMTYPE;
  }

}
