package com.aionemu.gameserver.services.ecfunctions.customportals;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;


/**
 * @author Ghostfur
 *
 */

public class SiegePortal extends GeneralInstanceHandler {

	private static final SiegePortal service = new SiegePortal();
	
	private int worldID;
	private double ax, ay, az, ah;	
	private double ex, ey, ez, eh;
    
    public static SiegePortal getInstance(){
        return service;
    }
    public void Init() {	
    	setSiegeMAP();
    	spawnPortals();
    }
    private void setSiegeMAP() {  	
		ax = 1070.5579; //Gelkmaros
		ay = 1212.955;
		az = 359.99872;
		ah = 0;
		
		ex = 2372.2544;
		ey = 1029.7344;
		ez = 355.11945;
		eh = 0;
		worldID = 220070000;
		
    }
    
    public void TeleSiege (Player player) {	
    	if (player.getRace() == Race.ASMODIANS) {
    		TeleportService2.teleportTo(player, worldID, (float) ax, (float) ay, (float) az, (byte) ah);
    	} else {
    		TeleportService2.teleportTo(player, worldID, (float) ex, (float) ey, (float) ez, (byte) eh);
    	}	
    }   
    
    public int getWorldID () {
    	return worldID;
    }  
    
	private void spawnPortals() {
		/*
		 * Siege Loc in Panda/Sanctum
		 */
        SpawnTemplate spawnElySiege = SpawnEngine.addNewSpawn(210100000, 730663, 1440, 1265, 336, (byte) 58, 0);
        spawnElySiege.setMasterName("PVP-SIEGE");
        SpawnEngine.spawnObject(spawnElySiege, 1);
        AI2Engine.getInstance().setupAI("siege_portal", (Creature) spawnElySiege.getVisibleObject());
        ((AbstractAI) ((Creature) spawnElySiege.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawnAsmoSiege = SpawnEngine.addNewSpawn(220110000, 730662, 1803, 1948, 199, (byte) 59, 0);
        spawnAsmoSiege.setMasterName("PVP-SIEGE");
        SpawnEngine.spawnObject(spawnAsmoSiege, 1);
        AI2Engine.getInstance().setupAI("siege_portal", (Creature) spawnAsmoSiege.getVisibleObject());
        ((AbstractAI) ((Creature) spawnAsmoSiege.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);        
	}
}