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

In order to be able to create actors and send message from the graphical user interface, it is necessary to to register their constructors using the logger. <br/>
Two methods are provided to this end. <br/>
The method logActorType(<type_name>, <constuctor>) is used to register an actor class. This class must extends akka.actor.Actor.

```
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create();
    AkkaVisualLogger log = AkkaVisualLogger.create(system);
    log.logActorType("Sender", Sender::props);
    // ...
  }
  
  public class Sender extends AbstractActor {
    private final AkkaVisualLogger log = AkkaVisualLogger.getInstance();
    // ...
    public static Props props() {
      return Props.create(Sender.class, () -> new Sender());
    }
    public Sender() {
      log.logActorCreated(this);
    }
    // ...
  }
```
The method logMessageType(<type_name>, <constructo>) is used to register a message class.
  
```
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create();
    AkkaVisualLogger log = AkkaVisualLogger.create(system);
    log.logMessageType("query", () -> new Query(/* ... */));
    // ...
  }
```

# GUI-side walkthrough
The graphical interface is composed of two panels. The left panel (menu pane) and the right panel (simulation pane - where the actors, channels and messages are displayed). <br/>
The left panel is divided in three tabs:
- Actors: the actors states are displayed here;
- Actor Types: the registered actors class are displayed;
- Messages: the registered messages class are displayed here.
<br/> 
The program has two modes: simulation mode and replay mode. The replay mode offer the possibility to navigate step by step through the registered events.

## Simulation mode shorcuts
There are several shortcuts that are specific to simulation mode. They will not work once replay mode is entered.
- To create an actor, drag and drop an entry from the Actor Types tab to the simulation pane.
- To create a communication channel, select a source actor with <left_click> and select a target actor with <ctr+left_click>.
- To send a message, select a message type from the Messages pane, select a source actor with <left_click> and select a target actor with <alt+left_click>.
- To delete an item, select it with <left_click> and press <suppr>.
<br/>

## Replay mode shortcuts
There are several shortcuts that are specific to replay mode. The use of any one of them these will make the program enter replay mode.
- To go to the first event in the event history, press <back_space>.
- To go to the previous event, press <q>.
- To go to the previous event, press <d>. 
<br/>
Warning : Once in replay mode, there is no exiting it.
