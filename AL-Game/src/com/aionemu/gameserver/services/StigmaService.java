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
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Credits goes to all Open Source Core Developer Groups listed below
 * Please do not change here something, ragarding the developer credits, except the "developed by XXXX".
 * Even if you edit a lot of files in this source, you still have no rights to call it as "your Core".
 * Everybody knows that this Emulator Core was developed by Aion Lightning 
 * @-Aion-Unique-
 * @-Aion-Lightning
 * @Aion-Engine
 * @Aion-Extreme
 * @Aion-NextGen
 * @Aion-Core Dev.
 */
package com.aionemu.gameserver.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.templates.item.RequireSkill;
import com.aionemu.gameserver.model.templates.item.Stigma;
import com.aionemu.gameserver.model.templates.item.Stigma.StigmaSkill;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author ATracer
 * @modified cura
 * @updated ever & Kill3r 4.8
 */
public class StigmaService {

    private static final Logger log = LoggerFactory.getLogger(StigmaService.class);
    /**
     * @param player
     * @param resultItem
     * @param slot
     * @return
     */
    public static boolean notifyEquipAction(Player player, Item resultItem, long slot) {
    	int StigmaEnchantLvl = 1;
    	if(resultItem.getEnchantLevel() <=0){
    		StigmaEnchantLvl = 1;
    	}else{
    		StigmaEnchantLvl = resultItem.getEnchantLevel() + 1;
    	}
    	
        if (resultItem.getItemTemplate().isStigma()) {
            if (ItemSlot.isRegularStigma(slot)) {
                // check the number of stigma wearing
                if (getPossibleStigmaCount(player) <= player.getEquipment().getEquippedItemsRegularStigma().size()) {
                    AuditLogger.info(player, "Possible client hack stigma count big , StigmaCoutPossible :" + getPossibleStigmaCount(player) + " Stigma Equiped Count: " + player.getEquipment().getEquippedItemsRegularStigma().size());
                    return false;
                }
            } else if (ItemSlot.isAdvancedStigma(slot)) {
                // check the number of advanced stigma wearin
                if (getPossibleAdvencedStigmaCount(player) <= player.getEquipment().getEquippedItemsAdvencedStigma().size()) {
                    AuditLogger.info(player, "Possible client hack advanced stigma count big :O");
                    return false;
                }
            } else if (ItemSlot.isMajorStigma(slot)) {
                // check the number of major stigma wearing
                if (getPossibleMajorStigmaCount(player) <= player.getEquipment().getEquippedItemsMajorStigma().size()) {
                    AuditLogger.info(player, "Possible client hack advanced stigma count big :O");
                    return false;
                }
            }

            if (resultItem.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass()) == false) {
                AuditLogger.info(player, "Possible client hack not valid for class.");
                return false;
            }

            // You cannot equip 2 stigma skills at 1 slot , was possible before.. o.o
            if (!StigmaService.isPossibleEquippedStigma(player, resultItem)) {
                AuditLogger.info(player, "Player tried to get Multiple Stigma's from One Stigma Stone!");
                return false;
            }

            Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();

            if (stigmaInfo == null) {
                log.warn("Stigma info missing for item: " + resultItem.getItemTemplate().getTemplateId());
                return false;
            }

            int kinahCount = stigmaInfo.getKinah();
            if (player.getInventory().getKinah() < kinahCount) {
                AuditLogger.info(player, "Possible client hack kinah count low.");
                return false;
            }

            if (!player.getInventory().tryDecreaseKinah(kinahCount)){
                return false;
            }

            if (resultItem.getItemId() == 140000001 || resultItem.getItemId() == 140000002  ||
            	resultItem.getItemId() == 140000003 || resultItem.getItemId() == 140000004 ){
            	player.getSkillList().addStigmaSkill(player, stigmaInfo.getSkills(), true);

                List<Integer> sStigma = player.getEquipment().getEquippedItemsAllStigmaIds();
                sStigma.add(resultItem.getItemId()); // The last item ur about to add is not in getEquippedItemsAllStigma , so adding manual

            }else{
            
            HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(stigmaInfo.getSkills().get(0).getSkillId()).getStack());
            Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
            treeMap.putAll(realSkillId);
            NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
            int skillId=0;
            for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                if (player.getLevel() >= entry.getKey()){
                	skillId = entry.getValue();
                	if (realSkillId != null)
                    	player.getSkillList().addStigmaSkill(player, skillId, StigmaEnchantLvl);
                    }
                }           
            List<Integer> sStigma = player.getEquipment().getEquippedItemsAllStigmaIds();
            sStigma.add(resultItem.getItemId()); // The last item ur about to add is not in getEquippedItemsAllStigma , so adding manual

