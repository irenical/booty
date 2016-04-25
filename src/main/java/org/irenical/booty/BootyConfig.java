package org.irenical.booty;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.irenical.lifecycle.LifeCycle;

public class BootyConfig {

  private Supplier<List<LifeCycle>> lifecycleSupplier;

  private Consumer<Exception> onError;

  private boolean shutdownHook = true;

  public Supplier<List<LifeCycle>> getLifecycleSupplier() {
    return lifecycleSupplier;
  }

  /**
   * @param lifecycleSupplier
   *          a function that returns all the lifecycles that make up the
   *          application. This supplier will be called only once. Default value
   *          is null.
   */
  public void setLifecycleSupplier(Supplier<List<LifeCycle>> lifecycleSupplier) {
    this.lifecycleSupplier = lifecycleSupplier;
  }

  public Consumer<Exception> getOnError() {
    return onError;
  }

  /**
   * 
   * @param onError
   *          a function to be called if an error occurs while booting the
   *          lifecycles. The default value is a function that logs the error
   *          using SLF4J.
   */
  public void setOnError(Consumer<Exception> onError) {
    this.onError = onError;
  }

  /**
   * 
   * @param shutdownHook
   *          Whether or not the application should listen for JVM shutdown. If
   *          so, a shutdown will wait for all the lifecycles to stop in order
   *          to continue. The default value is true.
   */
  public void setShutdownHook(boolean shutdownHook) {
    this.shutdownHook = shutdownHook;
  }

  public boolean isShutdownHook() {
    return shutdownHook;
  }

}
