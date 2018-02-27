/**
 * This file is part of Archsoft Development
 */
package com.aionemu.gameserver.model.cp;

import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author IceReaper, orfeo087, Avol, AEJTester
 */
public final class PlayerCpList implements CpList<Player> {

    private final Map<Integer, PlayerCpEntry> learnskills;
    private final Map<Integer, PlayerCpEntry> statUpCps;
    private final Map<Integer, PlayerCpEntry> enchantSkillCps;
    private final List<PlayerCpEntry> deletedCps;

    public PlayerCpList() {
        this.learnskills = new HashMap<Integer, PlayerCpEntry>();
        this.statUpCps = new HashMap<Integer, PlayerCpEntry>(0);
        this.enchantSkillCps = new HashMap<Integer, PlayerCpEntry>(0);
        this.deletedCps = new ArrayList<PlayerCpEntry>(0);
    }

    public PlayerCpList(List<PlayerCpEntry> skills) {
        this();
        for (PlayerCpEntry entry : skills) {
            if (entry.isStatup()) {
            	statUpCps.put(entry.getSlotId(), entry);
            } else if(entry.isEnchantSkill()) {
            	enchantSkillCps.put(entry.getSlotId(), entry);
            }else if(entry.isLearnSkill()){
            	learnskills.put(entry.getSlotId(), entry);
            }
        }
    }

    /**
     * Returns array with all cps
     */
    public PlayerCpEntry[] getAllCps() {
        List<PlayerCpEntry> allCps = new ArrayList<PlayerCpEntry>();
        allCps.addAll(learnskills.values());
        allCps.addAll(statUpCps.values());
        allCps.addAll(enchantSkillCps.values());
        return allCps.toArray(new PlayerCpEntry[allCps.size()]);
    }

    public PlayerCpEntry[] getLearnCps() {
        return learnskills.values().toArray(new PlayerCpEntry[learnskills.size()]);
    }
    
    public PlayerCpEntry[] getStatUpCps() {
        return statUpCps.values().toArray(new PlayerCpEntry[statUpCps.size()]);
    }
    
    public PlayerCpEntry[] getEnchantSkillCps() {
    	return enchantSkillCps.values().toArray(new PlayerCpEntry[enchantSkillCps.size()]);
    }
    
    public PlayerCpEntry[] getDeletedCps() {
        return deletedCps.toArray(new PlayerCpEntry[deletedCps.size()]);
    }
    
    public PlayerCpEntry getCpEntry(int slotId) {
        if (learnskills.containsKey(slotId)) {
            return learnskills.get(slotId);
        }
        
        if (statUpCps.containsKey(slotId)) {
            return statUpCps.get(slotId);
        }
        
        return enchantSkillCps.get(slotId);
    }
    
    public PlayerCpEntry getEnchantSkillCpEntry(int slotId) {
        return enchantSkillCps.get(slotId);
    }

  
    
    @Override
    public boolean setCpStat(Player player, int slotId, int cpPoint) {
        return addCp(player, slotId, cpPoint, true, false,  PersistentState.NEW);
    }
    
    @Override
    public boolean setCpEnchant(Player player, int slotId, int cpPoint) {
        return addCp(player, slotId, cpPoint, false, true, PersistentState.NEW); // NOACTION
    }
    
    @Override
    public boolean setCpLearn(Player player, int slotId, int cpPoint) {
        return addCp(player, slotId, cpPoint, false, false, PersistentState.NEW); // NOACTION
    }
    
   
    private synchronized boolean addCp(Player player, int slotId, int cpPoint, boolean isStatUp, boolean isEnchantSkill, PersistentState state) {
    	PlayerCpEntry existingCp = isStatUp ? statUpCps.get(slotId) : enchantSkillCps.get(slotId);

    	if(isStatUp){
    		existingCp = statUpCps.get(slotId);
    	}else if(isEnchantSkill){
    		existingCp =enchantSkillCps.get(slotId);
    	}else{
    		existingCp=learnskills.get(slotId);
    	}
    	
        if (existingCp != null) {
            existingCp.setCpPoint(cpPoint);
        } else {
            if (isStatUp) {
            	statUpCps.put(slotId, new PlayerCpEntry(slotId, true,  false, false, cpPoint, state));
            } else if (isEnchantSkill) {
            	enchantSkillCps.put(slotId, new PlayerCpEntry(slotId, false, true, false, cpPoint, state));
            }else{
            	learnskills.put(slotId, new PlayerCpEntry(slotId, false, false, true, cpPoint, state));
            }
        }
        if (player.isSpawned()) {
           // sendMessage(player, slotId, isNew);
        }
        return true;
    }

    @Override
    public boolean isCpPresent(int slotId) {
        return statUpCps.containsKey(slotId) || enchantSkillCps.containsKey(slotId)|| learnskills.containsKey(slotId);
    }

    @Override
    public int getCpPoint(int slotId) {
        if (statUpCps.containsKey(slotId)) {
            return statUpCps.get(slotId).getCpPoint();
        }
        
        if (learnskills.containsKey(slotId)) {
            return learnskills.get(slotId).getCpPoint();
        }
        
        return enchantSkillCps.get(slotId).getCpPoint();
    }

    @Override
    public synchronized boolean removeCp(int slotId) {
        PlayerCpEntry entry = enchantSkillCps.get(slotId);
        
        if (entry == null) {
            entry = statUpCps.get(slotId);
        }
        
        if (entry == null) {
            entry = enchantSkillCps.get(slotId);
        }
        
        if (entry != null) {
            entry.setPersistentState(PersistentState.DELETED);
            deletedCps.add(entry);
            learnskills.remove(slotId);
            statUpCps.remove(slotId);
            enchantSkillCps.remove(slotId);
        }
        return entry != null;
    }

    @Override
    public int size() {
        return learnskills.size()+ statUpCps.size() + enchantSkillCps.size();
    }

    /**
     * @param player
     * @param slotId
     *//*
    private void sendMessage(Player player, int slotId, boolean isNew) {
        switch (slotId) {
            case 30001:
            case 30002:
                PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(slotId), 1330005, false));
                break;
            case 30003:
                PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(slotId), 1330005, false));
                break;
            case 40001:
            case 40002:
            case 40003:
            case 40004:
            case 40005:
            case 40006:
            case 40007:
            case 40008:
            case 40009:
            case 40010:
                PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(slotId), 1330053, false));
                break;
            default:
                PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player.getSkillList().getSkillEntry(slotId), 1300050, isNew));
        }
    }*/
}
