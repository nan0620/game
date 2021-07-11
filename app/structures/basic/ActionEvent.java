package structures.basic;

import java.io.Serializable;

import akka.actor.ActorRef;

public abstract class ActionEvent{
	protected Object data;
	protected Unit trigger;
	
	public void setTrigger(Unit trigger) {
		this.trigger=trigger;
	}
	public abstract void eventTriggered(Object trigger, ActorRef out);
	
}
