package org.irenical.booty;

import org.irenical.lifecycle.LifeCycle;

public class SlowLifecycle implements LifeCycle {
  
  private boolean walking = false;

  @Override
  public void start() throws InterruptedException {
    Thread.sleep(1000);
    walking = true;
  }

  @Override
  public void stop() throws InterruptedException {
    Thread.sleep(1000);
    walking = false;
  }

  @Override
  public <ERROR extends Exception> boolean isRunning() throws ERROR {
    return walking;
  }

}
