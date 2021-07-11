//author: 2499878
//function: there are two different cards, unitcard and spellcard
//here to set the two kinds of cards' attributions and bind a unit or spell effect to a card
package controllers;

import structures.basic.Card;
import structures.basic.CardAttribute;
import structures.basic.SpellAttribute;
import structures.basic.SpellCard;
import structures.basic.Unit;
import structures.basic.UnitAttribute;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


public class CardUnitController {
	private static int nextID=0;
	private static int nextCardID=0;
	private static int nextUnitID=0;
	private int cardID;
	private CardAttribute attribute;
	private String cardConf;

	//Used to create an object that binds the card to the unit according to the input setting file
	public static CardUnitController CardUnitBuilder(String cardConf) {
		int index=0;

		//check if it is a unit card
		for(String t:StaticConfFiles.unitCards) {
			if(t.equals(cardConf))break;
			index++;
		}

		//find the target
		if(index!=StaticConfFiles.unitCards.length){
			return new CardUnitController(nextCardID++,StaticConfFiles.unitCards[index],StaticConfFiles.unitsAttributes[index]);
		}
		
		index=0;

		//check if it is a spell card
		for(String t:StaticConfFiles.spellCards) {
			
			if(t.equals(cardConf))break;
			index++;
		}

		//find the target
		if(index!=StaticConfFiles.spellCards.length){
			CardUnitController t=new CardUnitController(nextCardID++,StaticConfFiles.spellCards[index],StaticConfFiles.spellAttribute[index]);
			//for debugging
			//System.out.println(StaticConfFiles.spellCards[index]+"\t:\t"+index);
			//System.out.println((t.getAttribute() instanceof SpellAttribute)+"==\t:\t=="+index);
			return t;
		}

		return null;
	}
	
	public CardUnitController(int cardID, String cardConf, CardAttribute attribute) {
		super();
		this.cardID = cardID;
		this.cardConf = cardConf;
		this.attribute=attribute;
		
}
//getter and setter
	public int getCardID() {
		return cardID;
	}
	public void setCardID(int cardID) {
		this.cardID = cardID;
	}
	
	
	public String getCardConf() {
		return cardConf;
	}
	public void setCardConf(String cardConf) {
		this.cardConf = cardConf;
	}
	
	
//getter and setter
	
	public CardAttribute getAttribute() {
		//System.out.print();
		return attribute;
	}
	
	public Card buildCard() {
		Card res=BasicObjectBuilders.loadCard(cardConf,nextID++,Card.class);
		res.setCardID(cardID);
		if(attribute instanceof SpellAttribute) {
			res=new SpellCard(res);
			((SpellCard)res).setTarget(((SpellAttribute)attribute).getTarget());
		}
		return res;
	}

	//successfully set a unit card or a spell card and bind its effect
	public Unit buildUnit() {
		UnitAttribute attribute=(UnitAttribute)this.attribute;
		Unit res=BasicObjectBuilders.loadUnit(attribute.getUnitConf(),nextUnitID++,Unit.class);
		//res.setUnitID(((UnitAttribute)attribute));
		
		res.setProperty(attribute.getProperty());
		res.setEvents(attribute.getEvents());
		res.setMaxAtkNum(attribute.getAtkNum());
		res.setAtkRange(attribute.getAtkRange());
		res.setAtkNum(attribute.getAtkNum());
		//for debugging
		//System.out.println(String.format("%s:\n{atknum:%d\natkrange:%d\n}", attribute.getUnitConf(),res.getAtkNum(),attribute.getAtkRange()));
		return res;
	}
	
	
	
	
}
