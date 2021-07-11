// author: 2564916J & 2499878H
// function: actual on-going operations in the whole game
package structures;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.util.Collections;
import commands.BasicCommands;
import controllers.CardController;
import controllers.CardUnitController;
import controllers.CardUnitDatabase;
import structures.basic.Card;
import structures.basic.DamageAction;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.SpellAttribute;
import structures.basic.SpellCard;
import structures.basic.SpellUsedEvent;
import structures.basic.SummonAction;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import structures.basic.UnitAttribute;
import structures.basic.UnitDyingAction;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


public class GameState {

    public boolean pause = false;
    public boolean playerTurn = true;

    //set the five kinds of operations
    public enum State {
        Summon, Attack, Idle, MoveUnit, Spell
    }

    private Tile[][] tiles;
    private Unit[][] unitArea;
    // initialize the two players' value of health and mana
    // the human player
    private Player player = new Player(20, 1);
    // the ai player
    private Player ai = new Player(20, 0);

    private State state = State.Idle;

    private ArrayList<Unit> unitList = new ArrayList<Unit>();
    private ArrayList<Unit> friendUnitList = new ArrayList<Unit>();
    private ArrayList<Unit> enemyUnitList = new ArrayList<Unit>();
    private CardController playerCard = new CardController();
    private CardController aiCard = new CardController();
    private CardUnitDatabase database = new CardUnitDatabase();

    private Unit movingUnit = null;

