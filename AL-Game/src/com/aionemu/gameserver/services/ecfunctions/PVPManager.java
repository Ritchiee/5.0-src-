package com.aionemu.gameserver.services.ecfunctions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.utils3d.FFA3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;


/**
 * @author Goong ADM
 * @editor Ghostfur
 */
public class PVPManager extends GeneralInstanceHandler {

    @SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(PVPManager.class);
    
    private static final PVPManager service = new PVPManager();
	
	private int count;
	private String winnerName, loserName;


    public static PVPManager getInstance(){
        return service;
    }
	
    /*
     * Misc features
     */
    
    public String toLowercase (String text) {
        String theText = text;
        theText = theText.toLowerCase();
        theText = Character.toString(theText.charAt(0)).toUpperCase() + theText.substring(1);
        return theText;
    }
    public void openDoors(WorldMapInstance instance) {
        for (StaticDoor door : instance.getDoors().values()) {
            if (door != null) {
            	door.setOpen(true);
            }
        }
    }    
	protected FFA3D spawnLoc(FFA3D loc, FFA3D [] spawnArray, int position) {
    	loc = spawnArray[position];
    	return loc;
    }	
    protected void xMorph(Player target) {
        target.getTransformModel().setModelId(0);
        PacketSendUtility.broadcastPacketAndReceive(target, new SM_TRANSFORM(target, true));
    }
    protected String getWinnerName () {
    	return winnerName;
    }
    protected String getLoserName () {
    	return loserName;
    } 
    
    /*
     * Root/Paralize/Sleep
     */
    
    public void paralizePlayer(Player player, boolean canParalize) {
    	if (canParalize) {
    		player.getEffectController().setAbnormal(AbnormalState.PARALYZE.getId());
    	} else {
    		player.getEffectController().unsetAbnormal(AbnormalState.PARALYZE.getId());
    	}
    	player.getEffectController().updatePlayerEffectIcons();
    	player.getEffectController().broadCastEffects();
    }    
    public void rootPlayer(Player player, boolean canRoot) {
    	if (canRoot) {
    		player.getEffectController().setAbnormal(AbnormalState.ROOT.getId());
    	} else {
    		player.getEffectController().unsetAbnormal(AbnormalState.ROOT.getId());
    	}
    	player.getEffectController().updatePlayerEffectIcons();
    	player.getEffectController().broadCastEffects();
    }
    public int getPlayerCountDFFA(WorldMapInstance instance) {
        if(instance == null) {
            return 0;
        } else {
        	count = 0;
			for (Player p : instance.getPlayersInside()) {
				if (p.isInDuoFFA()) {
					count++;
				}
			}
			return count;
        }   
    }    
    public int getPlayerCountFFA(WorldMapInstance instance) {
        if(instance == null) {
            return 0;
        } else {
        	count = 0;
			for (Player p : instance.getPlayersInside()) {
				if (p.isInFFA()) {
					count++;
				}
			}
			return count;
        }   
    }    
    public void AddProtection(final Player p, int duration) {    	
        Skill protector1 = SkillEngine.getInstance().getSkill(p, 9833, 1, p.getTarget());
        Skill protector2 = SkillEngine.getInstance().getSkill(p, 18474, 1, p.getTarget());
        
        Effect a = new Effect(p, p, protector1.getSkillTemplate(), protector1.getSkillLevel(), duration);
        Effect b = new Effect(p, p, protector2.getSkillTemplate(), protector2.getSkillLevel(), duration);
        
        for (EffectTemplate at : a.getEffectTemplates()) {
            at.setDuration(duration);
        }
        for (EffectTemplate bt : b.getEffectTemplates()) {
        	bt.setDuration(duration);
        }
        a.initialize();
        a.applyEffect();
        b.initialize();
        b.applyEffect();
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
            	p.getEffectController().removeEffect(9833);
            	p.getEffectController().removeEffect(18474);
            }
        }, duration);
    }
    protected void HealPlayer(Player player, boolean withDp, boolean sendUpdatePacket) {
        player.getLifeStats().setCurrentHpPercent(100);
        player.getLifeStats().setCurrentMpPercent(100);
        if (withDp) {
            player.getCommonData().setDp(4000);
        }
        if (sendUpdatePacket) {
            player.getLifeStats().sendHpPacketUpdate();
            player.getLifeStats().sendMpPacketUpdate();
        }
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
    }
    
    /*
     * Send Message Holder     
     */   
    
    public void sendSpecMessage(String sender, String msg, WorldMapInstance instance) {
        for (Player p : instance.getPlayersInside()) {
            PacketSendUtility.sendMessage(p, sender, msg, ChatType.GROUP_LEADER);
        }
    }
    public void sendSpecMessageHolder(String sender, String msg, List<Player> thePlayers) {
		for (Player p : thePlayers) {
			PacketSendUtility.sendMessage(p, sender, msg, ChatType.GROUP_LEADER);
		}
	}
	protected void deSpawnPortal(SpawnTemplate spawn) {
        Npc npcSpawn = null;
        npcSpawn = (Npc) spawn.getVisibleObject();
        if (npcSpawn != null) {
        	npcSpawn.getController().onDelete();
        }
	}

/**
 * Display Template For FFA/2v2/Arena
 */

	public static int getDisplayTemplate(Player player, Item item) {
		 if (item.getItemTemplate().isWeapon()) {
		  switch (item.getItemTemplate().getWeaponType()) {
		   case POLEARM_2H: // Gulare's Polearm
		    return 101300983;
		   case DAGGER_1H: // Gulare's Dagger
		    return 100201189;
		   case BOW: // Gulare's Bow
		    return 101701064;
		   case SWORD_1H: // Gulare's Sword
		    return 100001348;
		   case SWORD_2H: // Gulare's Greatsword
		    return 100901040;
		   case MACE_1H: // Gulare's Mace
		    return 100101027;
		   case STAFF_2H: // Gulare's Staff
		    return 101501055;
		   case ORB_2H: // Gulare's Orb
		    return 100501040;
		   case BOOK_2H: // Gulare's Spellbook
		    return 100601092;
		   case GUN_1H: // Gulare's Pistol
		    return 101800837;
		   case CANNON_2H: // Gulare's Aethercannon
		    return 101900841;
		   case HARP_2H: // Gulare's Harp
		    return 102000875;
		   case KEYBLADE_2H: // Gulare's Cipher-Blade
		    return 102100733;
		   default:
		    return 102100733;
		  }
		 } else if (item.getEquipmentSlot() == 8) {// Torse
		  if (player.getRace() == Race.ELYOS) {
		   return 110901091;// Legion suit
		  } else {
		   return 110901092;// Legion suit
		  }
		 } else if (item.getItemTemplate().getArmorType() == ArmorType.SHIELD) // Shield
		 {
		  return 115001388;// Antro's Shield
		 } else {
		  return item.getItemSkinTemplate().getTemplateId();
		 }
		}

/**
 * Get Display Name
 */

public String getName(Player player, Player target) {
	if (player.isGmMode()) {
		return target.getName();
	}

	String FFAplayerName = target.getPlayerClass().name();

	//if (!player.isInGroup2() && !player.isInAlliance2()) {
	//	FFAplayerName += " " + (target.getFFAIndex() + 1);
	//}

	return FFAplayerName;
  }
}

