package structures.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import structures.basic.SpellCard.Target;

public class SpellAttribute extends CardAttribute {
	private SpellCard.Target target;
	private String effect;
	public SpellAttribute(Target target, String effect,ActionEvent[] actionEvent) {
		super();
		this.target = target;
		this.effect = effect;
		//List<ActionEvent> t=Arrays.asList(actionEvent);
		this.events=actionEvent;
	}
	
	public SpellCard.Target getTarget(){
		return target;
	}
	
	
	
	
	
}
