// author: 2564916J
package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.ActionEvent;
import structures.basic.EffectAnimation;
import structures.basic.DamageAction;
import structures.basic.Player;
import structures.basic.SpellAttribute;
import structures.basic.SpellCard;
import structures.basic.SpellUsedEvent;
import structures.basic.SummonAction;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAttribute;
import structures.basic.UnitAnimationType;
import structures.basic.UnitDyingAction;


/**
 * This is a utility class that just has short-cuts to the location of various
 * config files.
 * <p>
 * IMPORTANT: Note the start letter for unit types is u_... while the start letter
 * for card types is c_...
 *
 * @author Dr. Richard McCreadie
 */
public class StaticConfFiles {

    // Board Pieces
    public final static String tileConf = "conf/gameconfs/tile.json";
    public final static String gridConf = "conf/gameconfs/grid.json";

    // Avatars
    public final static String humanAvatar = "conf/gameconfs/avatars/avatar1.json";
    public final static String aiAvatar = "conf/gameconfs/avatars/avatar2.json";

    // Deck 1 Cards
    public final static String c_truestrike = "conf/gameconfs/cards/1_c_s_truestrike.json";
    public final static String c_sundrop_elixir = "conf/gameconfs/cards/1_c_s_sundrop_elixir.json";
    public final static String c_comodo_charger = "conf/gameconfs/cards/1_c_u_comodo_charger.json";
    public final static String c_azure_herald = "conf/gameconfs/cards/1_c_u_azure_herald.json";
    public final static String c_azurite_lion = "conf/gameconfs/cards/1_c_u_azurite_lion.json";
    public final static String c_fire_spitter = "conf/gameconfs/cards/1_c_u_fire_spitter.json";
    public final static String c_hailstone_golem = "conf/gameconfs/cards/1_c_u_hailstone_golem.json";
    public final static String c_ironcliff_guardian = "conf/gameconfs/cards/1_c_u_ironcliff_guardian.json";
    public final static String c_pureblade_enforcer = "conf/gameconfs/cards/1_c_u_pureblade_enforcer.json";
    public final static String c_silverguard_knight = "conf/gameconfs/cards/1_c_u_silverguard_knight.json";
    public final static String[] playerDeck = {c_truestrike,
            c_sundrop_elixir,
            c_comodo_charger,
            c_azure_herald,
            c_azurite_lion,
            c_fire_spitter,
            c_hailstone_golem,
            c_ironcliff_guardian,
            c_pureblade_enforcer,
            c_silverguard_knight};


    // Deck 1 Units
    public final static String u_comodo_charger = "conf/gameconfs/units/comodo_charger.json";
    public final static String u_azure_herald = "conf/gameconfs/units/azure_herald.json";
    public final static String u_azurite_lion = "conf/gameconfs/units/azurite_lion.json";
    public final static String u_fire_spitter = "conf/gameconfs/units/fire_spitter.json";
    public final static String u_hailstone_golem = "conf/gameconfs/units/hailstone_golem.json";
    public final static String u_ironcliff_guardian = "conf/gameconfs/units/ironcliff_guardian.json";
    public final static String u_pureblade_enforcer = "conf/gameconfs/units/pureblade_enforcer.json";
    public final static String u_silverguard_knight = "conf/gameconfs/units/silverguard_knight.json";
    public final static String[] playerUnit = {u_comodo_charger,
            u_azure_herald,
            u_azurite_lion,
            u_fire_spitter,
            u_hailstone_golem,
            u_ironcliff_guardian,
            u_pureblade_enforcer,
            u_silverguard_knight
    };

