package structures.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class UnitAttribute extends CardAttribute {
	private int hp;
	private int atk;
	private String unitConf;
	private int atkNum;
	private int atkRange;
	private ArrayList<Unit.Property> property;
	
	
	public UnitAttribute(int atk, int hp,int atkNum,int atkRange, String conf, ActionEvent[] actionEvent,Unit.Property[] property) {
		super();
		this.hp = hp;
		this.atk = atk;
		this.atkNum=atkNum;
		this.atkRange=atkRange;
		this.events=actionEvent;
		this.unitConf=conf;
		List<Unit.Property> list=Arrays.asList(property);
		this.property=new ArrayList<>(list);
	}
	public int getAtkNum() {
		return atkNum;
	}
	public void setAtkNum(int atkNum) {
		this.atkNum = atkNum;
	}
	public int getAtkRange() {
		return atkRange;
	}
	public void setAtkRange(int atkRange) {
		this.atkRange = atkRange;
	}
	public String getUnitConf() {
		return unitConf;
	}
	public int getHp() {
		return hp;
	}
	
	public int getAtk() {
		return atk;
	}
	public ArrayList<Unit.Property> getProperty() {
		return property;
	}
	
	
	

	
	
}
