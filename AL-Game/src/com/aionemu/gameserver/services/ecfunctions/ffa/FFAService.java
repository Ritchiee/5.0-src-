/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.services.ecfunctions.ffa;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.world.knownlist.Visitor;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.utils3d.FFA3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Ghostfur 
 * @author Goong
 */
public class FFAService extends PVPManager {
	
	private final Logger log = LoggerFactory.getLogger(FFAService.class);
	private static final FFAService service = new FFAService();
	
	private int worldNumber;
	private Player Killer;
	public String worldName;
	private FFA3D loc;

	public double x, y, z;
	public int h;
	
	private int randomNum = 0;
	
    private SpawnTemplate spawnEly;
    private SpawnTemplate spawnAsmo;
   
    private WorldMapInstance instance;
    private WorldMapInstance oldInstance;
    
    /**
	 * Instance ID 
	 */
    
    public int mediumMapID1 = 300200000; // HARAMEL
    public int mediumMapID2 = 300030000; // Nochsana Training Camp
    public int mediumMapID3 = 300700000; // HEXWAY
    public int mediumMapID4 = 320130000; // ADMASTRONGHOLD
    public int mediumMapID5 = 300110000; // CHANTRA DREDGION
    
    WorldMapInstance mediumInstance1, mediumInstance2, mediumInstance3, mediumInstance4, mediumInstance5;
	
    public static FFAService getInstance(){
        return service;
    }    
	public void Init() {		
		mediumInstance1 = createInstance(mediumMapID1, false);	
		mediumInstance2 = createInstance(mediumMapID2, true);	
		mediumInstance3 = createInstance(mediumMapID3, false);	
		mediumInstance4 = createInstance(mediumMapID4, false);
		mediumInstance5 = createInstance(mediumMapID5, false);
		instanceTask(80);
    	randomMediumInstance();
	} 
		
    /**
	 * Create Instance
	 */
	
	private WorldMapInstance createInstance(int worldId, boolean withDoor) {
    	if (withDoor) {
    		WorldMapInstance FFAInstance = InstanceService.getNextAvailableFFAInstance(worldId, true);
    		return FFAInstance;
    	} else {
    		WorldMapInstance FFAInstance = InstanceService.getNextAvailableFFAInstance(worldId, false);
    		return FFAInstance;
    	}
    }
	
	/**
	 * Medium posMedium / Spawn Locations
	 */
	
