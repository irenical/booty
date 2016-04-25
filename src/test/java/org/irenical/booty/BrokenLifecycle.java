package org.irenical.booty;

import org.irenical.lifecycle.LifeCycle;

public class BrokenLifecycle implements LifeCycle {

  @Override
  public void start() throws Exception {
    throw new Exception("I blew up");
  }

  @Override
  public void stop() throws Exception {
    throw new Exception("I blew up even more");
  }

  @Override
  public <ERROR extends Exception> boolean isRunning() throws ERROR {
    return false;
  }

}
