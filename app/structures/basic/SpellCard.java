package structures.basic;

public class SpellCard extends Card {
	public static enum Target{
		All,Enemy,Friend,EnemyUnit,EnemyPlayer,FriendUnit,FriendPlayer,Unit,Player
	}
	private Target target;
	
	SpellCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard){
		super(id, cardname,manacost,miniCard,bigCard);
	}
	SpellCard(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard, Target target){
		super(id, cardname,manacost,miniCard,bigCard);
		this.target=target;
	}
	public SpellCard(Card card){//copy data from a card
		this.id=card.id;
		this.cardID=card.cardID;
		this.bigCard=card.bigCard;
		this.cardname=card.cardname;
		this.manacost=card.manacost;
		this.miniCard=card.miniCard;
	
		//this.type=Card.Type.Spell;
	}
	
	public void setTarget(Target target){
		this.target=target;
	}
	
	public Target getTarget(){
		return target;
	}
	
}
