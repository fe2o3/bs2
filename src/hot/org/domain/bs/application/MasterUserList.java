package org.domain.bs.application;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.domain.bs.User;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("masterUserList")
@Scope(ScopeType.APPLICATION)
public class MasterUserList {

  private Map<String, User> users = new ConcurrentHashMap<String, User>();
  private final Object lock = new Object();

  public Set<String> getUsernames() {
    return users.keySet();
  }

  public User getUser(String username) {
    return users.get(username);
  }

  public boolean userExists(String username) {
    return users.containsKey(username);
  }

  public void addUser(String username, User u) {
    synchronized (lock) {
      users.put(username, u);
    }
  }

  public void removeUser(String username) {
    synchronized (lock) {
      users.remove(username);
    }
  }

}
