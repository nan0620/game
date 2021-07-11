package structures.basic;

import akka.actor.ActorRef;
import structures.GameState;

public abstract class UnitDyingAction extends ActionEvent {

	@Override
	public void eventTriggered(Object trigger, ActorRef out) {
		// TODO Auto-generated method stub

	}
	
	public abstract void onUnitDying(ActorRef out, GameState gameState,Unit unit);
	

}
