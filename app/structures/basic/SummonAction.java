package structures.basic;

import akka.actor.ActorRef;
import structures.GameState;

public abstract class SummonAction extends ActionEvent {

	@Override
	public void eventTriggered(Object trigger, ActorRef out) {
		// TODO Auto-generated method stub

	}
	
	public abstract void onSummon(ActorRef out,GameState gameState, Player summoner,Unit unit, Unit summonerAvatar);
}
