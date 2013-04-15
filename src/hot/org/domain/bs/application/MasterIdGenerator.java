package org.domain.bs.application;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("masterIdGenerator")
@Scope(ScopeType.APPLICATION)
public class MasterIdGenerator {

  private Long id = 0L;

  public Long getNextId() {
    return id++;
  }
}
