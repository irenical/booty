[![][maven img]][maven]
[![][travis img]][travis]
[![][codecov img]][codecov]
[![][codacy img]][codacy]


Booty leverages on the [LifeCycle](https://github.com/irenical/lifecycle) interface to define an application and handle it's bootstrap.

A common application is made up of several modules, each either developed by you, your teammate, an open community, some big coorporation, etc... These modules are things like logging, database access, TPC listening and also etc...

Although you can make a Java application as a monolithic block on top of the JREs, this is seldom the case. You can use Booty to help you with the handling of all the modules in your application, still keeping them separated.

## Prerequisites

Each of your application's modules must somehow implement LifeCycle. This is the minimum contract Booty needs in order to handle them. If you don't want to change your module dependencies, you can simply create anonymous LifeCycle classes that wrap them. Either way you end up having to implement the following methods for each of your modules: void start(), void stop(), boolean isRunning(). Chances are the behaviours themselves are already implemented in each module.

## Usage

```java
public static void main(String[] args) {
  LifeCycle loggingConfiguration = new MyLoggingConfiguration();
  LifeCycle httpServer = new MyHTTPServer();
  LifeCycle dao = MyDatabaseAccess();
  
  BootyConfig config = new BootyConfig();
  config.setLifecycleSupplier(()->Arrays.asList(loggingConfiguration,httpServer,dao));
  Booty.build(config).start();
}

```

## Notes

How the modules communicate with one another is outside of the scope of this project and should be solved by correcty modeling your application.  
It is also possible to use Booty in a servlet container. To do so, it should be called in the application Context Listener.

[maven]:http://search.maven.org/#search|gav|1|g:"org.irenical.booty"%20AND%20a:"booty"
[maven img]:https://maven-badges.herokuapp.com/maven-central/org.irenical.booty/booty/badge.svg

[travis]:https://travis-ci.org/irenical/booty
[travis img]:https://travis-ci.org/irenical/booty.svg?branch=master

[codecov]:https://codecov.io/gh/irenical/booty
[codecov img]:https://codecov.io/gh/irenical/booty/branch/master/graph/badge.svg

[codacy]:https://api.codacy.com/project/badge/Grade/04363fe4528d49668ccf72379890c09c
[codacy img]:https://www.codacy.com/app/tiagosimao/booty?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=irenical/booty&amp;utm_campaign=Badge_Grade