    // Deck 2 Cards
    public final static String c_staff_of_ykir = "conf/gameconfs/cards/2_c_s_staff_of_ykir.json";
    public final static String c_entropic_decay = "conf/gameconfs/cards/2_c_s_entropic_decay.json";
    public final static String c_blaze_hound = "conf/gameconfs/cards/2_c_u_blaze_hound.json";
    public final static String c_bloodshard_golem = "conf/gameconfs/cards/2_c_u_bloodshard_golem.json";
    public final static String c_hailstone_golemR = "conf/gameconfs/cards/2_c_u_hailstone_golem.json";
    public final static String c_planar_scout = "conf/gameconfs/cards/2_c_u_planar_scout.json";
    public final static String c_pyromancer = "conf/gameconfs/cards/2_c_u_pyromancer.json";
    public final static String c_rock_pulveriser = "conf/gameconfs/cards/2_c_u_rock_pulveriser.json";
    public final static String c_serpenti = "conf/gameconfs/cards/2_c_u_serpenti.json";
    public final static String c_windshrike = "conf/gameconfs/cards/2_c_u_windshrike.json";

    public final static String[] aiDeck = {c_staff_of_ykir,
            c_entropic_decay,
            c_blaze_hound,
            c_bloodshard_golem,
            c_hailstone_golemR,
            c_planar_scout,
            c_pyromancer,
            c_rock_pulveriser,
            c_serpenti,
            c_windshrike


    };

    // Deck 2 Units
    public final static String u_blaze_hound = "conf/gameconfs/units/blaze_hound.json";
    public final static String u_bloodshard_golem = "conf/gameconfs/units/bloodshard_golem.json";
    public final static String u_hailstone_golemR = "conf/gameconfs/units/hailstone_golem2.json";
    public final static String u_planar_scout = "conf/gameconfs/units/planar_scout.json";
    public final static String u_pyromancer = "conf/gameconfs/units/pyromancer.json";
    public final static String u_rock_pulveriser = "conf/gameconfs/units/rock_pulveriser.json";
    public final static String u_serpenti = "conf/gameconfs/units/serpenti.json";
    public final static String u_windshrike = "conf/gameconfs/units/windshrike.json";
    public final static String[] aiUnits = {u_blaze_hound,
            u_bloodshard_golem,
            u_hailstone_golemR,
            u_planar_scout,
            u_pyromancer,
            u_rock_pulveriser,
            u_serpenti,
            u_windshrike

    };

    // Effects
    public final static String f1_inmolation = "conf/gameconfs/effects/f1_inmolation.json";
    public final static String f1_buff = "conf/gameconfs/effects/f1_buff.json";
    public final static String f1_martyrdom = "conf/gameconfs/effects/f1_martyrdom.json";
    public final static String f1_projectiles = "conf/gameconfs/effects/f1_projectiles.json";
    public final static String f1_summon = "conf/gameconfs/effects/f1_summon.json";
    //set different unit cards
    public final static String[] unitCards = {
            c_comodo_charger,//0
            c_azure_herald,//1
            c_azurite_lion,//2
            c_fire_spitter,//3
            c_hailstone_golem,//4
            c_ironcliff_guardian,//5
            c_pureblade_enforcer,//6
            c_silverguard_knight,//7
            c_blaze_hound,//8
            c_bloodshard_golem,//9
            c_hailstone_golemR,//10
            c_planar_scout,//11
            c_pyromancer,//12
            c_rock_pulveriser,//13
            c_serpenti,//14
            c_windshrike//15
    };

    public final static String[] units = {
            u_comodo_charger,
            u_azure_herald,
            u_azurite_lion,
            u_fire_spitter,
            u_hailstone_golem,
            u_ironcliff_guardian,
            u_pureblade_enforcer,
            u_silverguard_knight,
            u_blaze_hound,
            u_bloodshard_golem,
            u_hailstone_golemR,
            u_planar_scout,
            u_pyromancer,
            u_rock_pulveriser,
            u_serpenti,
            u_windshrike

    };

