package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(!gameState.playerTurn)return;
		if(gameState.pause)return;
		gameState.resetTile(out);
		gameState.highlightCard(-1, out);
		gameState.setState(GameState.State.Idle);
		gameState.resetUnit();
		gameState.endTurn(1, out);
		//gameState.playerDraw(out);
		
	}

}