	  //Haramel 300200000
		static FFA3D[] posMedium1 = new FFA3D[]{
			new FFA3D(173.65646f, 20.642525f, 144.2249f, 48),
			new FFA3D(177.50137f, 54.633404f, 144.31184, 70),
			new FFA3D(65.592285f, 103.332886f, 139.21887f, 70),
			new FFA3D(147.95638f, 145.66255f, 144.28432f, 71),
			new FFA3D(222.8991f, 143.64795f, 137.24051f, 4),
			new FFA3D(196.22012, 224.88269f, 127.297775f, 70),
			new FFA3D(259.68097f, 214.05086, 88.951996, 10),};
		   //Nochsana Training Camp  300030000
		   static FFA3D[] posMedium2 = new FFA3D[]{
		   new FFA3D(513.0f, 668.0f, 331.0625f, 45),   
		   new FFA3D(372.4836f, 618.4905f, 359.0056f, 45),
		   new FFA3D(415.9786f, 524.7223f, 343.5721f, 45),
		   new FFA3D(311.99176f, 461.84393f, 362.86475f, 45),
           new FFA3D(396.91864f, 452.92197f, 365.7388f, 45), 
		   new FFA3D(385.7237f, 511.18246f, 356.83243f, 45),
		   new FFA3D(339.515f, 559.9779f, 347.93246f, 45),};
		//Hexway 300700000
		static FFA3D[] posMedium3 = new FFA3D[]{
			new FFA3D(672.7278f, 606.0872f, 321.21414f, 35),
			new FFA3D(671.12964f, 548.0154f, 335.33472f, 53),
			new FFA3D(573.454f, 574.33246f, 351.15314f, 20),
			new FFA3D(496.35623f, 607.3109f, 354.911f, 117),
			new FFA3D(281.05402f, 741.2451f, 364.93622f, 89),
			new FFA3D(329.2735f, 607.40625f, 362.06534f, 45),
			new FFA3D(502.37585f, 480.88242f, 352.70825f, 50),};
		  //Adma StrongHold 320130000
		static FFA3D[] posMedium4 = new FFA3D[]{
			new FFA3D(477.3009f, 829.53735f, 164.83f, 35),
			new FFA3D(412.77263f, 766.42993f, 157.3499f, 64),	
			new FFA3D(418.8922f, 734.38824f, 156.48161f, 53),
			new FFA3D(511.591f, 723.5784f, 158.40771f, 20),
			new FFA3D(499.9079f, 665.70013f, 162.78363f, 117),
			new FFA3D(445.4567f, 572.82654f, 162.26096f, 89),
			new FFA3D(430.8684f, 635.88104f, 163.3105f, 45),
			new FFA3D(405.17413f, 654.0031f, 168.9041f, 50),
			new FFA3D(355.19757f, 604.04974f, 168.9041f, 50),};
		 //Chantra Dredgion 300110000
		static FFA3D[] posMedium5 = new FFA3D[]{
			new FFA3D(485.1806f, 306.4124f, 402.696f, 35),
			new FFA3D(468.0433f, 583.24805f, 391.57822f, 64),
			new FFA3D(422.15012f, 467.68222f, 393.14484f, 53),
			new FFA3D(318.10907f, 403.9567f, 412.34094f, 20),
			new FFA3D(415.9837f, 567.6851f, 410.72897f, 117),
			new FFA3D(401.1345f, 693.84625f, 402.20755f, 89),
			new FFA3D(484.84583f, 771.5248f, 388.9195f, 45),
			new FFA3D(559.26385f, 698.91876f, 402.20535f, 45),
			new FFA3D(587.76495f, 540.60944f, 409.4695f, 45),
			new FFA3D(587.76495f, 540.60944f, 409.4695f, 45),
			new FFA3D(650.5343f, 410.8248f, 412.2131f, 45),
			new FFA3D(485.2726f, 859.8147f, 417.9915f, 50),};
			
		private void randomLoc () {
			if (worldNumber == mediumMapID1) { 
				loc = posMedium1[Rnd.get(posMedium1.length - 1)];
			} else if (worldNumber == mediumMapID2) {
				loc = posMedium2[Rnd.get(posMedium2.length - 1)];
			} else if (worldNumber == mediumMapID3) {
				loc = posMedium3[Rnd.get(posMedium3.length - 1)];
			} else if (worldNumber == mediumMapID4) {
				loc = posMedium4[Rnd.get(posMedium4.length - 1)];
			} else if (worldNumber == mediumMapID5) {
				loc = posMedium5[Rnd.get(posMedium5.length - 1)];
		}
	}
	private int getRandomNum() {
		return randomNum;
	}
	private void setRandomNum(int randomNum) {
		this.randomNum = randomNum;
	}
	private void randomMediumInstance() {	
	   	Random r = new Random();
	   	int  i = r.nextInt(5) + 1;
	   	if (getRandomNum() == 0) {
	   		setRandomNum(i);
	   	} else {
		   	while (i == randomNum) {
				i = r.nextInt(5) + 1;
			}
		   	setRandomNum(i);
	   	}
		if (i == 1) {
			worldName = "Haramel"; 
			worldNumber = 300200000;
			instance = mediumInstance1;
		 } else if (i == 2) {
			worldName = "Nochsana Training Camp"; 
			worldNumber = 300030000;
			instance = mediumInstance2; 
			openDoors(instance);
		 } else if (i == 3) {
		    worldName = "Hexway";
		    worldNumber = 300700000;
		    instance = mediumInstance3; 
		 } else if (i == 4) {
		    worldName = "AdmaStrongHold";
		    worldNumber = 320130000;
		    instance = mediumInstance4;
		 } else if (i == 5) {
		    worldName = "Chantra Dredgion";
		    worldNumber = 300110000;
		    instance = mediumInstance5;
	    } 
	} 	
	
