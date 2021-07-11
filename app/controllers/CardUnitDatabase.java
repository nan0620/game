package controllers;

import java.util.ArrayList;

import structures.basic.Card;
import structures.basic.CardAttribute;
import structures.basic.Unit;
import structures.basic.UnitAttribute;

/*
 * this class is used to stored all kind of card and unit
 * 
 */
public class CardUnitDatabase {
	private ArrayList<CardUnitController> database=new ArrayList<CardUnitController>();
	
	public boolean addCard(CardUnitController entity) {
		if(entity!=null) {
			database.add(entity);
			return true;
		}else return false;
		
	}

/*	public Card getCardByUnitID(int id) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id)return t.buildCard();
		}
		return null;
	}*/
	
	public Unit getUnitByCardID(int id) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id)return t.buildUnit();
		}
		return null;
	}

	public Card getCardByCardConf(String conf) {
		for(CardUnitController t: database) {
			if(t.getCardConf().equals(conf))return t.buildCard();
		}
		return null;
	}
	
	public CardAttribute getAtrributeByCardID(int id) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id)return t.getAttribute();
		}
		return null;
	}
	
	public boolean cardContainProperty(int id, Unit.Property p) {
		for(CardUnitController t: database) {
			if(t.getCardID()==id) {
				return ((UnitAttribute)t.getAttribute()).getProperty().contains(p);
			}
		}
		return false;
	}
}
