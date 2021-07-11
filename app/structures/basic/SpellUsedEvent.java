package structures.basic;

import akka.actor.ActorRef;
import structures.GameState;

public abstract class SpellUsedEvent extends ActionEvent {

	@Override
	public void eventTriggered(Object trigger, ActorRef out) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void usingSpell(Unit target,GameState gameState,ActorRef out);

}
