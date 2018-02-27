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
 * @author Nimwey
 *
 */

public class RechargerPortal extends GeneralInstanceHandler {

	private static final RechargerPortal service = new RechargerPortal();
	
	private int worldID;
	private double ax, ay, az, ah;	
	private double ex, ey, ez, eh;
    
    public static RechargerPortal getInstance(){
        return service;
    }
    public void Init() {	
    	setRechargerMAP();
    	spawnPortals();
    }
    private void setRechargerMAP() {  	
		ax = 367.423 ; //Enshar
		ay = 847.24634;
		az = 251.7896;
		ah = 0;
		
		ex = 367.423 ;
		ey = 847.24634;
		ez = 251.7896;
		eh = 0;
		worldID = 220080000;
		
    }
    
    public void TeleRecharger (Player player) {	
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
		 * Recharger Loc in Panda/Sanctum
		 */
        SpawnTemplate spawnElyRecharger = SpawnEngine.addNewSpawn(210100000, 730663, 1433, 1266, 336, (byte) 58, 0);
        spawnElyRecharger.setMasterName("RECHARGER");
        SpawnEngine.spawnObject(spawnElyRecharger, 1);
        AI2Engine.getInstance().setupAI("recharger_portal", (Creature) spawnElyRecharger.getVisibleObject());
        ((AbstractAI) ((Creature) spawnElyRecharger.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawnAsmoRecharger = SpawnEngine.addNewSpawn(220110000, 730662, 1793, 1946, 199, (byte) 59, 0);
        spawnAsmoRecharger.setMasterName("RECHARGER");
        SpawnEngine.spawnObject(spawnAsmoRecharger, 1);
        AI2Engine.getInstance().setupAI("recharger_portal", (Creature) spawnAsmoRecharger.getVisibleObject());
        ((AbstractAI) ((Creature) spawnAsmoRecharger.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);        
	}
}