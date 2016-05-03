package org.irenical.booty;

import java.util.List;

import org.irenical.lifecycle.LifeCycle;
import org.irenical.lifecycle.builder.CompositeLifeCycle;
import org.slf4j.LoggerFactory;

public class Booty extends CompositeLifeCycle {

  private BootyConfig config;

  private Booty() {
  }

  /**
   * Creates an application ready to start with given configs. If an error
   * occurs during bootstrap, the Exception will be logged, the lifecycles
   * stopped in the reversed order and the onError consumer is called. The
   * returned instance's start() method blocks the current thread until all
   * lifecycle's start() methods are called.
   * 
   * @param bootyConfig
   *          the application bootstrap configuration
   * @return a running composite lifecycle, grouping all lifecycles that make up
   *         the application
   */
  public static LifeCycle build(BootyConfig bootyConfig) {
    assertConfig(bootyConfig);
    List<LifeCycle> lifecycles = bootyConfig.getLifecycleSupplier().get();
    if (lifecycles == null || lifecycles.isEmpty()) {
      throw new InvalidConfigurationException("No lifecycle was supplied");
    }
    Booty booty = new Booty();
    booty.config = bootyConfig;
    lifecycles.forEach(booty::append);
    if (bootyConfig.isShutdownHook()) {
      booty.withShutdownHook();
    }
    return booty;
  }

  @Override
  public synchronized <ERROR extends Exception> void start() throws ERROR {
    try {
      super.start();
    } catch (Exception e) {
      super.stop();
      config.getOnError().accept(e);
    }
  }

  private static void assertConfig(BootyConfig got) {
    if (got == null) {
      throw new InvalidConfigurationException("BootyConfig cannot be null");
    }
    if (got.getLifecycleSupplier() == null) {
      throw new InvalidConfigurationException("Lifecycle supplier cannot be null");
    }
    if (got.getOnError() == null) {
      got.setOnError(e -> LoggerFactory.getLogger(Booty.class).error("Error bootstrapping application", e));
    }
  }

}
