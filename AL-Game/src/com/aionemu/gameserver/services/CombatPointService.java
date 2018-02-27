/**
 */
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;

import com.aionemu.gameserver.model.cp.PlayerCpEntry;
import com.aionemu.gameserver.model.cp.PlayerCpList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ESSENCE_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.SkillEnchantTemplate;
import com.aionemu.gameserver.skillengine.model.SkillLearnTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;



/**
 * @author Xnemonix
 */
public class CombatPointService implements StatOwner {
	private static final Logger log = LoggerFactory.getLogger(CombatPointService.class);
	
	 public void onPlayerLogin(Player player) {
		PlayerCpList playerCpList = player.getCpList(); 
		int level = player.getLevel();
		
		PacketSendUtility.sendPacket(player, new SM_ESSENCE_LIST(player, player.getCpList().getAllCps()));
		
		player.getGameStats().endEffect(this);
		
        for (PlayerCpEntry cpEntry : playerCpList.getEnchantSkillCps()) {
			int slotid = cpEntry.getSlotId();
			int cppoint = cpEntry.getCpPoint();
			
			SkillEnchantTemplate[] slotIds = DataManager.SKILL_ENCHANT_DATA.getTemplatesForGroup(slotid);

			for (int i = 0; i <= level; i++) {
	            
	            SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());  
	            for (SkillLearnTemplate template : skillTemplates) {
	            	if (template.getSkillGroup() != null){
	            		if (template.getSkillGroup().equals(slotIds[0].getSkillGroup())){
	                		int skillid = template.getSkillId();  
	                		PlayerSkillEntry skills = player.getSkillList().getSkillEntry(skillid);
	                		if (skills != null)
	                		skills.setSkillLvl(cppoint);
	                	}
	            	}
	            }
	        }
        
        }

		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		
}

	public void resetCp(Player player){
		PlayerCpList playerCpList = player.getCpList();
		int level = player.getLevel();
		
		player.getGameStats().endEffect(this);
		
		for (PlayerCpEntry cpEntry : playerCpList.getStatUpCps()) {
			int slotid = cpEntry.getSlotId();
			player.getCpList().setCpStat(player, slotid, 0);
		}
		for (PlayerCpEntry cpEntry : playerCpList.getLearnCps()) {
			int slotid = cpEntry.getSlotId();
			player.getCpList().setCpLearn(player, slotid, 0);
		}
		
		for (PlayerCpEntry cpEntry : playerCpList.getEnchantSkillCps()) {
			int slotid = cpEntry.getSlotId();

			player.getCpList().setCpEnchant(player, slotid, 0);
			SkillEnchantTemplate[] slotIds = DataManager.SKILL_ENCHANT_DATA.getTemplatesForGroup(slotid);
			for (int i = 0; i <= level; i++) {           
	            SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());

	            for (SkillLearnTemplate template : skillTemplates) {
	            	if (template.getSkillGroup() != null){
	            		if (template.getSkillGroup().equals(slotIds[0].getSkillGroup())){
	                		int skillid = template.getSkillId();  
	                		PlayerSkillEntry skills = player.getSkillList().getSkillEntry(skillid);
	                		if (skills !=null)
	                			skills.setSkillLvl(1);               
	                	}
	            	}
	            }
	        }
		}
		
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
		PacketSendUtility.sendPacket(player, new SM_ESSENCE_LIST(player, player.getCpList().getAllCps()));
        } 	
    
    public void executeCp(Player player) {
    	PlayerCpList playerCpList = player.getCpList(); 
		int level = player.getLevel();
		boolean Statup = false;
		
		player.getGameStats().endEffect(this);
		
		for (PlayerCpEntry cpEntry : playerCpList.getEnchantSkillCps()) {
			int slotid = cpEntry.getSlotId();
			int cppoint = cpEntry.getCpPoint();
			SkillEnchantTemplate[] slotIds = DataManager.SKILL_ENCHANT_DATA.getTemplatesForGroup(slotid);
			for (int i = 0; i <= level; i++) {
	            SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
	            for (SkillLearnTemplate template : skillTemplates) {
	            	if (template.getSkillGroup() != null){
	            		if (template.getSkillGroup().equals(slotIds[0].getSkillGroup())){
	                		int skillid = template.getSkillId();  
	                		int skilllevel = template.getSkillLevel();
	                		PlayerSkillEntry skills = player.getSkillList().getSkillEntry(skillid);
	                		skills.setSkillLvl(cppoint+skilllevel);
	                	}
	            	}
	            }
	        }
		}
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GIVE_CP_ENCHANT);
    	PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills())); // this not like
    	if (Statup) {
    		PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
    	}
    	PacketSendUtility.sendPacket(player, new SM_ESSENCE_LIST(player, player.getCpList().getAllCps()));
}
    public static final CombatPointService getInstance() {
        return SingletonHolder.instance;
    }
 
  @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final CombatPointService instance = new CombatPointService();
    } 
}

