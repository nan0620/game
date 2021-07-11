package structures.basic;

import akka.actor.ActorRef;

public abstract class CardUsedEvent extends ActionEvent {

	@Override
	public abstract void eventTriggered(Object trigger, ActorRef out);

}
