package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CommandDemo;

import structures.GameState;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		//CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
		
		Tile[][] tiles=new Tile[9][5];
		for(int i=0;i<9;i++) {//generate all tiles and show them on the front-end
			for(int j=0;j<5;j++) {
				Tile tile = BasicObjectBuilders.loadTile(i, j);
				tiles[i][j]=tile;
				BasicCommands.drawTile(out, tile, 0);
			}
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		gameState.setTile(tiles);
		
		
		
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, -1, Unit.class);//generate player's avatar
		unit.setPositionByTile(tiles[1][2]);
		gameState.addUnit(unit);
		gameState.addFriendUnit(unit);
		
		unit.setHeath(20);
		unit.setAttack(2);//set the attack and hp of the unit
		unit.setMaxHealth(20);
		unit.setAtkRange(1);
		BasicCommands.drawUnit(out, unit, tiles[1][2]);//set the position
		
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}//take a break to make sure that the front-end has generate the unit
		//if we don't wait, unit's attack and health cannot be update on the front-end
		
		BasicCommands.setUnitAttack(out, unit, 2);
		//try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, unit, 20);
		
		
		
		//generate ai avatar like human player do
		unit = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, -2, Unit.class);
		unit.setPositionByTile(tiles[7][2]);
		gameState.addUnit(unit);
		gameState.addEnemyUnit(unit);
		
		unit.setHeath(20);
		unit.setAttack(2);
		unit.setMaxHealth(20);
		BasicCommands.drawUnit(out, unit, tiles[7][2]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit, 2);
		
		BasicCommands.setUnitHealth(out, unit, 20);
		
		//gameState.summonUnit(out, 6, 3, 0);
		//gameState.summonUnit(out, 4, 3, 0);
		
		//Set the initial health and mana of both players
		BasicCommands.setPlayer1Health(out, gameState.getHumanPlayer());
		BasicCommands.setPlayer2Health(out, gameState.getHumanPlayer());
		BasicCommands.setPlayer1Mana(out, gameState.getHumanPlayer());
		
		
		
		
		Card[] playerDeck=new Card[20];
		
		for(int i=0;i<20;i++) {
			playerDeck[i]=gameState.getDatabase().getCardByCardConf(StaticConfFiles.playerDeck[i/2]);
			
		}
		Card[] aiDeck=new Card[20];
		for(int i=0;i<20;i++) {
			aiDeck[i]=gameState.getDatabase().getCardByCardConf(StaticConfFiles.aiDeck[i/2]);
		}
		
		gameState.setDeck(1, playerDeck);
		gameState.setDeck(0, aiDeck);
		
		
		
		for(int i=0;i<3;i++) {
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			gameState.playerDraw(out);
			gameState.aiDraw(out);
			
		}
		
		
		
		
		
		
		
	}

}