    public GameState() {
        // load unit card and its data to the database
        for (String t : StaticConfFiles.unitCards) {//load card and unit data into the database
            database.addCard(CardUnitController.CardUnitBuilder(t));
        }
        // load spell card and its data to the database
        for (String t : StaticConfFiles.spellCards) {
            database.addCard(CardUnitController.CardUnitBuilder(t));
        }
    }

//    public void getCard(String conf, ActorRef out) {
//        Card newCard = database.getCardByCardConf(conf);
//        playerCard.getCard(newCard);
//        if (newCard != null) {
//            BasicCommands.drawCard(out, newCard, playerCard.getHandNumber(), 0);
//        }
//    }
//
//    public void deleteUnit(Unit unit) {
//        if (unitList.contains(unit)) unitList.remove(unit);
//        if (friendUnitList.contains(unit)) friendUnitList.remove(unit);
//        if (enemyUnitList.contains(unit)) enemyUnitList.remove(unit);
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (unitArea[i][j] == unit) {
//                    unitArea[i][j] = null;
//                    return;
//                }
//            }
//        }
//    }

    // set each tile of the whole board, and the board is 5 tiles * 9 tiles
    public void setTile(Tile[][] tiles) {
        this.tiles = tiles;
        this.unitArea = new Unit[9][5];

    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public Tile getTile(Unit unit) {
        return tiles[unit.getPosition().getTilex()][unit.getPosition().getTiley()];
    }

    public void addUnit(Unit unit) {
        unitList.add(unit);
        int x = unit.getPosition().getTilex();
        int y = unit.getPosition().getTiley();
        unitArea[x][y] = unit;
    }

    // summon each player's cards
    public void addFriendUnit(Unit unit) {
        friendUnitList.add(unit);
    }
    public void addEnemyUnit(Unit unit) {
        enemyUnitList.add(unit);
    }


    public Player getHumanPlayer() {
        return player;
    }
    public Player getAIPlayer() {
        return ai;
    }

    //1 for player's deck,others for ai
    public void setDeck(int target, Card[] deck) {
        if (target == 1) {
            playerCard.setDeck(deck);
            playerCard.shuffleDeck();
        } else {
            aiCard.setDeck(deck);
            aiCard.shuffleDeck();
        }
    }

    // player's drawing cards operation
    public void playerDraw(ActorRef out) {
        pause = true;
        Card newCard = playerCard.DrawCard();
        if (newCard != null) {
            BasicCommands.drawCard(out, newCard, playerCard.getHandNumber(), 0);
            if (playerCard.handFull()) {
                BasicCommands.addPlayer1Notification(out, "My hand is too full!", 2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BasicCommands.deleteCard(out, 7);
                playerCard.removeCard(6);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            BasicCommands.addPlayer1Notification(out, "I cannot draw!", 2);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BasicCommands.addPlayer1Notification(out, "We are defeated!", 2);
        }
        pause = false;
    }

    // ai's drawing cards operation
    public void aiDraw(ActorRef out) {
        Card newCard = aiCard.DrawCard();
        if (newCard != null) {

            if (aiCard.handFull()) {
                BasicCommands.addPlayer1Notification(out, "Enemy's hand is too full", 2);
                aiCard.removeCard(7);
            }
        } else {
            BasicCommands.addPlayer1Notification(out, "Enemy cannot draw!", 2);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BasicCommands.addPlayer1Notification(out, "We defeat the enemy!", 2);
        }
    }

    // store the unit date
    public CardUnitDatabase getDatabase() {
        return database;
    }

    // click the card and have the highkight effect
    public Card highlightCard(int pos, ActorRef out) {
        return playerCard.highlightCard(pos, out);
    }

    // when you click a unit card or spell card in the deck
    // the board will show the white tiles that the card can put
    public void showSummonArea(ActorRef out, int team) {

        CardController player = team == 1 ? this.playerCard : this.aiCard;

        resetTile(out);

        ArrayList<Unit> unitList = team == 1 ? this.friendUnitList : this.enemyUnitList;
        // unit card contained airdrop property
        if (database.cardContainProperty(player.getSummonCard().getCardID(), Unit.Property.AirDrop)) {
            for (Tile[] tiles : this.tiles) {
                for (Tile tile : tiles) {
                    int x = tile.getTilex();
                    int y = tile.getTiley();
                    if (unitArea[x][y] == null) {
                        BasicCommands.drawTile(out, tile, 1);
                        tile.setAllowSummon(true);
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // limit the summon area to one steps
            int[][] summonArea = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

            for (Unit t : unitList) {
                int x = t.getPosition().getTilex();
                int y = t.getPosition().getTiley();
                for (int[] pos : summonArea) {
                    int newx = x + pos[0];
                    int newy = y + pos[1];
                    if (newx >= 0 && newy >= 0 && newx < 9 && newy < 5 && unitArea[newx][newy] == null) {
                        if (unitArea[newx][newy] == null) {
                            BasicCommands.drawTile(out, tiles[newx][newy], 1);
                            tiles[newx][newy].setAllowSummon(true);
                        }
                    }

                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        state = State.Summon;
    }

    public void setSummonUnit(Card target, int team) {
        CardController player = team == 1 ? this.playerCard : this.aiCard;
        player.setSummonCard(target);
    }

    public void resetTile(ActorRef out) {
        for (Tile[] t : tiles) {
            for (Tile i : t) {
                BasicCommands.drawTile(out, i, 0);
                i.setAllowSummon(false);
                i.setAttackable(false);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public State getCurrentState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isManaEnough(Card card, int team) {//1 for player, others for ai
        CardController cards = team == 1 ? playerCard : aiCard;
        Player player = team == 1 ? this.player : this.ai;


        return player.getMana() >= card.getManacost();
    }

    public void summonUnit(ActorRef out, int x, int y, int team) {//team:1 for player, others for ai

        CardController playercard = team == 1 ? playerCard : aiCard;
        Unit unit = database.getUnitByCardID(playercard.getSummonCard().getCardID());
        Player player = team == 1 ? this.player : this.ai;

        if (team == 1) playercard.removeCard(playercard.getSummonCard(), out);
        else playercard.removeCard(playercard.getSummonCard());

        if (team == 1) highlightCard(-1, out);

        spendMana(playercard.getSummonCard().getManacost(), player);

        if (team == 1) BasicCommands.setPlayer1Mana(out, player);
        else BasicCommands.setPlayer2Mana(out, player);

        unit.setPositionByTile(tiles[x][y]);
        addUnit(unit);

        if (playercard == playerCard) addFriendUnit(unit);
        else {
            addEnemyUnit(unit);
            System.out.println("add to enemy Unit");
        }


        BasicCommands.drawUnit(out, unit, tiles[x][y]);//set the position

        unitArea[x][y] = unit;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//take a break to make sure that the front-end has generate the unit
        //if we don't wait, unit's attack and health cannot be update on the front-end


        UnitAttribute attribute = (UnitAttribute) database.getAtrributeByCardID(playercard.getSummonCard().getCardID());
        BasicCommands.setUnitAttack(out, unit, attribute.getAtk());
        //try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
        BasicCommands.setUnitHealth(out, unit, attribute.getHp());
        unit.setHeath(attribute.getHp());
        unit.setMaxHealth(attribute.getHp());
        unit.setAttack(attribute.getAtk());
        //checkProvoked(unit);
        System.out.println("onSummon:" + unit.getEvent(SummonAction.class));
        SummonAction action = (SummonAction) unit.getEvent(SummonAction.class);
        if (action != null) {
            action.onSummon(out, this, player, unit, team == 1 ? friendUnitList.get(0) : enemyUnitList.get(0));
            System.out.println("1:" + action.getClass());
            System.out.println("2:" + (action instanceof SummonAction));
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public boolean isUnitMoved(int x, int y) {
        return unitArea[x][y].HasMoved();

    }

    public boolean isUnitAttacked(int x, int y) {
        return unitArea[x][y].getAtkNum() == 0;

    }

    public void showAttackArea(ActorRef out, int unitX, int unitY) {
        movingUnit = unitArea[unitX][unitY];
        int aR = movingUnit.getAtkRange();
        int[][] pos = new int[(aR * 2 + 1) * (aR * 2 + 1) - 1][2];

        for (int i = -aR, index = 0; i <= aR; i++) {
            for (int j = -aR; j <= aR; j++) {
                if (!(i == 0 && j == 0)) {
                    pos[index][0] = i;
                    pos[index][1] = j;
                    index++;
                }
            }
        }

        for (int[] i : pos) {
            int x = unitX + i[0];
            int y = unitY + i[1];
            if (x >= 0 && x < 9 && y >= 0 && y < 5) {
                if (unitArea[x][y] != null && enemyUnitList.contains(unitArea[x][y])) {
                    BasicCommands.drawTile(out, tiles[x][y], 2);
                    tiles[x][y].setAttackable(true);
                }
            }
        }
    }

    public void showMoveArea(ActorRef out, int unitX, int unitY) {

        resetTile(out);


        movingUnit = unitArea[unitX][unitY];


        int mP = movingUnit.getMovingPoint();
        if (movingUnit.containProperty(Unit.Property.Flying)) {
            mP = 12;
        }
        ArrayList<Unit> enemyUnitList = this.friendUnitList.contains(movingUnit) ? this.enemyUnitList : this.friendUnitList;

        int[][] pos = new int[((mP + 1) * mP) / 2 * 4][2];

        for (int i = -mP, index = 0; i <= mP; i++) {
            for (int j = -mP; j <= mP; j++) {
                if (Math.abs(i) + Math.abs(j) <= mP && !(i == 0 && j == 0)) {
                    pos[index][0] = i;
                    pos[index][1] = j;

                    index++;

                }
            }
        }

        for (int[] i : pos) {
            int x = unitX + i[0];
            int y = unitY + i[1];
            if (x >= 0 && x < 9 && y >= 0 && y < 5) {
                if (unitArea[x][y] == null) {
                    BasicCommands.drawTile(out, tiles[x][y], 1);
                    tiles[x][y].setAllowSummon(true);
                } else {
                    if (enemyUnitList.contains(unitArea[x][y])) {
                        BasicCommands.drawTile(out, tiles[x][y], 2);
                        tiles[x][y].setAttackable(true);
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        int aR = movingUnit.getAtkRange();
        int[][] atkArea = new int[(aR * 2 + 1) * (aR * 2 + 1) - 1][2];

        for (int i = -aR, index = 0; i <= aR; i++) {
            for (int j = -aR; j <= aR; j++) {
                if (!(i == 0 && j == 0)) {
                    atkArea[index][0] = i;
                    atkArea[index][1] = j;
                    index++;
                }
            }
        }

        for (int[] i : pos) {
            int x = unitX + i[0];
            int y = unitY + i[1];
            if (x >= 0 && x < 9 && y >= 0 && y < 5) {
                for (int[] j : atkArea) {
                    int atkx = x + j[0];
                    int atky = y + j[1];

                    if (atkx >= 0 && atkx < 9 && atky >= 0 && atky < 5 &&
                            (!tiles[atkx][atky].checkAllowSummon()) && enemyUnitList.contains(unitArea[atkx][atky])) {

                        BasicCommands.drawTile(out, tiles[atkx][atky], 2);
                        tiles[atkx][atky].setAttackable(true);
                    }
                }
            }

        }
    }

    public int isUnitOnTile(int x, int y) {//return value: 0 means none unit on tile, positive: friend unit, negative: enemy unit
        if (unitArea[x][y] == null) return 0;
        else {
            if (friendUnitList.contains(unitArea[x][y])) {
                return 1;
            } else {
                return -1;
            }
        }
    }


    public boolean tryToMoveUnit(int x, int y, ActorRef out) {
        checkProvoked(movingUnit);


        int unitX = movingUnit.getPosition().getTilex();
        int unitY = movingUnit.getPosition().getTiley();
        System.out.println(String.format("{%d,%d}{%d,%d}:%b", unitX, unitY, x, y, tiles[x][y].checkAllowSummon()));
        System.out.println(String.format("{%d,%d}{%d,%d}:%b", unitX, unitY, x, y, tiles[x][y].isAttackable()));
        boolean res = false;
        if (tiles[x][y].checkAllowSummon()) {
            if (movingUnit.isProvoked()) {
                BasicCommands.addPlayer1Notification(out, "This unit is provoked!", 2);
                movingUnit = null;
                state = State.Idle;
                return false;
            }
            unitArea[unitX][unitY] = null;
            unitArea[x][y] = movingUnit;
            Position pos = new Position(tiles[x][y].getXpos(), tiles[x][y].getYpos(), x, y);
            movingUnit.setPosition(pos);

            BasicCommands.moveUnitToTile(out, movingUnit, tiles[x][y]);


            movingUnit.setMovingPoint(movingUnit.getMovingPoint() - Math.abs(unitY - y) - Math.abs(unitX - x));


            res = true;
        } else if (tiles[x][y].isAttackable()) {


            int aR = movingUnit.getAtkRange();
            int[][] pos = new int[(aR * 2 + 1) * (aR * 2 + 1) - 1][2];

            for (int i = -aR, index = 0; i <= aR; i++) {
                for (int j = -aR; j <= aR; j++) {
                    if (!(i == 0 && j == 0)) {
                        pos[index][0] = i;
                        pos[index][1] = j;
                        index++;
                    }
                }
            }
            int[] target = {x - unitX, y - unitY};
            for (int[] i : pos) {


                if (i[0] == target[0] && i[1] == target[1]) {

                    if ((movingUnit.isProvoked() && unitArea[x][y].containProperty(Unit.Property.Provoke) && Math.abs(i[0]) <= 1 && Math.abs(i[1]) <= 1) || (!movingUnit.isProvoked())) {

                        attackUnit(unitArea[unitX][unitY], unitArea[x][y], out);
                        res = true;
                        break;
                    } else {
                        BasicCommands.addPlayer1Notification(out, "Unit with provoke should be attack first", 2);
                        return false;
                    }


                }

            }
            for (int[] i : pos) {
                if (res) break;
                int moveX = x + i[0];
                int moveY = y + i[1];

                if (moveX < 9 && moveX >= 0 && moveY < 5 && moveY >= 0 && tiles[moveX][moveY].checkAllowSummon()) {
                    if (tryToMoveUnit(moveX, moveY, out)) {
                        state = State.Attack;
                        movingUnit = unitArea[x][y];
                        resetTile(out);
                        return true;
                    }

                }
            }

        }
        //checkProvoked(movingUnit);
        state = State.Idle;
        resetTile(out);
        movingUnit = null;
        return res;
    }

    public void attackUnit(Unit attacker, Unit target, ActorRef out) {

        pause = true;


        if (attacker.isProvoked() && !target.containProperty(Unit.Property.Provoke)) {
            BasicCommands.addPlayer1Notification(out, "I must attack the unit with Provoke", 2);
            return;
        }

        attacker.setAtkNum(attacker.getAtkNum() - 1);

        BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.attack);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dealDamage(attacker, target, attacker.getAttack(), out);
        if (!target.isDead()) {
            int[] pos = {attacker.getPosition().getTilex() - target.getPosition().getTilex(), attacker.getPosition().getTiley() - target.getPosition().getTiley()};
            int aR = target.getAtkRange();


            for (int i = -aR; i <= aR; i++) {
                for (int j = -aR; j <= aR; j++) {
                    if (!(i == 0 && j == 0) && i == pos[0] && j == pos[1]) {
                        BasicCommands.playUnitAnimation(out, target, UnitAnimationType.attack);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dealDamage(target, attacker, target.getAttack(), out);

                        if (attacker.isDead()) {
                            if (attacker.getId() == -1) {
                                BasicCommands.addPlayer1Notification(out, "We lose the game!", 2);

                                pause = true;
                                return;
                            } else if (attacker.getId() == -2) {
                                BasicCommands.addPlayer1Notification(out, "We win the game!", 2);
                                pause = true;
                                return;
                            }
                        }
                    }
                }
            }


        } else {

            if (target.getId() == -1) {
                BasicCommands.addPlayer1Notification(out, "We lose the game!", 2);

                pause = true;
                return;
            } else if (target.getId() == -2) {
                BasicCommands.addPlayer1Notification(out, "We win the game!", 2);
                pause = true;
                return;
            }
        }


        attacker.setMovingPoint(0);
        movingUnit = null;
        state = State.Idle;
        pause = false;
    }

    public void attackUnit(int attackerID, ActorRef out) {

        for (Unit i : unitList) {
            if (i.getId() == attackerID) {
                attackUnit(i, movingUnit, out);
                break;
            }
        }
    }

    public void spendMana(int manacost, Player player) {//1 for player, others for ai


        player.setMana(player.getMana() - manacost);


    }

    public void resetUnit() {
        for (Unit i : unitList) {
            i.resetAction();
        }
    }


    public void endTurn(int team, ActorRef out) {

        Player player = team != 1 ? this.player : this.ai;
        player.increaseTurnCount();
        int mana = player.getTurnCount();
        if (mana > 9) mana = 9;
        player.setMana(mana);
        if (team != 1) {
            BasicCommands.setPlayer1Mana(out, player);
            playerTurn = true;
            pause = false;
            playerDraw(out);
        } else {
            playerTurn = false;
            pause = true;
            BasicCommands.setPlayer2Mana(out, player);
            aiDraw(out);
            aiAction(out);
        }

    }

    public void usingSpell(ActorRef out, int x, int y, int team) {
        this.pause = true;
        int unitTeam = isUnitOnTile(x, y);


        Player player = team == 1 ? this.player : this.ai;
        CardController playercard = team == 1 ? this.playerCard : this.aiCard;
        boolean isValid = false;
        if (unitTeam != 0) {
            Unit target = unitArea[x][y];
            SpellCard card = (SpellCard) playercard.getSummonCard();

            SpellAttribute attribute = (SpellAttribute) database.getAtrributeByCardID(card.getCardID());

            SpellUsedEvent event = (SpellUsedEvent) attribute.getEvents()[0];

            switch (card.getTarget()) {
                case All:
                    isValid = true;
                    break;
                case Unit:
                    if (target.getId() >= 0) {
                        isValid = true;

                    }
                    break;
                case FriendPlayer:
                    if ((target.getId() == -1 && unitTeam > 0) || (target.getId() == -2 && unitTeam < 0)) {
                        isValid = true;

                    }
                    break;

                case EnemyUnit:
                    if ((team == 1 && unitTeam < 0) || (team != 1 && unitTeam > 0)) {
                        isValid = true;
                    }
                    break;


            }

            if (isValid) {
                spendMana(playercard.getSummonCard().getManacost(), player);
                if (team == 1) BasicCommands.setPlayer1Mana(out, player);
                else BasicCommands.setPlayer2Mana(out, player);
                event.usingSpell(target, this, out);
                if (team == 1) playercard.removeCard(card, out);
                else playercard.removeCard(card);
                if (target.getId() == -1) {
                    player.setHealth(target.getHP());
                    BasicCommands.setPlayer1Health(out, this.player);
                } else if (target.getId() == -2) {
                    player.setHealth(target.getHP());
                    BasicCommands.setPlayer2Health(out, this.player);
                }
                ArrayList<Unit> units = team == 1 ? this.enemyUnitList : this.friendUnitList;

                for (Unit unit : units) {
                    if (unit.containProperty(Unit.Property.SpellThief)) {
                        unit.setAttack(unit.getAttack() + 1);
                        unit.setMaxHealth(unit.getMaxHealth() + 1);
                        unit.setHeath(unit.getHP() + 1);
                        BasicCommands.setUnitAttack(out, unit, unit.getAttack());
                        BasicCommands.setUnitHealth(out, unit, unit.getHP());
                    }
                }


            } else {
                BasicCommands.addPlayer1Notification(out, "Wrong target", 2);
            }
        }
        if (team == 1) playercard.highlightCard(-1, out);
        state = State.Idle;
        this.pause = false;
    }

    public void checkProvoked(Unit unit) {
        if (unit == null) return;
        int x = unit.getPosition().getTilex();
        int y = unit.getPosition().getTiley();
        int[][] pos = {{-1, -1}, {0, -1}, {1, -1},
                {-1, 0}, {1, 0},
                {-1, 1}, {0, 1}, {1, 1}};
        ArrayList<Unit> enemyList = this.enemyUnitList.contains(unit) ? this.friendUnitList : this.enemyUnitList;

        unit.setProvoked(false);

        for (int[] i : pos) {
            int newx = x + i[0];
            int newy = y + i[1];
            //System.out.println(String.format("{%d,%d}", newx,newy));
            if (newx < 0 || newx > 8 || newy < 0 || newy > 4 || unitArea[newx][newy] == null) continue;
            //System.out.println(String.format("{%d,%d}:%s", newx,newy,unitArea[newx][newy]));
            if (unitArea[newx][newy].containProperty(Unit.Property.Provoke) && enemyList.contains(unitArea[newx][newy])) {
                unit.setProvoked(true);

            }
			
			/*if((!unit.isDead())&&unit.containProperty(Unit.Property.Provoke)&&enemyList.contains(unitArea[newx][newy])) {
				unitArea[newx][newy].setProvoked(true);
			}*/
        }
    }

    public void checkProvoked(int id) {
        for (Unit unit : this.unitList) {
            if (unit.getId() == id) {
                checkProvoked(unit);
                break;
            }
        }
    }

    public void dealDamage(Unit attacker, Unit target, int hp, ActorRef out) {
        target.getDemage(hp);
        BasicCommands.setUnitHealth(out, target, target.getHP());
        if (target.getId() == -1) {
            player.setHealth(target.getHP());
            BasicCommands.setPlayer1Health(out, player);
        } else if (target.getId() == -2) {
            ai.setHealth(target.getHP());
            BasicCommands.setPlayer2Health(out, ai);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Unit unit : unitList) {
            DamageAction action = (DamageAction) unit.getEvent(DamageAction.class);
            if (action != null) {
                action.damageDealt(out, this, attacker, target, unit);
            }
        }

        if (target.isDead()) {
            BasicCommands.playUnitAnimation(out, target, UnitAnimationType.death);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BasicCommands.deleteUnit(out, target);

            UnitDyingAction action = (UnitDyingAction) target.getEvent(UnitDyingAction.class);
            if (action != null) action.onUnitDying(out, this, target);
			
			/*if(target.containProperty(Unit.Property.Provoke)) {
				
				
				
				
				/*int[][] pos= {{-1,-1},{0,-1},{1,-1},
						{-1,0},			{1,0},
						{-1,1},{0,1},{1,1}};
				ArrayList<Unit> enemyList=this.enemyUnitList.contains(target)?this.friendUnitList:this.enemyUnitList;
				for(int[] i:pos) {
					
					int x=target.getPosition().getTilex()+i[0];
					int y=target.getPosition().getTiley()+i[1];
					if(x<0||x>8||y<0||y>4||unitArea[x][y]==null)continue;
					if(enemyList.contains(unitArea[x][y])) {
						checkProvoked(unitArea[x][y]);
					}
				}
			}*/

            resetTile(out);
            unitList.remove(target);
            if (enemyUnitList.contains(target)) enemyUnitList.remove(target);
            if (friendUnitList.contains(target)) friendUnitList.remove(target);
            unitArea[target.getPosition().getTilex()][target.getPosition().getTiley()] = null;
			/*unitList.remove(unitArea[target.getPosition().getTilex()][target.getPosition().getTiley()]);
			enemyUnitList.remove(unitArea[target.getPosition().getTilex()][target.getPosition().getTiley()]);
			unitArea[target.getPosition().getTilex()][target.getPosition().getTiley()]=null;*/


        }
		
		
		
		/*else {
			BasicCommands.playUnitAnimation(out, target, UnitAnimationType.attack);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			target.attack(attacker);
			BasicCommands.setUnitHealth(out, attacker, attacker.getHP());
			
			
			
			if(attacker.getId()==-1) {
				player.setHealth(attacker.getHP());
				BasicCommands.setPlayer1Health(out, player);
			}else if(attacker.getId()==-2) {
				ai.setHealth(attacker.getHP());
				BasicCommands.setPlayer2Health(out, ai);
			}
			
			if(attacker.isDead()) {
				BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.death);
				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
				BasicCommands.deleteUnit(out, attacker);
				
				if(attacker.containProperty(Unit.Property.Provoke)) {
					int[][] pos= {{-1,-1},{0,-1},{1,-1},
							{-1,0},			{1,0},
							{-1,1},{0,1},{1,1}};
					ArrayList<Unit> enemyList=this.enemyUnitList.contains(attacker)?this.friendUnitList:this.enemyUnitList;
					for(int[] i:pos) {
						
						int x=target.getPosition().getTilex()+i[0];
						int y=target.getPosition().getTiley()+i[1];
						if(x<0||x>8||y<0||y>4||unitArea[x][y]==null)continue;
						if(enemyList.contains(unitArea[x][y])) {
							checkProvoked(unitArea[x][y]);
						}
					}
				}
				
				
				unitList.remove(unitArea[attacker.getPosition().getTilex()][attacker.getPosition().getTiley()]);
				friendUnitList.remove(unitArea[attacker.getPosition().getTilex()][attacker.getPosition().getTiley()]);
				unitArea[attacker.getPosition().getTilex()][attacker.getPosition().getTiley()]=null;
				
				if(attacker.getId()==-1) {
					BasicCommands.addPlayer1Notification(out, "We lose the game!", 2);
					
					pause=true;
					return;
				}else if(attacker.getId()==-2) {
					BasicCommands.addPlayer1Notification(out, "We win the game!", 2);
					pause=true;
					return;
				}
				
			}
			
		}*/
    }

    public void aiAction(ActorRef out) {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Card card = null;

                ArrayList<Unit> provoker = new ArrayList<>();
                for (Unit unit : enemyUnitList) {
                    checkProvoked(unit);
                    if (unit.isProvoked()) {
                        int[][] pos = {
                                {-1, -1}, {0, -1}, {1, -1},
                                {-1, 0}, {1, 0},
                                {-1, 1}, {0, 1}, {1, 1}
                        };

                        for (int[] i : pos) {
                            int x = unit.getPosition().getTilex() + i[0];
                            int y = unit.getPosition().getTiley() + i[1];
                            if (x > 8 || x < 0 || y < 0 || y > 8) continue;
                            if (unitArea[x][y] != null && unitArea[x][y].containProperty(Unit.Property.Provoke) && !provoker.contains(unitArea[x][y]) && !enemyUnitList.contains(unitArea[x][y])) {
                                provoker.add(unitArea[x][y]);
                            }
                        }
                    }
                }

                System.out.println("===================provoke check 1...===============");

                Unit playerUnit = friendUnitList.get(0);
                Tile playerArea = tiles[playerUnit.getPosition().getTilex()][playerUnit.getPosition().getTiley()];
                ArrayList<Unit> Attacker = tryToSiege(playerUnit, out);

                if (Attacker != null) {
                    for (Unit unit : Attacker) {
                        movingUnit = unit;
                        tryToMoveUnit(playerArea.getTilex(), playerArea.getTiley(), out);
                        while (state == State.Attack) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("waiting...");
                        }
                        if (playerUnit.isDead()) {
                            state = State.Idle;
                            return;
                        }
                    }
                    if (playerUnit.isDead()) return;
                }

                System.out.println("===================kill player check...===============");


                if ((card = aiCard.containCard("Entropic Decay")) != null && card.getManacost() <= ai.getMana()) {
                    Unit biggestThreat = null;
                    int threatPoint = 0;

                    for (int i = 1; i < friendUnitList.size(); i++) {
                        Unit unit = friendUnitList.get(i);
                        int threat = 0;
                        threat += unit.getAtkNum() + unit.getHP() / 2;
                        if (unit.containProperty(Unit.Property.SpellThief)) threat += 2;
                        if (unit.containProperty(Unit.Property.Flying)) threat += 3;
                        if (unit.containProperty(Unit.Property.Provoke)) threat += 5;
                        if (unit.getAtkRange() > 1) threat += 5;
                        if (threat > threatPoint) {
                            threatPoint = threat;
                            biggestThreat = unit;
                        }
                    }
                    if (biggestThreat != null) {
                        aiCard.setSummonCard(card);
                        usingSpell(out, biggestThreat.getPosition().getTilex(), biggestThreat.getPosition().getTiley(), 0);

                    }
                }
                if ((card = aiCard.containCard("Staff of Y'Kir'")) != null && card.getManacost() <= ai.getMana()) {
                    aiCard.setSummonCard(card);
                    usingSpell(out, enemyUnitList.get(0).getPosition().getTilex(), enemyUnitList.get(0).getPosition().getTiley(), 0);

                }


                System.out.println("===================spell check...===============");

                for (Unit unit : provoker) {
                    Attacker = tryToSiege(unit, out);

                    if (Attacker != null) {
                        for (Unit attacker : Attacker) {
                            movingUnit = attacker;
                            tryToMoveUnit(unit.getPosition().getTilex(), unit.getPosition().getTiley(), out);

                            while (state == State.Attack) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("waiting...");
                            }
                        }

                    }
                }

                for (int i = 0; i < enemyUnitList.size(); i++) {
                    Unit unit = enemyUnitList.get(i);
                    checkProvoked(unit);
                    showMoveArea(out, unit.getPosition().getTilex(), unit.getPosition().getTiley());
                    if (unit.isProvoked()) {
                        int[][] pos = {
                                {-1, -1}, {0, -1}, {1, -1},
                                {-1, 0}, {1, 0},
                                {-1, 1}, {0, 1}, {1, 1}
                        };

                        for (int[] j : pos) {
                            int x = unit.getPosition().getTilex() + j[0];
                            int y = unit.getPosition().getTiley() + j[1];
                            if (x > 8 || x < 0 || y > 8 || y < 0) continue;
                            if (unitArea[x][y] != null && unitArea[x][y].containProperty(Unit.Property.Provoke) && friendUnitList.contains(unitArea[x][y])) {
                                movingUnit = unit;
                                System.out.println(String.format("{%d,%d} is provoked by {%d,%d}", unit.getPosition().getTilex(), unit.getPosition().getTiley(), x, y));
                                tryToMoveUnit(x, y, out);

                                while (state == State.Attack) {
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("waiting...");
                                }
                            }
                        }
                    } else {
                        Unit Threat = null;
                        int targetThreat = 0;


                        for (Tile[] t : tiles) {
                            for (Tile tile : t) {
                                if (tile.isAttackable()) {

                                    Unit target = unitArea[tile.getTilex()][tile.getTiley()];
                                    int threat = 0;
                                    threat += unit.getAtkNum();
                                    if (target.getId() == -1) threat += 1;
                                    if (target.containProperty(Unit.Property.SpellThief)) threat += 2;
                                    if (target.containProperty(Unit.Property.Flying)) threat += 3;
                                    if (target.containProperty(Unit.Property.Provoke)) threat += 5;
                                    if (target.getAtkRange() > 1) threat += 5;
                                    if (unit.getAttack() >= target.getHP()) threat += 20;
                                    if (threat > targetThreat) {
                                        targetThreat = threat;
                                        Threat = target;
                                    }
                                    //System.out.println(String.format("={%d,%d}=:%b", target.getPosition().getTilex(),target.getPosition().getTiley(),tile.isAttackable()));
                                }
                            }
                        }


                        if (Threat != null) {
                            //System.out.println(Threat.getPosition().getTilex()+","+ Threat.getPosition().getTiley()+"============================================================");
                            tryToMoveUnit(Threat.getPosition().getTilex(), Threat.getPosition().getTiley(), out);
                            while (state == State.Attack) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("waiting...");
                            }
                        }
                        resetTile(out);
                    }
                }
                System.out.println("===================attack check...===============");
                Card[] hand = aiCard.getHands();
                int handNumber = aiCard.getHandNumber();
                for (int i = 0; i < handNumber; i++) {
                    Card c = hand[i];
                    System.out.println(c.getManacost() + ":===================summon check...===============");
                    if (c.getManacost() <= ai.getMana() && !(c instanceof SpellCard)) {
                        aiCard.setSummonCard(c);
                        //showSummonArea(out, 0);
                        boolean summon = false;
                        int nearx = 0;
                        int neary = 0;
                        int aix = enemyUnitList.get(0).getPosition().getTilex();
                        int aiy = enemyUnitList.get(0).getPosition().getTiley();
                        //System.out.println("===================Try to summon 1...===============");
                        showSummonArea(out, 0);
                        for (Tile[] t : tiles) {
                            //System.out.println(":===================Try to summon 2...===============");
                            //if(summon==true)break;
                            //System.out.println(":===================Try to summon 3...===============");
                            for (Tile tile : t) {

                                //System.out.println(tile.checkAllowSummon()+":===================Try to summon 4...===============");
                                if (tile.checkAllowSummon()) {
                                    //System.out.println(":===================Try to summon 5...===============");

                                    int x = tile.getTilex() - aix;
                                    int y = tile.getTiley() - aiy;
                                    int large = Math.max(Math.abs(x), Math.abs(y));
                                    int recordLarge = Math.max(Math.abs(nearx - aix), Math.abs(neary - aiy));
                                    if (large < recordLarge) {
                                        nearx = x + aix;
                                        neary = y + aiy;
                                    }
                                    summon = true;
                                    break;
                                }
                            }
                        }

                        if (summon) {
                            summonUnit(out, nearx, neary, 0);
                        }
                        resetTile(out);

                        //System.out.println(hand[i]);
                        i--;
                        handNumber--;

                    }
                }
                state = State.Idle;

                if (playerUnit.isDead()) {
                    state = State.Idle;
                    return;
                } else {
                    endTurn(0, out);
                }
                System.out.println(String.format("pause:%b,playerTurn:%b", pause, playerTurn));


            }
        });

        t.start();


    }

    private ArrayList<Unit> tryToSiege(Unit target, ActorRef out) {

        Tile targetArea = tiles[target.getPosition().getTilex()][target.getPosition().getTiley()];
        ArrayList<Unit> Attacker = new ArrayList<>();
        int attack = 0;
        for (Unit unit : enemyUnitList) {
            showMoveArea(out, unit.getPosition().getTilex(), unit.getPosition().getTiley());
            int x = Math.abs(targetArea.getTilex() - unit.getPosition().getTilex());
            int y = Math.abs(targetArea.getTiley() - unit.getPosition().getTiley());

            if (targetArea.isAttackable() && (!unit.isProvoked() || (target.containProperty(Unit.Property.Provoke) && x <= 1 && y <= 1))) {
                attack += unit.getAttack();
                Attacker.add(unit);
            }
            resetTile(out);
        }
        if (attack >= target.getHP()) {
            return Attacker;
        }

        return null;
    }


}
