package org.irenical.booty;

import java.util.List;
import java.util.function.Consumer;

import org.irenical.lifecycle.LifeCycle;
import org.irenical.lifecycle.builder.CompositeLifeCycle;
import org.slf4j.LoggerFactory;

public class Booty {

  /**
   * Bootstraps application and given configs. If an error occurs during
   * bootstrap, the Exception will be logged, the lifecycles stopped in the
   * reversed order and the onError consumer is called. This method blocks the
   * current thread until all lifecycle's start() methods are called.
   * 
   * @param bootyConfig
   *          the application bootstrap configuration
   * @return a running composite lifecycle, grouping all lifecycles that make up
   *         the application
   */
  public static LifeCycle boot(BootyConfig bootyConfig) {
    assertConfig(bootyConfig);
    List<LifeCycle> lifecycles = bootyConfig.getLifecycleSupplier().get();
    if (lifecycles == null || lifecycles.isEmpty()) {
      throw new InvalidConfigurationException("No lifecycle was supplied");
    }
    CompositeLifeCycle baseLifecyle = new CompositeLifeCycle();
    try {
      lifecycles.forEach(baseLifecyle::append);

      if (bootyConfig.isShutdownHook()) {
        baseLifecyle.withShutdownHook();
      }
      baseLifecyle.start();

    } catch (Exception e) {
      try {
        baseLifecyle.stop();
      } catch (Exception stopE) {
        LoggerFactory.getLogger(Booty.class).error("Error reverting bootstrap... ignoring.", stopE);
      }
      Consumer<Exception> onError = bootyConfig.getOnError();
      if (onError != null) {
        onError.accept(e);
      }
    }
    return baseLifecyle;
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
