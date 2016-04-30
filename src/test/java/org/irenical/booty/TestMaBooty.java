package org.irenical.booty;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.irenical.lifecycle.LifeCycle;
import org.junit.Assert;
import org.junit.Test;

public class TestMaBooty {
  
  @Test(expected=InvalidConfigurationException.class)
  public void nullConfigTest(){
    Booty.build(null);
  }
  
  @Test(expected=InvalidConfigurationException.class)
  public void nullSupplierTest(){
    Booty.build(new BootyConfig());
  }
  
  @Test(expected=InvalidConfigurationException.class)
  public void emptyNullLifeCyclesTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->null);
    Booty.build(config).start();
  }
  
  @Test(expected=InvalidConfigurationException.class)
  public void emptyLifeCyclesTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.emptyList());
    Booty.build(config).start();
  }
  
  @Test
  public void trivialTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.singletonList(new TrivialLifecycle()));
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertTrue(app.isRunning());
  }
  
  @Test
  public void shutdownHook(){
    BootyConfig config = new BootyConfig();
    config.setShutdownHook(false);
    config.setLifecycleSupplier(()->Collections.singletonList(new TrivialLifecycle()));
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertTrue(app.isRunning());
    Assert.assertFalse(config.isShutdownHook());
  }
  
  @Test
  public void singleSupplierCallTest(){
    List<LifeCycle> createdLifecycles = new LinkedList<>();
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->{
      LifeCycle lc = new TrivialLifecycle();
      createdLifecycles.add(lc);
      return Collections.singletonList(lc);
    });
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertEquals(createdLifecycles.size(), 1);
    Assert.assertTrue(app.isRunning());
  }
  
  @Test
  public void slowLifecycleTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.singletonList(new SlowLifecycle()));
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertTrue(app.isRunning());
    app.stop();
    Assert.assertFalse(app.isRunning());
  }
  
  @Test
  public void brokenLifecycleTest(){
    BootyConfig config = new BootyConfig();
    LifeCycle lc = new BrokenLifecycle();
    config.setLifecycleSupplier(()->Collections.singletonList(lc));
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertFalse(lc.isRunning());
    Assert.assertFalse(app.isRunning());
  }
  
  @Test
  public void errorHandleTest(){
    BootyConfig config = new BootyConfig();
    config.setOnError(e->Assert.assertEquals(e.getMessage(), "I blew up"));
    LifeCycle lc = new BrokenLifecycle();
    config.setLifecycleSupplier(()->Collections.singletonList(lc));
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertFalse(lc.isRunning());
    Assert.assertFalse(app.isRunning());
  }
  
  @Test
  public void rollbackTest(){
    TrivialLifecycle lc = new TrivialLifecycle();
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Arrays.asList(lc, new BrokenLifecycle()));
    LifeCycle app = Booty.build(config);
    app.start();
    Assert.assertFalse(app.isRunning());
    Assert.assertFalse(lc.isRunning());
  }

}
