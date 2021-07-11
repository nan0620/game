//author: 2499878
//function: this class is used to control the cards, like shuffle, draw, get, contain, remove card
package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class CardController {
    private Card summonCard;

    public Card getSummonCard() {
        return summonCard;
    }

    public void setSummonCard(Card target) {
        this.summonCard = target;
    }

    private ArrayList<Card> deck;//contain all the cards

    private Card[] hand = new Card[7];//the maximum number of cards in hand
    private int handNumber = 0;//the initial number of cards in hand

    //function: set the deck of cards
    public void setDeck(Card[] deck) {
        List<Card> t = Arrays.asList(deck);
        this.deck = new ArrayList<Card>(t);
    }

    //function: shuffle the card randomly
    public void shuffleDeck() {
        java.util.Collections.shuffle(deck);
    }

    //function: draw card randomly
    //At the start of the game I should be able to draw three cards, and then
    //draw an extra card at the end of each turn.
    public Card DrawCard() {
        if (deck.isEmpty()) {
            return null;
        }
        hand[handNumber] = deck.get(0);
        deck.remove(0);
        return hand[handNumber++];
    }

    //function: after getting a card, the cards' number in hand will +1
    public void getCard(Card card) {
        hand[handNumber++] = card;
    }

    // about the card's information and name
    public Card containCard(String cardName) {
        for (int i = 0; i < handNumber; i++) {
            if (hand[i].getCardname().equals(cardName)) return hand[i];
        }

        return null;
    }

    // function: after drawing a card, the number of cards in hand will -1, and the total cards' number will -1
    public int removeCard(Card card, ActorRef out) {
        for (int i = 0; i < handNumber; i++) {
            if (hand[i] == card) {
                BasicCommands.deleteCard(out, handNumber);


                for (int j = i; j < handNumber - 1; j++) {
                    hand[j] = hand[j + 1];
                }
                handNumber--;

                return i;
            }
            ;

        }
        return -1;
    }

    public int removeCard(Card card) {
        for (int i = 0; i < handNumber; i++) {
            if (hand[i] == card) {


                for (int j = i; j < handNumber - 1; j++) {
                    hand[j] = hand[j + 1];
                }
                handNumber--;

                return i;
            }
            ;

        }
        return -1;
    }

    public boolean removeCard(int pos) {
        if (pos < 0 || pos > 6) return false;

        for (int i = pos; i < handNumber - 1; i++) {
            hand[i] = hand[i + 1];
        }
        handNumber--;

        return true;
    }


    public boolean deckEmpty() {
        return deck.isEmpty();
    }

    public boolean handFull() {
        return handNumber == 7;
    }

    public int getHandNumber() {
        return handNumber;
    }

    //pos:positon of the card we want to highlight. -1 means reset all card
    public Card highlightCard(int pos, ActorRef out) {

        for (int i = 0; i < handNumber; i++) {
            Card t = hand[i];
            BasicCommands.drawCard(out, t, i + 1, 0);
            System.out.println();
        }
        if (pos > 5) return null;
        if (pos >= 0) {
            BasicCommands.drawCard(out, hand[pos], pos + 1, 1);
            return hand[pos];
        }

        return null;
    }

    public Card[] getHands() {
        return hand;
    }

}
