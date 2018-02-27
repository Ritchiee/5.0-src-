package instance.AionBGs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaManager;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaScore;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

@InstanceID(300800000) //infinity shard
public class Arena_InfinityShard extends ArenaManager {
    private ScheduledFuture<?> RoundTask;
    private ScheduledFuture<?> ResTask;
    private ScheduledFuture<?> countDownTask;
  
    private ScheduledFuture<?> countTask;
    private long countSec = 0;
    
    private boolean eventIsComplete = false;
    
    
    /*
     * BOSS Variables
     */
    private int protection;
    private boolean isInstanceDestroyed;
    private Future<?> resonator;
    private FastMap<Integer, Future<?>> skillTask = new FastMap<Integer, Future<?>>().shared();
    private FastList<Integer> skillCount = FastList.newInstance();
	private boolean isDone1;
	private boolean isDone2;
	private boolean isDone3;
	private boolean isDone4;
    
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
    	this.round = 1;
        
        players = new FastList<Player>();
        score = new FastList<ArenaScore>();
        super.onInstanceCreate(instance);
        
        /*
         * For Bossing 
         */
        Npc hyperion = instance.getNpc(231073);
        SkillEngine.getInstance().getSkill(hyperion, 21254, 60, hyperion).useNoAnimationSkill();
        Npc idegenerator1 = instance.getNpc(231074);
        SkillEngine.getInstance().getSkill(idegenerator1, 21371, 60, idegenerator1).useNoAnimationSkill();
        Npc idegenerator2 = instance.getNpc(231078);
        SkillEngine.getInstance().getSkill(idegenerator2, 21371, 60, idegenerator2).useNoAnimationSkill();
        Npc idegenerator3 = instance.getNpc(231082);
        SkillEngine.getInstance().getSkill(idegenerator3, 21371, 60, idegenerator3).useNoAnimationSkill();
        Npc idegenerator4 = instance.getNpc(231086);
        SkillEngine.getInstance().getSkill(idegenerator4, 21371, 60, idegenerator4).useNoAnimationSkill();
    }    
    @Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
        int npcId = npc.getNpcId();
        int index = dropItems.size() + 1;
        switch (npcId) {
            case 231073: //Hyperion
                for (Player player : instance.getPlayersInside()) {
                    if (player.isOnline()) {                    	
                    	dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053006, 1)); //[Event] Hyperion's Mythic Armor Chest
                    	dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053007, 1)); //[Event] Hyperion's Mythic Weapon Chest
                    	dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052045, 1)); //Doll Godstone Box                    	
                    	dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053295, 1)); //Empyrean Plume Chest.  
                    	
                    	dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051407, 1)); //Herro Chest 
                    	dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053196, 1)); //idian weapon chest
                    	
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000032, 15)); //balur heart
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166020000, 20)); //divine ballaur heart	
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000033, 10)); //hot ballaur hearts	 
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 169405252, 10)); //fury powder
                    	
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166030005, 10)); //Tempering Solution
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166020000, 10)); //Omega Stone
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166500002, 4)); //Amplification Stone
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000236, 20)); //Blood Mark
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 169000010, 1000)); //Superior Power Shard						
						Random r = new Random();
						int  i = r.nextInt(5) + 1;	 	
						if (i == 1) {
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 164002119, 10)); //[Event] Rx: Critozyne
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162001030, 20)); //[Event] Premium Recovery Potion
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000132, 15)); //Wright Token
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052956, 1)); //[Event] Pirate's Tool Bundle
						} else if (i == 2) {
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 164002120, 10)); //[Event] Rx: Mojocycline
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162001033, 20)); //[Event] Premium Recovery Serum   
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000115, 10)); //Circle Token
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051555, 1)); //Cat Weapon Bundle
						} else if (i == 3) {
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 164002116, 10)); //[Event] Rx: Accelerox
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162000107, 20)); //Saam king's Herbs
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000242, 5)); //Ceramium Medal							
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051576, 1)); //Sharptooth Surprise
						} else if (i == 4) {
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 164002119, 10)); //[Event] Rx: Blitzopan
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166030005, 20)); //Cippo Aether Jelly
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000130, 500)); //Crucible Insignia
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052651, 1)); //Title Card Box
						} else if (i == 5) {
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 164002120, 10)); //[Event] Rx: Castafodin
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002024, 10)); //Nutritional jelly paste
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000137, 25)); //Ceramium Medal
							dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051637, 1)); //[Event] Solorius Costume Box
						}     
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, ItemId.KINAH.value(), 20000000)); //Kinah
                    }
                }
                break;
        }
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
    public void onDie(Npc npc) {
        Npc hyperion = getNpc(231073);
		Npc shield = getNpc(284437);
		Player player = npc.getAggroList().getMostPlayerDamage();
        if (isInstanceDestroyed) {
            return;
        }
        switch (npc.getObjectTemplate().getTemplateId()) {
            case 231074:
                sendMsg(1401795);
                protection++;
                removeProtection();
                npc.getController().onDelete();
                break;
            case 231078:
                sendMsg(1401795);
                protection++;
                removeProtection();
                npc.getController().onDelete();
                break;
            case 231082:
                sendMsg(1401795);
                protection++;
                removeProtection();
                npc.getController().onDelete();
                break;
            case 231086:
                sendMsg(1401795);
                protection++;
                removeProtection();
                npc.getController().onDelete();
                break;
            case 231073:
                spawn(730842, 124.669853f, 137.840668f, 113.942917f, (byte) 0);
                cancelResonatorTask();
                despawnNpc(231092);
                despawnNpc(231093);
                despawnNpc(231094);
                despawnNpc(231095);
                PacketSendUtility.sendAnnounceMessage(String.format("[BinAion] %s's alliance has conquered <INFINITY SHARD> instance.", player.getName()), ChatType.BRIGHT_YELLOW_CENTER, false);  
                break;
            case 231087:
            case 231088:
            case 231089:
                isDeadGenerator1();
                break;
            case 231075:
            case 231076:
            case 231077:
                isDeadGenerator2();
                break;
            case 231083:
            case 231084:
            case 231085:
                isDeadGenerator3();
                break;
            case 231079:
            case 231080:
            case 231081:
                isDeadGenerator4();
                break;
            case 231092:
                if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED)) || !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
                    resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
				break;
            case 231093:
                if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED)) || !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
                    resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
                break;
            case 231094:
                 if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED)) || !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
                    resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
                break;
            case 231095:
				 if (!(hyperion.getAi2().getState() == (AIState.IDLE)) || !(hyperion.getAi2().getState() == (AIState.DIED)) || !(hyperion.getAi2().getState() == (AIState.DESPAWNED))) {
                    resonatorSpawn(npc.getNpcId(), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
                break;
        }
    }
    
    
    
    @Override
    public boolean onReviveEvent(Player player) {     
    	if (player.isInArena()) {
	    	onRevive(player);   
	    	if (this.eventIsComplete) {
	    		TeleOut(player);
	    	} else {
	    		this.ResTask = cancelTask(this.ResTask);    		
	    		moveToStartPosition(this.players);
	    		getReady();    		
	    	}
    	} else {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
            PlayerReviveService.revive(player, 100, 100, false, 0);
            player.getGameStats().updateStatsAndSpeedVisually();
            TeleportService2.teleportTo(player, this.mapId, this.instanceId, (float) 118, (float) 115, (float) 131.8584, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
            return true;
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
    
    /*
     * BOSS METHODS
     */
    private void cancelResonatorTask() {
        if (resonator != null && !resonator.isCancelled()) {
            resonator.cancel(true);
        }
    }

    private void cancelskillTask(int npcId) {
        Future<?> task = skillTask.get(npcId);
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        skillTask.remove(npcId);
    }

    private void startSkillTask(final Npc npc, final int skillId, final int messageId) {
        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Npc hyperion = getNpc(231073);
                if (hyperion == null) {
                    return;
                }
                if (hyperion.getLifeStats().isAlreadyDead()) {
                    return;
                }
				if (skillId == 21258 && !isDone1){
					NpcShoutsService.getInstance().sendMsg(npc, messageId);
					SkillEngine.getInstance().getSkill(npc, skillId, 1, hyperion).useNoAnimationSkill();
					if (!skillCount.contains(npc.getNpcId())) {
						skillCount.add(npc.getNpcId());
					}
					isDone1 = true;
				}
				if (skillId == 21382 && !isDone2 && isDone1){
					NpcShoutsService.getInstance().sendMsg(npc, messageId);
					SkillEngine.getInstance().getSkill(npc, skillId, 1, hyperion).useNoAnimationSkill();
					if (!skillCount.contains(npc.getNpcId())) {
						skillCount.add(npc.getNpcId());
					}
					isDone2 = true;
				}
				if (skillId == 21384 && isDone2 && !isDone3){
					NpcShoutsService.getInstance().sendMsg(npc, messageId);
					SkillEngine.getInstance().getSkill(npc, skillId, 1, hyperion).useNoAnimationSkill();
					if (!skillCount.contains(npc.getNpcId())) {
						skillCount.add(npc.getNpcId());
					}
					isDone3 = true;
				}
				if (skillId == 21416 && isDone3 && !isDone4){
					NpcShoutsService.getInstance().sendMsg(npc, messageId);
					SkillEngine.getInstance().getSkill(npc, skillId, 1, hyperion).useNoAnimationSkill();
					if (!skillCount.contains(npc.getNpcId())) {
						skillCount.add(npc.getNpcId());
					}
					isDone4 = true;
				}
                if (skillCount.size() == 4) {
                    stopInstance();
                }
            }
        }, 32000);
        skillTask.put(npc.getNpcId(), task);
    }

    private void stopInstance() {
        NpcShoutsService.getInstance().sendMsg(instance, 1401794, 0, false, 25, 0);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                NpcShoutsService.getInstance().sendMsg(instance, 1401909, 0, false, 25, 0);
                Npc hyperion = getNpc(231073);
                for (Player p : instance.getPlayersInside()) {
                    p.getController().onAttack(hyperion, p.getLifeStats().getMaxHp() + 1, true);
                }
                cancelResonatorTask();
                for (FastMap.Entry<Integer, Future<?>> e = skillTask.head(), end = skillTask.tail(); (e = e.getNext()) != end; ) {
                    if (e.getValue() != null && !e.getValue().isCancelled()) {
                        e.getValue().cancel(true);
                    }
                    despawnNpc(e.getKey());

                }
                skillTask.clear();
				despawnNpc(231073);
                spawn(730842, 129.46301f, 137.99736f, 112.17429f, (byte) 54);
            }
        }, 5000);
    }

    private void spawnResonators() {
        resonator = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
				if (getNpc(231092) != null) {
				startSkillTask((Npc) getNpc(231092), 21258, 1401791);
				}
				resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
						if (getNpc(231093) != null) {
						startSkillTask((Npc) getNpc(231093), 21382, 1401792);
						}
						resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
                            @Override
                            public void run() {
								if (getNpc(231094) != null) {
                                startSkillTask((Npc) getNpc(231094), 21384, 1401793);
								}
									resonator = ThreadPoolManager.getInstance().schedule(new Runnable() {
										@Override
										public void run() {
											if (getNpc(231095) != null) {
											startSkillTask((Npc) getNpc(231095), 21416, 1401794);
											}
										}
                                }, 40 * 1000);
                            }
                        }, 40 * 1000);
                    }
                }, 40 * 1000);
            }
        }, 40 * 1000, 40 * 1000);
    }

    private void resonatorSpawn(final int npcId, final float x, final float y, final float z, final float h) {
        cancelskillTask(npcId);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                spawn(npcId, x, y, z, (byte) h);
            }
        }, 40 * 1000);
    }

    private boolean isDeadGenerator1() {
        Npc Generator1 = getNpc(231087);
        Npc Generator2 = getNpc(231088);
        Npc Generator3 = getNpc(231089);
        if (isDead(Generator1) && isDead(Generator2) && isDead(Generator3)) {
            Npc idegenerator1 = getNpc(231074);
            if (idegenerator1 != null) {
                idegenerator1.getEffectController().removeEffect(21371);
            }
            return true;
        }
        return false;
    }

    private boolean isDeadGenerator2() {
        Npc Generator4 = getNpc(231075);
        Npc Generator5 = getNpc(231076);
        Npc Generator6 = getNpc(231077);
        if (isDead(Generator4) && isDead(Generator5) && isDead(Generator6)) {
            Npc idegenerator2 = getNpc(231078);
            if (idegenerator2 != null) {
                idegenerator2.getEffectController().removeEffect(21371);
            }
            return true;
        }
        return false;
    }

    private boolean isDeadGenerator3() {
        Npc Generator7 = getNpc(231083);
        Npc Generator8 = getNpc(231084);
        Npc Generator9 = getNpc(231085);
        if (isDead(Generator7) && isDead(Generator8) && isDead(Generator9)) {
            Npc idegenerator3 = getNpc(231082);
            if (idegenerator3 != null) {
                idegenerator3.getEffectController().removeEffect(21371);
            }
            return true;
        }
        return false;
    }

    private boolean isDeadGenerator4() {
        Npc Generator10 = getNpc(231079);
        Npc Generator11 = getNpc(231080);
        Npc Generator12 = getNpc(231081);
        if (isDead(Generator10) && isDead(Generator11) && isDead(Generator12)) {
            Npc idegenerator4 = getNpc(231086);
            if (idegenerator4 != null) {
                idegenerator4.getEffectController().removeEffect(21371);
            }
            return true;
        }
        return false;
    }

    private void removeProtection() {
        if (protection != 4) {
            return;
        }
        Npc hyperion = instance.getNpc(231073);
        if (hyperion != null) {
            sendMsg(1401796);
            despawnNpc(284437);
            hyperion.getEffectController().removeEffect(21254);
            getRandomTarget(hyperion);
            spawnResonators();
            NpcShoutsService.getInstance().sendMsg(instance, 1401790, 0, false, 25, 0);
        }
    }

    private void getRandomTarget(Npc hyperion) {
        List<Player> players = new ArrayList<Player>();
        for (Player player : instance.getPlayersInside()) {
            if (!PlayerActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, hyperion, 16)) {
                players.add(player);
            }
        }
        if (players.isEmpty()) {
            return;
        }
        SkillEngine.getInstance().getSkill(hyperion, 21241, 1, players.get(Rnd.get(0, players.size() - 1))).useNoAnimationSkill();
    }

    private void despawnNpc(int npcId) {
        Npc npc = getNpc(npcId);
        if (npc != null) {
            npc.getController().onDelete();
        }
    }

    private boolean isDead(Npc npc) {
        return (npc == null || npc.getLifeStats().isAlreadyDead());
    }
    
}