    private void changeMap(final WorldMapInstance inst) {        	

    	randomMediumInstance();
    
		log.info("Rotating FFA Map: " + worldNumber + " - " + worldName +"");
        for (Player p : inst.getPlayersInside()) {
        	PacketSendUtility.sendPacket(p, new SM_ITEM_USAGE_ANIMATION(p.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(10), 0, 0));
        	PacketSendUtility.sendWhiteMessageOnCenter(p, "FFA: Rotating map. All players will be teleported in 10 seconds.");
    	 	paralizePlayer(p, true);
        }
        
        ThreadPoolManager.getInstance().schedule(new Runnable() { 
            @Override
            public void run() {
        		for (final Player p : inst.getPlayersInside()) {
        			paralizePlayer(p, false);
        			if (p.isOnline() && !p.getLifeStats().isAlreadyDead()) {        					
    						randomLoc();
    						PacketSendUtility.broadcastPacketAndReceive(p, new SM_ITEM_USAGE_ANIMATION(p.getObjectId(), 0, 0, 0, 1, 0));
    						TeleChangeMap(p);        								    						
    					} else {
    						randomLoc();
    						PacketSendUtility.broadcastPacketAndReceive(p, new SM_ITEM_USAGE_ANIMATION(p.getObjectId(), 0, 0, 0, 1, 0));
        					TeleChangeMap(p);   
    					}
    			}    
            }
        }, (int) TimeUnit.SECONDS.toMillis(10));  
    }   
    
