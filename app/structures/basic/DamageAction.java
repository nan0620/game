package structures.basic;

import akka.actor.ActorRef;
import structures.GameState;

public abstract class DamageAction extends ActionEvent {

	@Override
	public void eventTriggered(Object trigger, ActorRef out) {
		// TODO Auto-generated method stub

	}
	
	
	
	public abstract void damageDealt(ActorRef out,GameState state,Unit attacker,Unit target,Unit trigger); 

}
