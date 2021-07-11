package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.SpellCard;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * <p>
 * {
 * messageType = “cardClicked”
 * position = <hand index position [1-6]>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class CardClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
        if (!gameState.playerTurn) return;
        if (gameState.pause) return;

        int handPosition = message.get("position").asInt() - 1;
        Card target = gameState.highlightCard(handPosition, out);

        gameState.resetTile(out);

        if (target != null && gameState.isManaEnough(target, 1)) {
            gameState.setSummonUnit(target, 1);

            if (target instanceof SpellCard) {
                gameState.setState(GameState.State.Spell);
            } else {
                gameState.showSummonArea(out, 1);

            }
        } else {
            BasicCommands.addPlayer1Notification(out, "We don't have enough mana", 2);

        }

    }

}