    //set the different special functions corresponding to the different cards
    public final static Unit.Property[][] unitProperty = {
            {},
            {},
            {},
            {},
            {},
            {Unit.Property.Provoke, Unit.Property.AirDrop},
            {Unit.Property.SpellThief},
            {Unit.Property.Provoke},
            {},
            {},
            {},
            {Unit.Property.AirDrop},
            {},
            {Unit.Property.Provoke},
            {},
            {Unit.Property.Flying}
    };

    //set the different special effects on different cards' properties
    public final static ActionEvent[][] unitEvent = {
            {},//0
            {//1
                    new SummonAction() {

                        @Override
                        public void onSummon(ActorRef out, GameState gameState, Player summoner, Unit unit,
                                             Unit summonerAvatar) {
                            // TODO Auto-generated method stub
                            summonerAvatar.getHealing(3);
                            BasicCommands.setUnitHealth(out, summonerAvatar, summonerAvatar.getHP());
                            summoner.setHealth(summonerAvatar.getHP());
                            BasicCommands.setPlayer1Health(out, summoner);
                        }

                    }
            },
            {},//2
            {},//3
            {},//4
            {},//5
            {},//6
            {//7
                    new DamageAction() {
                        @Override
                        public void damageDealt(ActorRef out, GameState state, Unit attacker, Unit target, Unit trigger) {
                            int x = this.trigger.getPosition().getTilex();
                            int y = this.trigger.getPosition().getTiley();
                            int triggerTeam = state.isUnitOnTile(x, y);

                            x = target.getPosition().getTilex();
                            y = target.getPosition().getTiley();

                            int targetTeam = state.isUnitOnTile(x, y);

                            if (triggerTeam == targetTeam && (target.getId() < 0)) {
                                trigger.setAttack(trigger.getAttack() + 2);
                                BasicCommands.setUnitAttack(out, trigger, trigger.getAttack());
                            }

                        }
                    }
            },
            {//8
                    new SummonAction() {

                        @Override
                        public void onSummon(ActorRef out, GameState gameState, Player summoner, Unit unit,
                                             Unit summonerAvatar) {
                            // TODO Auto-generated method stub
                            gameState.aiDraw(out);
                            gameState.playerDraw(out);
                        }
                    }
            },
            {},//9
            {},//10
            {},//11
            {},//12
            {},//13
            {},//14
            {
                    new UnitDyingAction() {

                        @Override
                        public void onUnitDying(ActorRef out, GameState gameState, Unit unit) {
                            // TODO Auto-generated method stub
                            int team = gameState.isUnitOnTile(unit.getPosition().getTilex(), unit.getPosition().getTiley());
                            if (team == 1) gameState.playerDraw(out);
                            else gameState.aiDraw(out);
                        }

                    }
            }//15


    };

    // the final unit cards contained its name, special effects, special properties
    public final static UnitAttribute[] unitsAttributes = {
            new UnitAttribute(1, 3, 1, 1, u_comodo_charger, unitEvent[0], unitProperty[0]),
            new UnitAttribute(1, 4, 1, 1, u_azure_herald, unitEvent[1], unitProperty[1]),
            new UnitAttribute(2, 3, 2, 1, u_azurite_lion, unitEvent[2], unitProperty[2]),
            new UnitAttribute(3, 2, 1, 9, u_fire_spitter, unitEvent[3], unitProperty[3]),
            new UnitAttribute(4, 6, 1, 1, u_hailstone_golem, unitEvent[4], unitProperty[4]),
            new UnitAttribute(3, 10, 1, 1, u_ironcliff_guardian, unitEvent[5], unitProperty[5]),
            new UnitAttribute(1, 4, 1, 1, u_pureblade_enforcer, unitEvent[6], unitProperty[6]),
            new UnitAttribute(1, 5, 1, 1, u_silverguard_knight, unitEvent[7], unitProperty[7]),
            new UnitAttribute(4, 3, 1, 1, u_blaze_hound, unitEvent[8], unitProperty[8]),
            new UnitAttribute(4, 3, 1, 1, u_bloodshard_golem, unitEvent[9], unitProperty[9]),
            new UnitAttribute(4, 6, 1, 1, u_hailstone_golemR, unitEvent[10], unitProperty[10]),
            new UnitAttribute(2, 1, 1, 1, u_planar_scout, unitEvent[11], unitProperty[11]),
            new UnitAttribute(2, 1, 1, 9, u_pyromancer, unitEvent[12], unitProperty[12]),
            new UnitAttribute(1, 4, 1, 1, u_rock_pulveriser, unitEvent[13], unitProperty[13]),
            new UnitAttribute(7, 4, 2, 1, u_serpenti, unitEvent[14], unitProperty[14]),
            new UnitAttribute(4, 3, 1, 1, u_windshrike, unitEvent[15], unitProperty[15])
    };

