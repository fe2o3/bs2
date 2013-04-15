package org.domain.bs.session;

import org.domain.bs.User;
import org.domain.bs.application.MasterUserList;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("userBean")
@Scope(ScopeType.SESSION)
public class UserBean {


  @In(create = true)
  private MasterUserList masterUserList;


  private User user;

  public User getUser() {
    return user;
  }

  public void login(User u) {
    user = u;
    masterUserList.addUser(u.getUsername(), u);
  }

  @Destroy
  public void logout() {
    user.leaveAllRooms();
    masterUserList.removeUser(user.getUsername());
  }



}