   	private void TeleChangeMap (Player player) {
    	TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
	    HealPlayer(player, false, true);
   	}   	
    private ScheduledFuture<?> instanceTask(int delayInMinutes) {
    	return ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {	
    		@Override
    		public void run() {    
				oldInstance = instance;		
				deSpawnPortal(spawnAsmo);
				deSpawnPortal(spawnEly);
				changeMap(oldInstance);     	
    		}
    	}, delayInMinutes / 2 * 1000 * 60, delayInMinutes / 2 * 1000 * 60);  
    }    
    public int getFFASize () {
    	return getPlayerCountFFA(instance);
    }   	
    
    /**
	 * Teleport Players In
	 */
    
    public void TeleIn(Player player) {
    	randomLoc(); 
    	if (player.isInFFA()) {
    		TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
    		HealPlayer(player, false, true);
            AddProtection(player, 10 * 1000);
            player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
    	} else {
    	    if (instance != null && getPlayerCountFFA(instance) > 1) {
            	PacketSendUtility.sendSpecMessage("FFA", player.getName() + " has joined Free For All with " + getPlayerCountFFA(instance) +" other players.");
    	    } else {
            	PacketSendUtility.sendSpecMessage("FFA", player.getName() + " has joined Free For All.");
            }
        	TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);	        
        	resetStart(player);
    	}      		
    }    
    
    /**
	 * Teleport Players Out
	 */
    
    public void TeleOut(Player player) {	
    	resetEnd(player);
        if (player.world() != 0) {
            TeleportService2.teleportTo(player, player.world(), player.locX(), player.locY(), player.locZ(), (byte) player.locH(), TeleportAnimation.JUMP_ANIMATION_3);
        }
        else {
            TeleportService2.moveToBindLocation(player, true);
        }
        player.clearPrevLoc();    
    }       
  
    
    /**
     * TradeKillAlert Reducing When Using Same Mac
     */

	private void checkIfSameMac(Player winner, Player victim){
        String ip1 = winner.getClientConnection().getIP();
        String mac1 = winner.getClientConnection().getMacAddress();
        String ip2 = victim.getClientConnection().getIP();
        String mac2 = victim.getClientConnection().getMacAddress();
        if ((mac1 != null) && (mac2 != null)) {
            if ((ip1.equalsIgnoreCase(ip2)) && (mac1.equalsIgnoreCase(mac2))) {
                AuditLogger.info(winner, "[TradeKillAlert] You really need to check player " + winner.getName() + " and " + victim.getName() + ", They have same IP and MAC and possible they are Trade Killing in FFA, so please go and check in invisible! (MAC: " + mac1 + ").");
                int lose_ap = 30000;
                int lose_gp = 5000;
                int omegaId = 166020000;
                int tsId = 166030005;
                AbyssPointsService.addAp(winner, -lose_ap); // reducing ap from trade killers
                AbyssPointsService.addAp(victim, -lose_ap);

                AbyssPointsService.addAGp(winner, 0, -lose_gp); // reducing gp from trade killers
                AbyssPointsService.addAGp(victim, 0, -lose_gp);

                winner.getInventory().decreaseByItemId(omegaId, 1); // removing Omega from trade killers
                victim.getInventory().decreaseByItemId(omegaId, 1);

                victim.getInventory().decreaseByItemId(tsId, 1); //removeing TS from trade killers
                winner.getInventory().decreaseByItemId(tsId, 1);
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] You lost " + lose_ap + " AP for Trade Killing!"); // AP Lose msg
                PacketSendUtility.sendMessage(victim, "[TradeKillAlert] You lost " + lose_ap + " AP for Trade Killing!");
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] You lost " + lose_gp + " GP for Trade Killing!"); // GP Lose msg
                PacketSendUtility.sendMessage(victim, "[TradeKillAlert] You lost " + lose_gp + " GP for Trade Killing!");
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] You lost [item:" + omegaId + "] x 1 and [item:" + tsId + "] x 1 item(s) for Trade Killing!"); // item lose Item msg
                PacketSendUtility.sendMessage(victim, "[TradeKillAlert] You lost [item:" + omegaId + "] x 1 and [item:" + tsId + "] x 1 item(s) for Trade Killing!"); // item lose Item msg
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] Next Time Don't Trade Kill >_>");
                PacketSendUtility.sendMessage(victim, "[TradeKillAlert] Next Time Don't Trade Kill >_>");
                log.info("[FFA-TradeKill] Player " + winner.getName() + " killed " + victim.getName() + " and Have same IP and MAC!");
                return;
            }
            if (ip1.equalsIgnoreCase(ip2)) {
                AuditLogger.info(winner, "[TradeKillAlert] Possible chances that " + winner.getName() + " and " + victim.getName() + " are trade killing in FFA. They have same ip " + ip1 + ".");
                AuditLogger.info(winner, "[TradeKillAlert] If not, they are in some kinda cafe, with same network. OR USING SAME WTFAST Connection!!");
            }
        }
    }
  
    /**
     * On die Section & Rewards
     */
    
	    
	 public void onDead(final Player player, Creature lastAttacker) {
	      PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
	      PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));    	
	  	  onReward(player, lastAttacker);
	 	}    
	 
	public void onRevive(Player player) {      		
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);		
		if (player.isInFFA()) {
			FFAService.getInstance().TeleIn(player); 
		} else {
        	TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
		}
	}
	 
	public void onReward(Player victim, Creature lastAttacker) {	
    	if (lastAttacker.getActingCreature() instanceof Player && victim != lastAttacker) {    	
    		Player winner;
    		Killer =  victim.getAggroList().getMostPlayerDamage();	
			if (Killer.getLifeStats().isAlreadyDead()) {
				winner = (Player) lastAttacker;	
				checkIfSameMac(winner, victim);
			} else {
				winner = Killer;
			}			
			AbyssPointsService.addAp(winner, 5000);
			AbyssPointsService.addAGp(winner, 0, 100);
			winner.setSpecialKills();
			checkIfSameMac(winner, victim);
			sendSpecMessage("FFA", String.format("%s has slain %s..ouch \uE07A!", winner.getName(), victim.getName()), instance); 
	        throwStreakAnnouncement(winner);
        }
    }	
	
	 private void throwStreakAnnouncement(Player winner){		        
	        if(winner.getSpecialKills() == 5) {   	
            PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " is now on a Killing Spree! (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll());
	        } else if (winner.getSpecialKills() == 10) {
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " is now on Rampage! (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll());  
	        } else if (winner.getSpecialKills() == 15) {
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " is now Dominating! (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll());  
	        } else if (winner.getSpecialKills() == 20) {
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " Unstoppable! (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll()); 	 
	        } else if (winner.getSpecialKills() == 25) {
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " CHUUCHUU MUTHAFAKAAASS! ", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll()); 	 
	        } else if (winner.getSpecialKills() == 30) {
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " is now Getting Crazzyyy!!! " + winner.getSpecialKills() + " fucking kills?? (+35 HP)", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll()); 	 
	        } else if (winner.getSpecialKills() == 35) {
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll());  
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " is now GODLIKE!!! ", ChatType.BRIGHT_YELLOW_CENTER, false); 
	        } else if (winner.getSpecialKills() == 40) {
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll());  
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " is now on WICKED SICKKKKKK!!! ", ChatType.BRIGHT_YELLOW_CENTER, false); 
	        } else if (winner.getSpecialKills() == 45) {
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll()); 	 
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " Really knows how to kill players!!!!!! ", ChatType.BRIGHT_YELLOW_CENTER, false); 
	        } else if (winner.getSpecialKills() == 50) {
            ItemService.addItem(winner, 166030005, 1);			
			InGameShopEn.getInstance().addToll(winner, 10);
			PacketSendUtility.sendMessage(winner, "You have receive 10 tolls. Totals: "+ winner.getPlayerAccount().getToll());  
        	PacketSendUtility.sendAnnounceMessage("[FFA] "+ winner.getName() + " IS NOW A TRUE PVP FIGHTER!!!!!! ", ChatType.BRIGHT_YELLOW_CENTER, false); 
            TeleIn(winner);
        } 
    }  
	 
    private void resetEnd(Player player) {
        player.setInFFA(false);
        AddProtection(player, 10 * 1000);
        player.getController().cancelCurrentSkill();
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        if (player.getAccessLevel() > 4) {
			player.setInvul(true);
			player.setAdminNeutral(3);
			player.setAdminEnmity(0);
			player.setSeeState(CreatureSeeState.SEARCH10);
		}	
		HealPlayer(player, false, true);
       	log.info(String.format("[FFA] %s Exit - Killed: %s", player.getName(), player.getSpecialKills()));
       	player.setZeroSpecialKills();
       	//PacketSendUtility.sendSpecMessage("FFA", player.getName() + " has left the battle.");
    }
    
    private void resetStart(Player player) {
    	player.setInFFA(true);
    	player.setZeroSpecialKills();
    	InstanceService.registerPlayerWithInstance(instance, player);
    	AddProtection(player, 10 * 1000);
    	if (player.getAccessLevel() > 4) {
			player.setInvul(false);
			player.setAdminNeutral(0);
			player.unsetSeeState(CreatureSeeState.SEARCH10);
		}	
    	HealPlayer(player, false, true);
    }
    

    public void announceNormalKillMsg(final Player winner,final Player victim, final String msg){
        instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player object) {
            	PacketSendUtility.sendSpecMessage("FFA", winner.getName() + " " + msg + " " + victim.getName());
            }
        });
    }
}

	