            checkForLinkStigmaAvailable(player, sStigma);
            }
        }
        return true;
    }

    /**
     * @param player
     * @param resultItem
     * @return
     */
    public static boolean notifyUnequipAction(Player player, Item resultItem) {
        if (resultItem.getItemTemplate().isStigma()) {
            Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
            int itemId = resultItem.getItemId();
            Equipment equipment = player.getEquipment();
            
            int StigmaEnchantLvl = 1;
        	if(resultItem.getEnchantLevel() <=0){
        		StigmaEnchantLvl = 1;
        	}else{
        		StigmaEnchantLvl = resultItem.getEnchantLevel() + 1;
        	}
            
            if (itemId == 140000007 || itemId == 140000005) {
                if (equipment.hasDualWeaponEquipped(ItemSlot.LEFT_HAND)) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STIGMA_CANNT_UNEQUIP_STONE_FIRST_UNEQUIP_CURRENT_EQUIPPED_ITEM);
                    return false;
                }
            }
            
            for (Item item : player.getEquipment().getEquippedItemsAllStigma()) {
                Stigma si = item.getItemTemplate().getStigma();
                if (resultItem == item || si == null) {
                    continue;
                }

                for (StigmaSkill sSkill : stigmaInfo.getSkills()) {
                    for (RequireSkill rs : si.getRequireSkill()) {
                        if (rs.getSkillIds().contains(sSkill.getSkillId())) {
                            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300410, new DescriptionId(resultItem.getItemTemplate().getNameId()), new DescriptionId(item.getItemTemplate().getNameId())));
                            return false;
                        }
                    }
                }
            }
            
            if (resultItem.getItemId() == 140000001 || resultItem.getItemId() == 140000002  ||
            	resultItem.getItemId() == 140000003 || resultItem.getItemId() == 140000004 ){
            	
            	 for (StigmaSkill sSkill : stigmaInfo.getSkills()) {
                     int nameId = DataManager.SKILL_DATA.getSkillTemplate(sSkill.getSkillId()).getNameId();
                     PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(nameId)));

                     // remove skill
                     if ((player.getEquipment().getEquippedItemsRegularStigma().size() < 6) && (player.getLinkedSkill() != 0)) {
                         SkillLearnService.removeLinkedSkill(player, player.getLinkedSkill());
                         SkillLearnService.removeSkill(player, player.getLinkedSkill());
                     } else {
                         SkillLearnService.removeSkill(player, sSkill.getSkillId());
                     }
                     // remove effect
                     player.getEffectController().removeEffect(sSkill.getSkillId());
                 }
            }else{
            
            
            HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(stigmaInfo.getSkills().get(0).getSkillId()).getStack());
            Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
            treeMap.putAll(realSkillId);
            NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
            int skillId=0;
            for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                	skillId = entry.getValue();
                	if (realSkillId != null)
                		SkillLearnService.removeStigmaSkill(player,skillId, StigmaEnchantLvl);
	                	player.getEffectController().removeEffect(skillId);
	                	int nameId = DataManager.SKILL_DATA.getSkillTemplate(skillId).getNameId();
	                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(nameId)));
                }
            SkillLearnService.removeLinkedSkill(player, player.getLinkedSkill());
            }

        }
        return true;
    }

    /**
     * @param player
     */
    public static void onPlayerLogin(Player player) {
        List<Item> equippedItems = player.getEquipment().getEquippedItemsAllStigma();
        List<Integer> equippedStigmaId =  player.getEquipment().getEquippedItemsAllStigmaIds();
        
        for (Item item : equippedItems) { // All Equipped Items are Stigmas
        	
        	int StigmaEnchantLvl = 1;
        	if(item.getEnchantLevel() <=1){
        		StigmaEnchantLvl = 1;
        	}else{
        		StigmaEnchantLvl = item.getEnchantLevel() + 1;
        	}
        	
            if (item.getItemTemplate().isStigma()) {
                Stigma stigmaInfo = item.getItemTemplate().getStigma();
                
                if (stigmaInfo == null) {
                    log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
                    return;
                }
                
                HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(stigmaInfo.getSkills().get(0).getSkillId()).getStack());
                Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                treeMap.putAll(realSkillId);
                NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                int skillId=0;
                for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                    if (player.getLevel() >= entry.getKey()){
                    	skillId = entry.getValue();
                    	if (realSkillId != null)
                        	player.getSkillList().addStigmaSkill(player, skillId, StigmaEnchantLvl);
                    }
                }
                
            }
        }

        for (Item item : equippedItems) {
            if (item.getItemTemplate().isStigma()) {
                if (!isPossibleEquippedStigma(player, item)) {
                    AuditLogger.info(player, "Possible client hack stigma count big :O");
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                }

                Stigma stigmaInfo = item.getItemTemplate().getStigma();

                if (stigmaInfo == null) {
                    log.warn("Stigma info missing for item: " + item.getItemTemplate().getTemplateId());
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                }

                if (item.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass()) == false) {
                    AuditLogger.info(player, "Possible client hack not valid for class.");
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                }
            }
        }
        
        checkForLinkStigmaAvailable(player, equippedStigmaId);
    }

    /**
     * Get the number of available Stigma
     *
     * @param player
     * @return
     */
    private static int getPossibleStigmaCount(Player player) {
        if (player == null || player.getLevel() < 20) {
            return 0;
        }

        if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
            return 3;
        }

        /*
         * Stigma Quest Elyos: 1929, Asmodians: 2900
         */
        boolean isCompleteQuest = false;

        if (player.getRace() == Race.ELYOS) {
            isCompleteQuest = player.isCompleteQuest(1929)
                    || (player.getQuestStateList().getQuestState(1929).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(1929).getQuestVars().getQuestVars() == 98);
        } else {
            isCompleteQuest = player.isCompleteQuest(2900)
                    || (player.getQuestStateList().getQuestState(2900).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(2900).getQuestVars().getQuestVars() == 99);
        }

        int playerLevel = player.getLevel();

        if (isCompleteQuest) {
            if (playerLevel <= 20) {
                return 1;
            } else if (playerLevel <= 30) {
                return 2;
            } else if (playerLevel <= 40) {
                return 3;
            } else {
                return 3;
            }
        }
        return 0;
    }

    /**
     * Get the number of available Advenced Stigma
     *
     * @param player
     * @return
     */
    private static int getPossibleAdvencedStigmaCount(Player player) {
        if (player == null || player.getLevel() < 45) {
            return 0;
        }

        if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
            return 2;
        }

        /*
         * Stigma Quest Elyos: 1929, Asmodians: 2900
         */
        boolean isCompleteQuest = false;

        if (player.getRace() == Race.ELYOS) {
            isCompleteQuest = player.isCompleteQuest(1929)
                    || (player.getQuestStateList().getQuestState(1929).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(1929).getQuestVars().getQuestVars() == 98);
        } else {
            isCompleteQuest = player.isCompleteQuest(2900)
                    || (player.getQuestStateList().getQuestState(2900).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(2900).getQuestVars().getQuestVars() == 99);
        }

        int playerLevel = player.getLevel();

        if (isCompleteQuest) {
            if (playerLevel <= 45) {
                return 1;
            } else if (playerLevel <= 50) {
                return 2;
            } else {
                return 2;
            }
        }
        return 0;
    }

    /**
     * Get the number of available Major Stigma
     *
     * @param player
     * @return
     */
    private static int getPossibleMajorStigmaCount(Player player) {
        if (player == null || player.getLevel() < 55) {
            return 0;
        }

        if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
            return 1;
        }

        /*
         * Stigma Quest Elyos: 1929, Asmodians: 2900
         */
        boolean isCompleteQuest = false;

        if (player.getRace() == Race.ELYOS) {
            isCompleteQuest = player.isCompleteQuest(1929)
                    || (player.getQuestStateList().getQuestState(1929).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(1929).getQuestVars().getQuestVars() == 98);
        } else {
            isCompleteQuest = player.isCompleteQuest(2900)
                    || (player.getQuestStateList().getQuestState(2900).getStatus() == QuestStatus.START && player.getQuestStateList().getQuestState(2900).getQuestVars().getQuestVars() == 99);
        }

        int playerLevel = player.getLevel();

        if (isCompleteQuest) {
            if (playerLevel >= 55) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Stigma is a worn check available slots
     *
     * @param player
     * @param item
     * @return
     */
    private static boolean isPossibleEquippedStigma(Player player, Item item) {
        if (player == null || (item == null || !item.getItemTemplate().isStigma())) {
            return false;
        }

        long itemSlotToEquip = item.getEquipmentSlot();

        // Stigma
        if (ItemSlot.isRegularStigma(itemSlotToEquip)) {
            int stigmaCount = getPossibleStigmaCount(player);

            if (stigmaCount > 0) {
                if (stigmaCount == 1) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 2) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask() || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 3) {
                	 return true;
                
                }
            }
        } // Advenced Stigma
        else if (ItemSlot.isAdvancedStigma(itemSlotToEquip)) {
            int advStigmaCount = getPossibleAdvencedStigmaCount(player);

            if (advStigmaCount > 0) {
                if (advStigmaCount == 1) {
                    if (itemSlotToEquip == ItemSlot.ADV_STIGMA1.getSlotIdMask()) {
                        return true;
                    }
                } else if (advStigmaCount == 2) {           	
                        return true;
                }
            }
        } // Major Stigma
        else if (ItemSlot.isMajorStigma(itemSlotToEquip)) {
            int majStigmaCount = getPossibleMajorStigmaCount(player);
                if (majStigmaCount == 1) {
                    if (itemSlotToEquip == ItemSlot.MAJ_STIGMA.getSlotIdMask()) {
                        return true;
                    }
                }

        }
        return false;
    }
    
    public static void onPlayerLogout(Player player){
      switch (player.getPlayerClass()){
      case GLADIATOR: 
        player.getSkillList().removeSkill(641);
        player.getSkillList().removeSkill(727);
        player.getSkillList().removeSkill(657);
        break;
      case TEMPLAR: 
        player.getSkillList().removeSkill(2919);
        player.getSkillList().removeSkill(2918);
        player.getSkillList().removeSkill(2915);
        break;
      case ASSASSIN: 
        player.getSkillList().removeSkill(3326);
        player.getSkillList().removeSkill(3239);
        player.getSkillList().removeSkill(3242);
        break;
      case RANGER: 
        player.getSkillList().removeSkill(1006);
        player.getSkillList().removeSkill(936);
        player.getSkillList().removeSkill(1061);
        break;
      case SORCERER: 
        player.getSkillList().removeSkill(1340);
        player.getSkillList().removeSkill(1540);
        player.getSkillList().removeSkill(1418);
        break;
      case SPIRIT_MASTER: 
        player.getSkillList().removeSkill(3541);
        player.getSkillList().removeSkill(3549);
        player.getSkillList().removeSkill(3849);
        break;
      case CLERIC: 
        player.getSkillList().removeSkill(3932);
        player.getSkillList().removeSkill(4167);
        player.getSkillList().removeSkill(3906);
        break;
      case CHANTER: 
        player.getSkillList().removeSkill(1907);
        player.getSkillList().removeSkill(1901);
        player.getSkillList().removeSkill(1904);
        break;
      case RIDER: 
        player.getSkillList().removeSkill(2852);
        player.getSkillList().removeSkill(2861);
        player.getSkillList().removeSkill(2849);
        break;
      case GUNNER: 
        player.getSkillList().removeSkill(2368);
        player.getSkillList().removeSkill(2371);
        player.getSkillList().removeSkill(2380);
        break;
      case BARD: 
        player.getSkillList().removeSkill(4483);
        player.getSkillList().removeSkill(4474);
        player.getSkillList().removeSkill(4564);
        break;
      }
    }
    
    public static void checkForLinkStigmaAvailable(Player player, List<Integer> sStigma) {
        boolean hasInert = false;

        for (Integer in : sStigma){ // if Inert Stigma socketed, Cannot get Link
            ItemTemplate it = DataManager.ITEM_DATA.getItemTemplate(in);
            if (it.getName().contains("(Inert)")){
                hasInert = true;
            }
        }

        switch (player.getPlayerClass()) {
            case GLADIATOR:
                if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001119) && sStigma.contains(140001106) && sStigma.contains(140001108) ||
                    	sStigma.contains(140001119) && sStigma.contains(140001106) && sStigma.contains(140001107) ||
                    	sStigma.contains(140001119) && sStigma.contains(140001108) && sStigma.contains(140001107)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(641).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(641).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001118) && sStigma.contains(140001104) && sStigma.contains(140001103) ||
                    	sStigma.contains(140001118) && sStigma.contains(140001104) && sStigma.contains(140001105) ||
                    	sStigma.contains(140001118) && sStigma.contains(140001103) && sStigma.contains(140001105)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(727).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(727).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(657).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(657).getName(), 1, player.getName()));
                    }
                }
                return;
            case TEMPLAR:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001134) && sStigma.contains(140001122) && sStigma.contains(140001120) ||
                    	sStigma.contains(140001134) && sStigma.contains(140001122) && sStigma.contains(140001125) ||
                    	sStigma.contains(140001119) && sStigma.contains(140001120) && sStigma.contains(140001125)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2919).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        @SuppressWarnings("unused")
						NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2919).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001135) && sStigma.contains(140001123) && sStigma.contains(140001124) ||
                    	sStigma.contains(140001135) && sStigma.contains(140001123) && sStigma.contains(140001121) ||
                    	sStigma.contains(140001135) && sStigma.contains(140001124) && sStigma.contains(140001121)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2918).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2918).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2915).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2915).getName(), 1, player.getName()));
                    }
                }
                return;
            case ASSASSIN:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001152) && sStigma.contains(140001138) && sStigma.contains(140001139) ||
                    	sStigma.contains(140001152) && sStigma.contains(140001138) && sStigma.contains(140001141) ||
                    	sStigma.contains(140001152) && sStigma.contains(140001139) && sStigma.contains(140001141)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3236).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3236).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001151) && sStigma.contains(140001136) && sStigma.contains(140001140) ||
                    	sStigma.contains(140001151) && sStigma.contains(140001136) && sStigma.contains(140001137) ||
                    	sStigma.contains(140001151) && sStigma.contains(140001140) && sStigma.contains(140001137)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3239).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3239).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3242).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3242).getName(), 1, player.getName()));
                    }
                }
                return;
            case RANGER:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001172) && sStigma.contains(140001155) && sStigma.contains(140001157) ||
                    	sStigma.contains(140001172) && sStigma.contains(140001155) && sStigma.contains(140001153) ||
                    	sStigma.contains(140001172) && sStigma.contains(140001157) && sStigma.contains(140001153)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1006).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1006).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001173) && sStigma.contains(140001154) && sStigma.contains(140001158) ||
                    	sStigma.contains(140001173) && sStigma.contains(140001154) && sStigma.contains(140001156) ||
                    	sStigma.contains(140001173) && sStigma.contains(140001158) && sStigma.contains(140001156)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(936).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(936).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1061).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1061).getName(), 1, player.getName()));
                    }
                }
                return;
            case SORCERER:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001191) && sStigma.contains(140001174) && sStigma.contains(140001181) ||
                    	sStigma.contains(140001191) && sStigma.contains(140001174) && sStigma.contains(140001178) ||
                    	sStigma.contains(140001191) && sStigma.contains(140001181) && sStigma.contains(140001178)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1340).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1340).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001192) && sStigma.contains(140001176) && sStigma.contains(140001177) ||
                    	sStigma.contains(140001192) && sStigma.contains(140001176) && sStigma.contains(140001184) ||
                    	sStigma.contains(140001192) && sStigma.contains(140001177) && sStigma.contains(140001184) ||
                    	sStigma.contains(140001192) && sStigma.contains(140001176) && sStigma.contains(140001185) ||
                    	sStigma.contains(140001192) && sStigma.contains(140001177) && sStigma.contains(140001185)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1540).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1540).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1418).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1418).getName(), 1, player.getName()));
                    }
                }
                return;
            case SPIRIT_MASTER:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001209) && sStigma.contains(140001195) && sStigma.contains(140001193) ||
                    	sStigma.contains(140001209) && sStigma.contains(140001195) && sStigma.contains(140001194) ||
                    	sStigma.contains(140001209) && sStigma.contains(140001193) && sStigma.contains(140001194)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3541).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3541).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001210) && sStigma.contains(140001199) && sStigma.contains(140001158) ||
                    	sStigma.contains(140001210) && sStigma.contains(140001199) && sStigma.contains(140001197) ||
                    	sStigma.contains(140001210) && sStigma.contains(140001158) && sStigma.contains(140001197) ||
                    	sStigma.contains(140001210) && sStigma.contains(140001199) && sStigma.contains(140001198) ||
                    	sStigma.contains(140001210) && sStigma.contains(140001158) && sStigma.contains(140001198)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3549).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3549).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3849).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3849).getName(), 1, player.getName()));
                    }
                }
                return;
            case CLERIC:
                if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001246) && sStigma.contains(140001234) && sStigma.contains(140001232) ||
                    	sStigma.contains(140001246) && sStigma.contains(140001234) && sStigma.contains(140001233) ||
                    	sStigma.contains(140001246) && sStigma.contains(140001232) && sStigma.contains(140001233)) { 	
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3932).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3932).getName(), 1, player.getName()));
                    } else if (sStigma.contains(140001245) && sStigma.contains(140001229) && sStigma.contains(140001228) ||
                    		   sStigma.contains(140001245) && sStigma.contains(140001229) && sStigma.contains(140001230) ||
                    	       sStigma.contains(140001245) && sStigma.contains(140001228) && sStigma.contains(140001230)||
                    	       sStigma.contains(140001245) && sStigma.contains(140001229) && sStigma.contains(140001231) ||
                    	       sStigma.contains(140001245) && sStigma.contains(140001228) && sStigma.contains(140001230)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(4167).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(4167).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(3906).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(3906).getName(), 1, player.getName()));
                    }
                }
                return;
            case CHANTER:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001226) && sStigma.contains(140001212) && sStigma.contains(140001213) ||
                    	sStigma.contains(140001226) && sStigma.contains(140001212) && sStigma.contains(140001211) ||
                    	sStigma.contains(140001226) && sStigma.contains(140001213) && sStigma.contains(140001211)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1907).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1907).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001227) && sStigma.contains(140001214) && sStigma.contains(140001216) ||
                    	sStigma.contains(140001227) && sStigma.contains(140001214) && sStigma.contains(140001215) ||
                    	sStigma.contains(140001227) && sStigma.contains(140001216) && sStigma.contains(140001215)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1901).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1901).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(1904).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(1904).getName(), 1, player.getName()));
                    }
                }
                return;
            case RIDER:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001279) && sStigma.contains(140001264) && sStigma.contains(140001269) ||
                    	sStigma.contains(140001279) && sStigma.contains(140001264) && sStigma.contains(140001265) ||
                    	sStigma.contains(140001279) && sStigma.contains(140001269) && sStigma.contains(140001265)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2852).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2852).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001280) && sStigma.contains(140001266) && sStigma.contains(140001268) ||
                    	sStigma.contains(140001280) && sStigma.contains(140001266) && sStigma.contains(140001267) ||
                    	sStigma.contains(140001280) && sStigma.contains(140001268) && sStigma.contains(140001267)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2861).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2861).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2849).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2849).getName(), 1, player.getName()));
                    }
                }
                return;
            case GUNNER:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001262) && sStigma.contains(140001249) && sStigma.contains(140001247) ||
                    	sStigma.contains(140001262) && sStigma.contains(140001249) && sStigma.contains(140001248) ||
                    	sStigma.contains(140001262) && sStigma.contains(140001247) && sStigma.contains(140001248)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2368).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2368).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001263) && sStigma.contains(140001251) && sStigma.contains(140001252) ||
                    	sStigma.contains(140001263) && sStigma.contains(140001251) && sStigma.contains(140001250) ||
                    	sStigma.contains(140001263) && sStigma.contains(140001252) && sStigma.contains(140001250)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2371).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2371).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(2380).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(2380).getName(), 1, player.getName()));
                    }
                }
                return;
            case BARD:
            	if ((sStigma.size() == 6) && !hasInert) {
                    if (sStigma.contains(140001297) && sStigma.contains(140001285) && sStigma.contains(140001283) ||
                    	sStigma.contains(140001297) && sStigma.contains(140001285) && sStigma.contains(140001286) ||
                    	sStigma.contains(140001297) && sStigma.contains(140001283) && sStigma.contains(140001286)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(4483).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(4483).getName(), 1, player.getName()));
                        
                    } else if (sStigma.contains(140001296) && sStigma.contains(140001281) && sStigma.contains(140001284) ||
                    	sStigma.contains(140001296) && sStigma.contains(140001281) && sStigma.contains(140001282) ||
                    	sStigma.contains(140001296) && sStigma.contains(140001284) && sStigma.contains(140001282)) {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(4474).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(4474).getName(), 1, player.getName()));
                    } else {
                    	HashMap<Integer, Integer> realSkillId = DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(DataManager.SKILL_DATA.getSkillTemplate(4564).getStack());
                        Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
                        treeMap.putAll(realSkillId);
                        NavigableSet<Integer> nset=((TreeMap<Integer, Integer>) treeMap).descendingKeySet();
                        int skillId=0;
                        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
                            if (player.getLevel() >= entry.getKey()){
                            	skillId = entry.getValue();
                            }
                         }
                        if (realSkillId != null){
                        	player.getSkillList().addLinkedSkill(player, skillId, 1); 
                        }
                    	PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_HIDDEN_SKILL(DataManager.SKILL_DATA.getSkillTemplate(4564).getName(), 1, player.getName()));
                    }
                }
                return;
        }
        hasInert = false;
    }
}