package instance.AionBGs;

import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaManager;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaScore;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

@InstanceID(300250000) //estorace bio lab
public class Arena_EsoraceBioLab extends ArenaManager {
    private ScheduledFuture<?> RoundTask;
    private ScheduledFuture<?> ResTask;
    private ScheduledFuture<?> countDownTask;
  
    private ScheduledFuture<?> countTask;
    private long countSec = 0;
    
    private boolean eventIsComplete = false;
    
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
    	round = 1;        
        players = new FastList<Player>();
        score = new FastList<ArenaScore>();
        super.onInstanceCreate(instance);
    }    
    @Override
    public void onInstanceDestroy() {
    	RoundTask = cancelTask(this.RoundTask);    	
    	ResTask = cancelTask(this.ResTask);   
        this.players = null;
        this.score = null;
    }
    @Override
    public void onEnterInstance(final Player player) {
    	super.onEnterInstance(player);
    	if (player.isInArena()) { 
	        if (instance.isRegistered(player.getObjectId()) && !this.containsPlayer(player.getObjectId(), this.players)) {
	            this.players.add(player);
	        } 
	        else {
	            return;
	        }
	        rootPlayer(player, true);	        
	        if (!containsInScoreList(player.getObjectId(), this.score)) {
	        	this.addToScoreList(player, this.score);
	        } 	        
	        if (this.instance.getPlayersInside().size() == 2) {
	        	this.sendSpecMessage("Round " + round, "Battle start in " + this.waitingTime + " seconds. Buff up now!!", this.instance);
	        	ThreadPoolManager.getInstance().schedule(new Runnable() {
		            @Override
		            public void run() {
		            	for (final Player p : players) {
		            		rootPlayer(p, false);		            		
		            	}
		                StartRound();
		            }
		        }, waitingTime * 1000);		     
	        	startCountDownArena(this.countDownTask, this.waitingTime, this.instance, false, this.round);
	        } else {
	        	PacketSendUtility.sendYellowMessageOnCenter(player, "Please wait for your match");       	
	        }
    	}
    }    
    @Override
    public void onLeaveInstance(Player player) {
		if (player.isInArena()) {
	    	stopTimer(player);	    	
	    	removeFromScoreList(player.getObjectId(), this.score);
	    	this.players.remove(player);
	        this.eventIsComplete = true;
	        player.setInArena(false);
	        if (ifOnePlayer(players)) {
	        	this.RoundTask = DoReward(this.players, this.RoundTask, this.score);  
	        }
		} 
    }    
    @Override
    public void onPlayerLogOut(Player player) {
    	if (player.isInArena()) {    		
	    	stopTimer(player);	    	
	    	removeFromScoreList(player.getObjectId(), this.score);
	    	this.players.remove(player);
	        this.eventIsComplete = true;
	        player.setInArena(false);
	        if (ifOnePlayer(players)) {
	        	this.RoundTask = DoReward(this.players, this.RoundTask, this.score);  
	        }
    	}
    }    
    @Override
    public boolean isEnemy(Player attacker, Player target) {
        if (attacker != target) {
            return true;
        }
        return super.isEnemy(attacker, target);
    }    
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
    	if (player.isInArena()) {
    		onDead(player, lastAttacker); 
    		this.ResTask = checkResurrection((Player) lastAttacker.getActingCreature()); //running test
    		deathPlayer(player, lastAttacker, this.instance, this.round, this.score, this.players, this.RoundTask);
    	} else {
            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
            PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));
    	}
        return true;
    }    
    @Override
    public boolean onReviveEvent(Player player) {        
    	onRevive(player); 
    	if (player.isInArena()) {	    	  
	    	if (this.eventIsComplete) {
	    		TeleOut(player);
	    	} else {
	    		this.ResTask = cancelTask(this.ResTask);    		
	    		moveToStartPosition(this.players);
	    		getReady();    		
	    	}
    	} else {
    		TeleportService2.moveToBindLocation(player, true);
    	}
        return true;
    }    
    /*
     * ARENA METHODS
     */
    private synchronized void StartRound() {
        if (RoundTask == null) {
            for (Player p : this.players) {
            	rootPlayer(p, false);          	
            	HealPlayer(p, false, true);
            	startTimer(p, this.battle_time);
            }
            if (ifOnePlayer(this.players)) {
                return;
            }
            this.RoundTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                	NextRound(true);
                }
            }, battle_time * 1000);           
            sendSpecMessage("Round " + this.round, "Fight!!!", this.instance);
        }
    }    
    private void NextRound(boolean timeIsUp) {
        if (this.players == null || this.players.isEmpty()) {
            return;
        }
        for (Player p : this.players) {
        	rootPlayer(p, false);
        }
        this.RoundTask = cancelTask(this.RoundTask);   
        this.round++;
        if (ifOnePlayer(this.players)) {
            return;
        }
        if (timeIsUp) {
            Player winner = timeIsUpEvent(this.players);
            if (winner != null) {
            	getScore(winner.getObjectId(), score).incWin();
                if (hasWinner(score)) {
                	this.RoundTask = DoReward( this.players, this.RoundTask, this.score);
                	this.eventIsComplete = true;
                    return;
                } else {
                	sendSpecMessage("Round " + this.round, String.format("Time is up! %s is the Winner.. \uE080!", winner.getName()), this.instance);        
        			moveToStartPosition(this.players);
                }
            } else {
            	this.RoundTask = DoReward( this.players, this.RoundTask, this.score);
            	this.eventIsComplete = true;
            	return;
            }
        } 
        StartRound();
    }           
    protected void startCountDownArena (ScheduledFuture<?> newTask, final long deLayInSec, final WorldMapInstance instance, final boolean nextRound, final int round) {
    	setCountSec(deLayInSec);    	
    	countTask = newTask;
		final int currentRound;
		currentRound = round + 1;
    	countTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {	
    		@Override
			public void run() {  
				caculateSec(countSec);
				if (getCountSec() > 0) {
					if (nextRound) {
						sendSpecMessage("Round " + currentRound, String.format("%s seconds", getCountSec()), instance);
					} else {
						sendSpecMessage("Round " + round, String.format("%s seconds", getCountSec()), instance);
					}
				} else if (getCountSec() < 1) {
			    	if (countTask != null ) {
				    	countTask.cancel(true);
				    	countTask = null;
			    	}
				}
			}
    	}, 1 * 1000, 1 * 1000);
	}
    private long getCountSec() {
    	return countSec;
    }
    private void setCountSec(long countSec) {
    	this.countSec = countSec;
    }
    private void caculateSec (long countSec) {
    	countSec--;
    	this.countSec = countSec;
    }
    private void getReady () {        
        for (Player p : this.players) {
            rootPlayer(p, true);                
        }
        int currentRound = round;
        
        this.sendSpecMessage("Round " + currentRound++, "Buff up and get ready!!", this.instance);
        startCountDownArena(countDownTask, 16, this.instance, true, this.round);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {   
                NextRound(false);
            }
        }, 15000);
    }
}