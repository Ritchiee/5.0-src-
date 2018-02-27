package com.aionemu.gameserver.services.ecfunctions.arena;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.utils3d.FFA3D;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;

public class ArenaService extends PVPManager {	
	
	private final Logger log = LoggerFactory.getLogger(ArenaService.class);
	
	private static final ArenaService service = new ArenaService();
	
	private List<Player> playerHolder = new FastList<Player>();
	
	private WorldMapInstance ArenaInstance;
	
	private FFA3D loc;
	private FFA3D [] spawnArray;
	
	private int worldNumber;
	private Player lastWinner;
	
    public static ArenaService getInstance(){
        return service;
    }
	public void Init() {
		announceTask(35);
	}
    private ScheduledFuture<?> announceTask(int delayInMinutes) {
    	return ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {	
    		@Override
    		public void run() {
    			PacketSendUtility.sendSpecMessage("Arena", "Solo arena is only for the Champions. Type '.arena' to test your skills now.");
    		}
    	}, delayInMinutes / 2 * 1000 * 60, delayInMinutes / 2 * 1000 * 60);  
    }    
    public void setLastWinner(Player lastWinner) {
    	this.lastWinner = lastWinner;
    }    
	private Player getLastWinner () {
		return this.lastWinner;
	}    
    private WorldMapInstance createInstance(int worldId, boolean withDoor) {
    	if (withDoor) {
    		WorldMapInstance Soloinstance = InstanceService.getNextAvailableSoloInstance(worldId, true);
    		return Soloinstance;
    	} else {
    		WorldMapInstance Soloinstance = InstanceService.getNextAvailableSoloInstance(worldId, false);
    		return Soloinstance;
    	}
    }    
	static FFA3D[] spawnTriniel = new FFA3D[]{ //Triniel arena 320090000 
  		new FFA3D(275.67032f, 285.79437f, 161.225f, 90),
  		new FFA3D(275.889f, 183.8111f, 161.97704f, 29),};
	static FFA3D[] spawnSanctum = new FFA3D[]{ ////Sanctum arena 310080000
  		new FFA3D(275.75375f, 285.39005f, 161.25763f, 90),
  		new FFA3D(275.9721f, 189.8644f, 161.06584f, 29),};
	static FFA3D[] spawnDanuar = new FFA3D[]{ //Danuar Sanctuary
  		new FFA3D(261f, 284f, 241f, 85),
  		new FFA3D(250f, 233f, 241f, 24),};
	static FFA3D[] spawnEstorate = new FFA3D[]{ //Estorate Bio Lab
  		new FFA3D(1316.73f, 1145.1937f, 51.996548f, 31),
  		new FFA3D(1316.3796f, 1197.33f, 51.996548f, 89),};
	static FFA3D[] spawnInfinity = new FFA3D[]{ //infinity shard
  		new FFA3D(117f, 116f, 131f, 17),
  		new FFA3D(114f, 153f, 113f, 107),};
	static FFA3D[] spawnTheobomos = new FFA3D[]{ //Theobomos labs	
  		new FFA3D(576f, 489f, 196f, 0),
  		new FFA3D(624f, 488f, 196f, 59),};	
	static FFA3D[] spawnLinkgate = new FFA3D[]{ //Linkgate
  		new FFA3D(261f, 284f, 241f, 85),
  		new FFA3D(250f, 233f, 241f, 24),};
	static FFA3D[] spawnRentus = new FFA3D[]{ //rentus base
  		new FFA3D(164.50769f, 424.03653f, 260.57123f, 112),
  		new FFA3D(211.57138f, 402.0738f, 260.55164f, 50),};
	static FFA3D[] spawnSauro1 = new FFA3D[]{ //sauro base
  		new FFA3D(480.7304f, 365.88965f, 182.2329f, 29),
  		new FFA3D(560.7437f, 487.2343f, 192.26617f, 59),};
	static FFA3D[] spawnSauro2 = new FFA3D[]{ //sauro base #2
  		new FFA3D(880.8239f,  889.8537f, 411.45676f, 119),
  		new FFA3D(919.31384f,  889.6376f, 411.4788f, 62),};	
	static FFA3D[] spawnStadium1 = new FFA3D[]{ // old map 300300000 Empyrean Crucible
  		new FFA3D(324.13043f, 349.06357f, 96.090904f, 119),
  		new FFA3D(370.16165f, 349.33316f, 96.091194f, 59),};
	static FFA3D[] spawnStadium2 = new FFA3D[]{ 
  		new FFA3D(1623.1039f, 129.34892f, 126.0f, 26),
  		new FFA3D(1644.2643f, 169.6943f, 126.0f, 73),};
	static FFA3D[] spawnStadium3 = new FFA3D[]{ 
  		new FFA3D(1784.601f, 773.9861f, 469.62326f, 31),
  		new FFA3D(1784.4885f, 820.46063f, 470.00174f, 90),};
	static FFA3D[] spawnStadium4 = new FFA3D[]{ 
  		new FFA3D(1760.0645f, 1743.8604f, 304.3797f, 17),
  		new FFA3D(1793.695f, 1779.4673f, 304.2167f, 78),};
	static FFA3D[] spawnStadium5 = new FFA3D[]{
  		new FFA3D(1268.9286f, 1725.4218f, 317.7411f, 5),
  		new FFA3D(1343.4878f, 1747.8689f, 317.3028f, 65),};
	static FFA3D[] spawnStadium6 = new FFA3D[]{  // new map 300320000 Crucible Challenge 		
  		new FFA3D(1808.5173f, 307.04434f, 469.25f, 54),
  		new FFA3D(1762.1791f, 315.2861f, 469.25f, 115),};
	static FFA3D[] spawnStadium7 = new FFA3D[]{ 
  		new FFA3D(1282.6403f, 237.95880f, 405.38037f, 60),
  		new FFA3D(1237.5205f, 237.87419f, 405.3801f, 3),};
	static FFA3D[] spawnStadium8 = new FFA3D[]{ 
  		new FFA3D(370.50717f, 349.40985f, 96.091194f, 59),
  		new FFA3D(323.11023f, 349.25412f, 96.090904f, 0),};	
	static FFA3D[] spawnStadium9 = new FFA3D[]{ 
  		new FFA3D(368.7469f, 1662.9462f, 96.659225f, 59),
  		new FFA3D(322.78876f, 1663.3391f, 95.9507f, 119),};	
	static FFA3D[] spawnStadium10 = new FFA3D[]{ 
  		new FFA3D(1293.6464f, 791.59686f, 436.64035f, 60),
  		new FFA3D(1250.4994f, 791.63f, 436.64008f, 0),};	
	static FFA3D[] spawnHarmony1 = new FFA3D[]{ //Amphitheater  300450000
  		new FFA3D(1876.0123f, 1711.7046f, 308.85144f, 45),
  		new FFA3D(1826.2347f, 1762.5052f, 308.85144f, 104),};
	static FFA3D[] spawnHarmony2 = new FFA3D[]{ //Burning Lava Caldera
  		new FFA3D(1575f, 347f, 202f, 38),
  		new FFA3D(1538f, 377f, 204f, 106),};
	static FFA3D[] spawnHarmony3 = new FFA3D[]{ //Ancient Sand Shrine
  		new FFA3D(533f, 1173f, 442f, 71),
  		new FFA3D(460f, 1105f, 448f, 19),};
	static FFA3D[] spawnDiscipline1 = new FFA3D[]{ //Plaza of Struggle  300360000 
  		new FFA3D(1822.8875f, 1737.0482f, 305.22974f, 0),
  		new FFA3D(1878.1543f, 1737.0363f, 305.24194f, 60),};
	static FFA3D[] spawnDiscipline2 = new FFA3D[]{ //Abyss Bridge
  		new FFA3D(349.0756f, 1182.2979f, 244.78984f, 43),
  		new FFA3D(249.59401f,1299.8933f, 248.3328f, 104),};
	static FFA3D[] spawnDiscipline3 = new FFA3D[]{ //Red Sand Arena
  		new FFA3D(1858.8259f, 1036.7157f, 337.74768f, 31),
  		new FFA3D(1855.2382f, 1075.332f, 338.083f, 89),};
	static FFA3D[] spawnChaos1 = new FFA3D[]{ //Plaza of Struggle  300350000 
  		new FFA3D(1834.2797f, 1712.1831f, 300.3545f, 24),
  		new FFA3D(1851.408f, 1753.2749f, 300.35474f, 82),};	
	static FFA3D[] spawnChaos2 = new FFA3D[]{ //Mumu Farm 
		new FFA3D(1293.7894f, 1079.9891f, 340.51547f, 119), 
		new FFA3D(1373.615f, 1076.5543f, 340.375f, 59),}; 
    static FFA3D[] spawnChaos3 = new FFA3D[]{ //Decay Garden   
		new FFA3D(672.8779f, 271.2494f, 512.6792f, 70), 
		new FFA3D(621.9829f, 246.58838f, 507.21323f, 9),}; 
    
    private void randomInstance () {
	   	Random r = new Random();
	   	int  i = r.nextInt(13) + 1;	   	
	   	if (i == 1) {
			worldNumber = 320090000; //Triniel Arena
			spawnArray = spawnTriniel;
			ArenaInstance = createInstance(worldNumber, false);		
	    } else if (i == 2 ) { 
			worldNumber = 310080000; //Sanctum Arena
			spawnArray = spawnSanctum;
			ArenaInstance = createInstance(worldNumber, false);
	    } else if (i == 3) {
			worldNumber = 301110000; //Danuar Sanctuary
			spawnArray = spawnDanuar;
			ArenaInstance = createInstance(worldNumber, false);		
	    } else if (i == 4 ) { 
	    	worldNumber = 301130000; //sauro base
			Random spawns = new Random();
		   	int spawn = spawns.nextInt(2) + 1;
		   	if (spawn == 1) {			
		   		spawnArray = spawnSauro1;
		   	} else if (spawn == 2) {
		   		spawnArray = spawnSauro2;
		   	}
			ArenaInstance = createInstance(worldNumber, false);
		} else if (i == 5) {
			worldNumber = 300250000; //Estorate Bio Lab
			spawnArray = spawnEstorate;
			ArenaInstance = createInstance(worldNumber, false);
		} else if (i == 6) {
			worldNumber = 300800000; //infinity shard
			spawnArray = spawnInfinity;
			ArenaInstance = createInstance(worldNumber, false);
		} else if (i == 7) {
			worldNumber = 310110000; //Theobomos labs		
			spawnArray = spawnTheobomos;
			ArenaInstance = createInstance(worldNumber, false);    
		} else if (i == 8) {
			worldNumber = 300280000; //rentus base	
			spawnArray = spawnRentus;
			ArenaInstance = createInstance(worldNumber, false);
		} else if (i == 9) {
			worldNumber = 300300000; // old map 300300000 Empyrean Crucible			
			Random spawns = new Random();
		   	int spawn = spawns.nextInt(5) + 1;
		   	if (spawn == 1) {
		   		spawnArray = spawnStadium1;
		   	} else if (spawn == 2){
		   		spawnArray = spawnStadium2;
		   	} else if (spawn == 3){
		   		spawnArray = spawnStadium3;
		   	} else if (spawn == 4){
		   		spawnArray = spawnStadium4;
		   	} else if (spawn == 5){
		   		spawnArray = spawnStadium5;
		   	} 
			ArenaInstance = createInstance(worldNumber, false);    
		} else if (i == 10) {
			worldNumber = 300320000; // new map 300320000 Crucible Challenge 	
			Random spawns = new Random();
		   	int spawn = spawns.nextInt(5) + 1; 
		   	if (spawn == 1) {
		   		spawnArray = spawnStadium6;
		   	} else if (spawn == 2){
		   		spawnArray = spawnStadium7;
		   	} else if (spawn == 3){
		   		spawnArray = spawnStadium8;
		   	} else if (spawn == 4){
		   		spawnArray = spawnStadium9;
		   	} else if (spawn == 5){
		   		spawnArray = spawnStadium10;
		   	} 
			ArenaInstance = createInstance(worldNumber, false);    
		} else if (i == 11) {
			worldNumber = 300450000; //Arena of Harmony 
			Random spawns = new Random();
		   	int spawn = spawns.nextInt(3) + 1; 
		   	if (spawn == 1) {
		   		spawnArray = spawnHarmony1; //Amphitheater
		   	} else if (spawn == 2){
		   		spawnArray = spawnHarmony2; //Burning Lava Caldera
		   	} else if (spawn == 3){
		   		spawnArray = spawnHarmony3; //Ancient Sand Shrine
		   	}		
			ArenaInstance = createInstance(worldNumber, true);   
			openDoors(ArenaInstance); 
		} else if (i == 12) { //Arena of Discipline
			worldNumber = 300360000; 
			Random spawns = new Random();
		   	int spawn = spawns.nextInt(3) + 1; 
		   	if (spawn == 1) {
		   		spawnArray = spawnDiscipline1; //Plaza of Struggle
		   	} else if (spawn == 2){
		   		spawnArray = spawnDiscipline2; //Abyss Bridge
		   	} else if (spawn == 3){
		   		spawnArray = spawnDiscipline3; //Red Sand Arena
		   	} 
			ArenaInstance = createInstance(worldNumber, true);
			openDoors(ArenaInstance);
		} else if (i == 13) { //Arena of Chaos
			worldNumber = 300350000; 
			Random spawns = new Random();
		   	int spawn = spawns.nextInt(3) + 1; 
		   	if (spawn == 1) {
		   		spawnArray = spawnChaos1; //Plaza of Struggle
		   	} else if (spawn == 2){
		   		spawnArray = spawnChaos2; //Mumu Farm
		   	} else if (spawn == 3){
		   		spawnArray = spawnChaos3; //Decayed Garden
		   	} 
			ArenaInstance = createInstance(worldNumber, true);
			openDoors(ArenaInstance);
		}
    }    
    /**
     * Register/Unregister 
     * 
     */
    public void register (Player player) {
    	if (playerHolder.size() == 0) {
    		playerHolder.add(player);    		
    		PacketSendUtility.sendSpecMessage("Arena",  String.format( "%s added to the waiting list. Type '.arena' to join now.", player.getName()));
    	} else if (playerHolder.size() == 1) {
    		playerHolder.add(player);
    		randomInstance();   
        	for (int i = 0; i < playerHolder.size(); i++) {
				Player p = playerHolder.get(i);
				if (p != player) {
					p.setEnemy(player);
					player.setEnemy(p);	
					log.info(String.format("[ARENA] > (%s, %s) Enter - %s", p.getName(), player.getName(), toLowercase(WorldMapType.getWorld(worldNumber).name())));
				} 
				loc = spawnLoc(loc, spawnArray, i);
				p.setBGLoc(worldNumber, (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h);
				TeleIn(p, worldNumber, ArenaInstance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h);
        	}        	
			playerHolder.clear();
    	} else if (playerHolder.size() == 2) {
    		PacketSendUtility.sendMessage(player,  String.format("Arena: Please wait and try again..", player.getName()));
    	}
    }    
    public void unRegister (Player player) {
    	for (int i = 0; i < playerHolder.size(); i++) {
    		Player p = playerHolder.get(i);
    		if (p == null || !p.isOnline()) {
                playerHolder.remove(i);
                i--;
                continue;
            }
            if (p.getObjectId() == player.getObjectId()) {
                playerHolder.remove(i);              
            }
            p.arenaWaiting = false;
    	}
    }    
    /**
     *  Teleport players
     * 
     */
    public void TeleIn(Player player, int worldId, int instanceId, float locX, float locY, float locZ, int locH) {
    	
    	xMorph(player);
    	if (player.isInArena()) {
        	TeleportService2.teleportTo(player, worldId, instanceId, (float) locX, (float) locY, (float) locZ, (byte) locH);
        } else {
        	resetStart(player);
	        TeleportService2.teleportTo(player, worldId, instanceId, (float) locX, (float) locY, (float) locZ, (byte) locH, TeleportAnimation.JUMP_ANIMATION);	        
        }
    }    
    public void TeleOut(Player player) {
    	
    	paralizePlayer(player, false);
        if (player.world() != 0) {
        	
        		TeleportService2.teleportTo(player, player.world(), player.locX(), player.locY(), player.locZ(), (byte) player.locH(), TeleportAnimation.JUMP_ANIMATION);
        		if (player == getLastWinner()) {	
    				log.info(String.format("[ARENA] > %s (Winner) Exit - %s", player.getName(), toLowercase(player.getPlayerClass().toString())));
    			} else {
    				log.info(String.format("[ARENA] > %s (Loser) Exit - %s", player.getName(), toLowercase(player.getPlayerClass().toString())));
    			}
        		player.clearPrevLoc();
        	
        	resetEnd(player);
        } else {
            TeleportService2.moveToBindLocation(player, true);
            resetEnd(player);
        }
    }
    /**
     * Set player preferences
     * 
     */    
    public void resetEnd(Player player) {
        player.clearBGLoc();
		if (player.getAccessLevel() > 1) {
			player.setInvul(true);
			player.setAdminNeutral(3);
			player.setAdminEnmity(0);
			player.setSeeState(CreatureSeeState.SEARCH10);
		}	
		player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
		HealPlayer(player, false, true);
	}
    public void resetStart(Player player) {   	

		player.setPrevLoc();
		
		player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
	  	player.setInArena(true);
	  	player.setCanWinArena(false);
		InstanceService.registerPlayerWithInstance(ArenaInstance, player);
		player.arenaWaiting = false;
		if (player.getAccessLevel() > 1) {
			player.setInvul(false);
			player.setAdminNeutral(0);
			player.unsetSeeState(CreatureSeeState.SEARCH10);
		}	
		HealPlayer(player, false, true);	
    }
}