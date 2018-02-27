/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.ecfunctions.arena;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author B1
 *
 */
public class ArenaManager extends ArenaService {	
	protected int round = 1;
	protected int winNeeded = 3;
	protected int waitingTime = 15;
    protected int battle_time = 300;    
    protected List<Player> players;
    protected List<ArenaScore> score;  
    /*
     * Misc Methods
     */
    protected boolean ifOnePlayer(List<Player> players) {
    	 if (players.size() == 1) {
    		 return true;
    	 }
		return false;
    }
    protected boolean hasWinner(List<ArenaScore> score) {
        for (ArenaScore es : score) {
            if (es.getWins() >= winNeeded) {
            	es.isWinner = true;
                return true;
            }
        }
        return false;
    }   
    /*
     * Award Methods
     */
    protected void deathPlayer(Player loser, Creature lastAttacker, WorldMapInstance instance, int round, List<ArenaScore> score, List<Player> players, ScheduledFuture<?> RoundTask) {
    	if (lastAttacker.getActingCreature() instanceof Player && loser != lastAttacker) {			
    		Player winner = (Player) lastAttacker.getActingCreature();    	    		
			ArenaScore winnerScore = getScore(winner.getObjectId(), score);
			winnerScore.incKills();
			winnerScore.incWin();
			ArenaScore loserScore = getScore(loser.getObjectId(), score);
			loserScore.incDeath();
			loserScore.incLose();					
			RoundTask = cancelTask(RoundTask);   	
			winner.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
			HealPlayer(winner,false,true);
			paralizePlayer(winner, true);
			sendSpecMessage("Round " + round, String.format("%s: %s - %s: %s", winner.getName(), winnerScore.getKills(), loser.getName(), loserScore.getKills()), instance);
			PacketSendUtility.sendYellowMessageOnCenter(winner, String.format("You won. Please wait"));
			for (Player p : players) {
				this.stopTimer(p);
			    p.setTarget(null);                   
			}	      
			if (hasWinner(score)) {
				DoReward(players, RoundTask, score);
			}   
		}
    }
    protected ScheduledFuture<?> DoReward(List<Player> players, ScheduledFuture<?> RoundTask, List<ArenaScore> score) {    	
    	int rank;
    	Player firstWinner = null;
    	Player secondWinner = null;
    	for (final Player p : players) {
    		if (players.size() == 1) { // case of one player left
    			if (p.getEnemy().canWinArena()) { 
    				firstWinner = p.getEnemy();
    			} else {
    				firstWinner = p;
    			}
    			arenaAward(firstWinner, true);
    		} else { // case of two players
        		final Object[] names = {"", ""};
            	for (final Player player : players) {
            		ArenaScore es = this.getScore(player.getObjectId(), score);
            		if (es.isWinner) {
            			rank = 1;
            		} else {
            			rank = 2;
            		}
            		switch (rank) {
                    	case 1:
                    		names[0] = player.getName();
                    		firstWinner = player;
                    		arenaAward(firstWinner, true);
                    		break;
                    	case 2:
                    		names[1] = player.getName();	
                    		secondWinner = player;
                    		arenaAward(secondWinner, false);
                    		break;
                   }
                   stopTimer(player);
            	}
    		}        		
     	} 	
    	PacketSendUtility.sendAnnounceMessage(String.format("[Arena] Congratulation! %s is the winner..", firstWinner.getName()), ChatType.BRIGHT_YELLOW_CENTER, false);  
    	setLastWinner(firstWinner);
    	for (final Player player : players) {
           	ThreadPoolManager.getInstance().schedule(new Runnable() {
           		@Override
                public void run() {	   	          
           			paralizePlayer(player, false); 
           			if (!player.getLifeStats().isAlreadyDead()) {
           				TeleOut(player);
           			}
           		}
           	}, 5000);
    	}       	
       	players.clear();       	
       	if (RoundTask != null) {
       		RoundTask.cancel(true);
       		RoundTask = null;
       	}
       	return RoundTask;
	}
    protected void arenaAward(Player winner, boolean win) {
    	if (win) {
            ItemService.addItem(winner, 166020000, 1);	  
            AbyssPointsService.addAGp(winner, 0, 50);
    	} else {
            //ItemService.addItem(winner, 166020000, 0);	  
            AbyssPointsService.addAGp(winner, 0, 25);
    	}
    }       
    /*
     * Score Methods
     */
    protected ArenaScore getScore(int id, List<ArenaScore> theScore) {
        for (ArenaScore es : theScore) {
            if (es.PlayerObjectId == id) {
                return es;
            }
        }
        return null;
    }   
    protected void addToScoreList(Player player, List<ArenaScore> theScore) {
    	ArenaScore es = new ArenaScore(player.getObjectId());
    	theScore.add(es);
    }    
    protected boolean containsInScoreList(int plrObjId, List<ArenaScore> theScore) {
        for (ArenaScore es : theScore) {
            if (es.PlayerObjectId == plrObjId) {
                return true;
            }
        }
        return false;
    }    
    protected boolean removeFromScoreList(int id, List<ArenaScore> theScore) {
        for (ArenaScore es : theScore) {
            if (es.PlayerObjectId == id) {
            	theScore.remove(es);
                return true;
            }
        }
        return false;
    }
    protected boolean containsPlayer(int id, List<Player> thePlayers) {
        for (Player p : thePlayers) {
            if (p != null && p.isOnline() && p.getObjectId() == id) {
                return true;
            }
        }
        return false;
    }
    /*
     * Timing Methods
     */
    protected void startTimer(Player player, int time) {
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, time));
    }
    protected void stopTimer(Player player) {
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
    }
    protected Player timeIsUpEvent(List<Player> players) {
        if (players.size() == 2) {
            Player winner;
            PlayerLifeStats pls1 = players.get(0).getLifeStats();
            PlayerLifeStats pls2 = players.get(1).getLifeStats();
            if (pls1.getCurrentHp() > pls2.getCurrentHp()) {
                winner = players.get(0);
            } 
            else if (pls1.getCurrentHp() < pls2.getCurrentHp()) {
                winner = players.get(1);
            }
            else {
                if (pls1.getMaxHp() > pls2.getMaxHp()) {
                    winner = players.get(0);
                } 
                else if (pls1.getMaxHp() < pls2.getMaxHp()) {
                    winner = players.get(1);
                }
                else {
                    winner = players.get(Rnd.get(0, players.size() - 1));
                }
            }
            return winner;
        }
        return null;
    }
    /*
     * Respawn Methods
     */
    protected void onDead(final Player player, Creature lastAttacker) {    	
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
        PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));    	
    }
    protected void onRevive(Player player) {

		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);		
	}    
    protected void moveToStartPosition(List<Player> thePlayers) {
    	int mapId, instanceId;    	
        for (Player p : thePlayers) {        	
        	mapId = p.getWorldId();
        	instanceId = p.getInstanceId();
        	TeleIn(p, mapId, instanceId, p.bgLocX(), p.bgLocY(), p.bgLocZ(), p.bgLocH());
        	paralizePlayer(p, false);
        	rootPlayer(p, true);
        }		
    }  
    private ScheduledFuture<?> runDuration(final Player player) {
    	@SuppressWarnings("unused")
		ScheduledFuture<?> ResTask;
		return ResTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
            	if (player.getEnemy().getLifeStats().isAlreadyDead()) { 
            		player.setCanLeaveArena(true);               		
            		PacketSendUtility.sendYellowMessageOnCenter(player, "Waiting time is over. You are the winner of Arena.");            	
            		paralizePlayer(player, true);        	
        	        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(5), 0, 0));
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                        	paralizePlayer(player, false);
                        	PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, 0, 1, 0));
                        	TeleOut(player);
                        }
                    }, (int) TimeUnit.SECONDS.toMillis(5));            	
            	} else {
            		player.setCanLeaveArena(false);
            	}
            }
		}, 60000);
	}
    protected ScheduledFuture<?> checkResurrection(Player player) {
    	ScheduledFuture<?> ResTask;
		player.setCanLeaveArena(false);
		ResTask = runDuration(player);
		return ResTask;
	}	
	protected ScheduledFuture<?> cancelTask(ScheduledFuture<?> Task) {
		if (Task != null) {
			Task.cancel(true);
			Task = null;
		}
		return Task;
	}
}
