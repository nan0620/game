package structures.basic;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import commands.BasicCommands;

/**
 * This is a representation of a Unit on the game board.
 * A unit has a unique id (this is used by the front-end.
 * Each unit has a current UnitAnimationType, e.g. move,
 * or attack. The position is the physical position on the
 * board. UnitAnimationSet contains the underlying information
 * about the animation frames, while ImageCorrection has
 * information for centering the unit on the tile. 
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Unit {

	public static enum Property{
		Provoke,Flying,AirDrop,SpellThief
	}
	
	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java objects from a file
	
	int id;
	int attack;
	int maxHealth;
	int health;
	int unitID;
	int maxMovingPoint=2;
	int movingPoint=2;
	int maxAtkNum=1;
	int atkNum=1;
	int atkRange=1;
	
	private ActionEvent[] events;
	private ArrayList<Property> property; 

	
	/*public ArrayList<ActionEvent> getEvents() {
		return events;
	}*/
	
	public ActionEvent getEvent(Class<? extends ActionEvent> type) {
		if(events==null)return null;
		for(ActionEvent event:events) {
			if(type.isInstance(event))return event;
		}
		return null;
	}

	public void setEvents(ActionEvent[] events) {
		this.events = events;
		for(ActionEvent event:events) {
			event.setTrigger(this);
		}
	}

	private boolean provoked=false;
	
	public boolean isProvoked() {
		return provoked;
	}

	public void setProvoked(boolean provoked) {
		this.provoked = provoked;
	}

	
	public boolean containProperty(Property p) {
		if(property==null)return false;
		return property.contains(p);
	}

	public void setProperty(ArrayList<Property> property) {
		this.property = property;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getMaxMovingPoint() {
		return maxMovingPoint;
	}

	public void setMaxMovingPoint(int maxMovingPoint) {
		this.maxMovingPoint = maxMovingPoint;
	}

	public int getMaxAtkNum() {
		return maxAtkNum;
	}

	public void setMaxAtkNum(int maxAtkNum) {
		this.maxAtkNum = maxAtkNum;
	}

	public void resetAction() {
		atkNum=maxAtkNum;
		movingPoint=maxMovingPoint;
		
	}
	
	public int getAtkRange() {
		return atkRange;
	}

	public void setAtkRange(int atkRange) {
		this.atkRange = atkRange;
	}

	public int getAtkNum() {
		return atkNum;
	}

	public void setAtkNum(int atkNum) {
		this.atkNum = atkNum;
	}

	public int getHP() {
		return health;
	}
	
	public int getAttack() {
		return attack;
	}

	public boolean HasMoved() {
		return movingPoint<=0;
	}

	public void setMovingPoint(int point) {
		
		this.movingPoint=point;
	}
	
	public int getMovingPoint() {
		return movingPoint;
	}
	
	public void attack(Unit target) {
		target.getDemage(this.attack);
	}
	
	public boolean isDead() {
		return this.health<=0;
	}

	UnitAnimationType animation;
	Position position;
	UnitAnimationSet animations;
	ImageCorrection correction;
	
	public void setHeath(int hp) {
		this.health=hp;
	}
	
	public int getUnitID() {
		return unitID;
	}
	
	public void setUnitID(int id) {
		unitID=id;
	}
	
	public void getHealing(int hp) {
		this.health+=hp;
		if(this.health>this.maxHealth)this.health=this.maxHealth;
	}
	public boolean getDemage(int hp) {
		this.health-=hp;
		if(hp<=0)return true;
		else return false;
	}
	public void setAttack(int atk) {
		this.attack=atk;
	}
	
	
	public Unit() {}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(0,0,0,0);
		this.correction = correction;
		this.animations = animations;
	}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;
		
		position = new Position(currentTile.getXpos(),currentTile.getYpos(),currentTile.getTilex(),currentTile.getTiley());
		this.correction = correction;
		this.animations = animations;
	}
	
	
	
	public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
			ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = animation;
		this.position = position;
		this.animations = animations;
		this.correction = correction;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UnitAnimationType getAnimation() {
		return animation;
	}
	public void setAnimation(UnitAnimationType animation) {
		this.animation = animation;
	}

	public ImageCorrection getCorrection() {
		return correction;
	}

	public void setCorrection(ImageCorrection correction) {
		this.correction = correction;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public UnitAnimationSet getAnimations() {
		return animations;
	}

	public void setAnimations(UnitAnimationSet animations) {
		this.animations = animations;
	}
	
	/**
	 * This command sets the position of the Unit to a specified
	 * tile.
	 * @param tile
	 */
	@JsonIgnore
	public void setPositionByTile(Tile tile) {
		position = new Position(tile.getXpos(),tile.getYpos(),tile.getTilex(),tile.getTiley());
	}

	
	
	
}
