package org.domain.bs.metagame;

import java.util.List;

public interface IRoom {

  public String roomType();

  public void enter(String username);

  public void leave(String username);

  public List<String> listMembers();
}
