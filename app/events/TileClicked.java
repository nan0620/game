package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.GameState.State;
import structures.basic.Tile;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(!gameState.playerTurn)return;
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		Tile target=gameState.getTile(tilex, tiley);
		
		
		if(gameState.pause)return;
		
		if(gameState.getCurrentState()==GameState.State.Summon) {
			
			
			if(target.checkAllowSummon()) {
				target.setAllowSummon(false);
				gameState.summonUnit(out,tilex,tiley,1);
				
			}else {
				BasicCommands.addPlayer1Notification(out, "I cannot summon unit on there", 2);
				
				
			}
			gameState.resetTile(out);
			
			gameState.setState(GameState.State.Idle);
		}else if(gameState.getCurrentState()==GameState.State.Idle) {
			if(gameState.isUnitOnTile(tilex, tiley)>0) {
				if(!gameState.isUnitMoved(tilex, tiley)) {
					gameState.setState(GameState.State.MoveUnit);
					gameState.showMoveArea(out, tilex, tiley);
				}else if(!gameState.isUnitAttacked(tilex, tiley)) {
					gameState.setState(GameState.State.Attack);
					gameState.showAttackArea(out, tilex,tiley);
				}else {
					BasicCommands.addPlayer1Notification(out, "That unit cannot move", 2);
				}
			}
			
		}else if(gameState.getCurrentState()==GameState.State.MoveUnit||gameState.getCurrentState()==GameState.State.Attack) {
			if(gameState.tryToMoveUnit(tilex, tiley,out)) {
				
			}else {
				gameState.resetTile(out);
				gameState.setState(GameState.State.Idle);
			}
			
		}else if(gameState.getCurrentState()==GameState.State.Spell) {
			gameState.usingSpell(out, tilex, tiley,1);
		}
		
	}

}
