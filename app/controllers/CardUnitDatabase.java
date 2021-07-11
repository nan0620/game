//author: 2499878
//function: this class is for storing all kinds of unit cards and spell cards
package controllers;

import java.util.ArrayList;

import structures.basic.Card;
import structures.basic.CardAttribute;
import structures.basic.Unit;
import structures.basic.UnitAttribute;


public class CardUnitDatabase {
	private ArrayList<CardUnitController> database=new ArrayList<CardUnitController>();
	
	public boolean addCard(CardUnitController entity) {
		if(entity!=null) {
			database.add(entity);
			return true;
		}else return false;
		
	}

	// get the card by its id
	public Unit getUnitByCardID(int id) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id)return t.buildUnit();
		}
		return null;
	}

	// get the card by its configure
	public Card getCardByCardConf(String conf) {
		for(CardUnitController t: database) {
			if(t.getCardConf().equals(conf))return t.buildCard();
		}
		return null;
	}

	// get the card's attribute by its id
	public CardAttribute getAtrributeByCardID(int id) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id)return t.getAttribute();
		}
		return null;
	}

	// get the card's special ability, like Provoke, Flying, AirDrop, SpellThief
	public boolean cardContainProperty(int id, Unit.Property p) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id) {
				return ((UnitAttribute)t.getAttribute()).getProperty().contains(p);
			}
		}
		return false;
	}
}
