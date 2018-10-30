# akka Visual Logger

This project is based on Akka. It offers event and actors state logging mechanism, a visualisation of these events and states, and allow the user to interact with akka using a GUI.

# Akka events

There are four kinds of events handled by the logger:
- actor creation / deletion;
- communication channel creation (purely graphical);
- message sending / receival.
- internal event

These events can be fired programatically or using the graphical user interface. <br/>
Each time an event is logged, the internal state of the actor generating it is stored (its public fields and their values).

# Java-side walkthrough

The class AkkaVisualLogger exports all the methods needed to interact with the GUI. <br/>
It is first initialized using:

```
  ActorSystem system = ActorSystem.create(); // create akka system
  AkkaVisualLogger.create(system); // create visual logger
```

And then referenced within actors using:

```
  AkkaVisualLogger log = AkkaVisualLogger.getInstance();
```

Actor creation and deletion is logged using the methods logActorCreated(<actor>) and logActorDeleted(<actor>).
  
```
  // invoked within a class extending akka.actor.Actor
  log.logActorCreated(this);
  log.logActorDeleted(this);
```
Warning : while the other logging methods use ActorRef to register events, this creation methods require a reference to the actor object itself. The reason for this is that it is necessary to have this reference to retrieve the actor state (its public fields and their values)

Message receival / sending is logged using the methods logMessageReceived(<message>, <source>, <target>) and logMessageSent(<message>, <source>, <target>).
  
```
  private void query(Query message) {
      log.logMessageReceived(message, getSender(), getSelf()); // log message reception before handling message
      
      // ...
      // handling message
      // ...
      
      // sending message to another actor
      Reply reply = new Reply(/* ... */);
      log.logMessageSent(reply, getSelf(), getSender()); // must be called before actually sending the message through akka
      getSender().tell(reply, getSelf());
  }
```
Is is also possible to display a communication channel between two actors using the methods logChannelCreated(<source>, <target>) and logChannelCreated(<source>, <target>). These methods are not sensitive to the source-target ordering (bidirectional channels).
  
```
  // channel creation
  private void hello(Hello message) {
    log.logChannelCreated(getSender(), getSelf());
  }
  
  // channel deletion
  private void goodbye(Goodbye message) {
    log.logChannelDeleted(getSender(), getSelf());
  }
```
Finally it is possible to fire an internal event (store actor state after a computation).

```
  private void message(Message m) {
    log.logMessageReceived(message, getSender(), getSelf());
    // ...
    // internal computation
    // ...
    log.logInternalEvent(getSelf());
  }
```

# GUI-side walkthrough
