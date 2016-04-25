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
    Booty.boot(null);
  }
  
  @Test(expected=InvalidConfigurationException.class)
  public void nullSupplierTest(){
    Booty.boot(new BootyConfig());
  }
  
  @Test(expected=InvalidConfigurationException.class)
  public void emptySupplierTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.emptyList());
    Booty.boot(config);
  }
  
  @Test
  public void trivialTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.singletonList(new TrivialLifecycle()));
    LifeCycle app = Booty.boot(config);
    Assert.assertTrue(app.isRunning());
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
    LifeCycle app = Booty.boot(config);
    Assert.assertEquals(createdLifecycles.size(), 1);
    Assert.assertTrue(app.isRunning());
  }
  
  @Test
  public void slowLifecycleTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.singletonList(new SlowLifecycle()));
    LifeCycle app = Booty.boot(config);
    Assert.assertTrue(app.isRunning());
  }
  
  @Test
  public void brokenLifecycleTest(){
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Collections.singletonList(new BrokenLifecycle()));
    LifeCycle app = Booty.boot(config);
    Assert.assertFalse(app.isRunning());
  }
  
  
  
  @Test
  public void rollbackTest(){
    TrivialLifecycle lc = new TrivialLifecycle();
    BootyConfig config = new BootyConfig();
    config.setLifecycleSupplier(()->Arrays.asList(lc, new BrokenLifecycle()));
    LifeCycle app = Booty.boot(config);
    Assert.assertFalse(app.isRunning());
    Assert.assertFalse(lc.isRunning());
  }

}