    // the final spellcards
    public final static String[] spellCards = {
            c_staff_of_ykir,//16
            c_entropic_decay,//17
            c_truestrike,//18
            c_sundrop_elixir//19
    };

    private final static ActionEvent[][] events = {
            {
                    new SpellUsedEvent() {
                        @Override
                        public void usingSpell(Unit target, GameState gameState, ActorRef out) {
                            // TODO Auto-generated method stub
                            target.setAttack(target.getAttack() + 2);
                            Tile tile = gameState.getTile(target);
                            EffectAnimation ef = BasicObjectBuilders.loadEffect(f1_buff);
                            BasicCommands.setUnitAttack(out, target, target.getAttack());
                            BasicCommands.playEffectAnimation(out, ef, tile);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


                    }
            },
            {
                    new SpellUsedEvent() {
                        @Override
                        public void usingSpell(Unit target, GameState gameState, ActorRef out) {
                            // TODO Auto-generated method stub
                            Tile tile = gameState.getTile(target);
                            EffectAnimation ef = BasicObjectBuilders.loadEffect(f1_martyrdom);
                            BasicCommands.playEffectAnimation(out, ef, tile);
                            gameState.dealDamage(null, target, target.getHP(), out);
                            //target.setHeath(0);
					
					// for debugging
							/*BasicCommands.setUnitHealth(out, target, 0);
					
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					BasicCommands.playUnitAnimation(out, target, UnitAnimationType.death);
					try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
					BasicCommands.deleteUnit(out,target);
					gameState.deleteUnit(target);*/

                        }


                    }
            },
            {
                    new SpellUsedEvent() {
                        @Override
                        public void usingSpell(Unit target, GameState gameState, ActorRef out) {
                            // TODO Auto-generated method stub

                            Tile tile = gameState.getTile(target);
                            EffectAnimation ef = BasicObjectBuilders.loadEffect(f1_inmolation);


                            BasicCommands.playEffectAnimation(out, ef, tile);
                            gameState.dealDamage(null, target, 2, out);


                        }


                    }
            },
            {
                    new SpellUsedEvent() {
                        @Override
                        public void usingSpell(Unit target, GameState gameState, ActorRef out) {
                            // TODO Auto-generated method stub

                            Tile tile = gameState.getTile(target);
                            EffectAnimation ef = BasicObjectBuilders.loadEffect(f1_buff);
                            target.getHealing(5);
                            BasicCommands.setUnitHealth(out, target, target.getHP());
                            BasicCommands.playEffectAnimation(out, ef, tile);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }


                    }
            }
    };

    // the final spell cards with its buff and special effects
    public final static SpellAttribute[] spellAttribute = {
            new SpellAttribute(SpellCard.Target.FriendPlayer, f1_buff, events[0]),
            new SpellAttribute(SpellCard.Target.Unit, f1_martyrdom, events[1]),
            new SpellAttribute(SpellCard.Target.EnemyUnit, f1_inmolation, events[2]),
            new SpellAttribute(SpellCard.Target.All, f1_buff, events[3])
    };


}
