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
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author A7xatomic
 * @reworked Ghostfur
 */

public class PvPSpreeService {

	private static final Logger log = LoggerFactory.getLogger("PVP_LOG");
	private static final String STRING_SPREE1 = LanguageHandler.translate(CustomMessageId.SPREE1);
	private static final String STRING_SPREE2 = LanguageHandler.translate(CustomMessageId.SPREE2);
	private static final String STRING_SPREE3 = LanguageHandler.translate(CustomMessageId.SPREE3);
	private static final String STRING_SPREE4 = LanguageHandler.translate(CustomMessageId.SPREE4);
	private static final String STRING_SPREE5 = LanguageHandler.translate(CustomMessageId.SPREE5);
	private static final String STRING_SPREE6 = LanguageHandler.translate(CustomMessageId.SPREE6);
	private static final String STRING_SPREE7 = LanguageHandler.translate(CustomMessageId.SPREE7);
	private static final String STRING_SPREE8 = LanguageHandler.translate(CustomMessageId.SPREE8);
	private static final String STRING_SPREE9 = LanguageHandler.translate(CustomMessageId.SPREE9);
	private static final String STRING_SPREE10 = LanguageHandler.translate(CustomMessageId.SPREE10);
	public static void increaseRawKillCount(Player winner) {
		int currentRawKillCount = winner.getRawKillCount();
		winner.setRawKillCount(currentRawKillCount + 1);
		int newRawKillCount = currentRawKillCount + 1;
		PacketSendUtility.sendWhiteMessageOnCenter(winner, LanguageHandler.translate(CustomMessageId.KILL_COUNT) + newRawKillCount);

		if ((newRawKillCount == CustomConfig.SPREE_KILL_COUNT) || (newRawKillCount == CustomConfig.CARNAGE_KILL_COUNT) 
				|| (newRawKillCount == CustomConfig.GENOCIDE_KILL_COUNT) || (newRawKillCount == CustomConfig.RAMPAGE_KILL_COUNT) 
				|| (newRawKillCount == CustomConfig.DOMINATING_KILL_COUNT) || (newRawKillCount == CustomConfig.UNSTOPPABLE_KILL_COUNT) 
				|| (newRawKillCount == CustomConfig.INSANEMONSTER_KILL_COUNT) || (newRawKillCount == CustomConfig.GODLIKE_KILL_COUNT) 
				|| (newRawKillCount == CustomConfig.WICKEDSICK_KILL_COUNT) || (newRawKillCount == CustomConfig.MUTHAFAKAAAS_KILL_COUNT)) {
			if (newRawKillCount == CustomConfig.SPREE_KILL_COUNT)
				updateSpreeLevel(winner, 1);
				ItemService.addItem(winner, CustomConfig.SPREE_REWARD_ITEM1, CustomConfig.SPREE_REWARD_COUNT1);		
				ItemService.addItem(winner, CustomConfig.SPREE_REWARD_ITEM2, CustomConfig.SPREE_REWARD_COUNT2);	
			if (newRawKillCount == CustomConfig.CARNAGE_KILL_COUNT)
				updateSpreeLevel(winner, 2);
			if (newRawKillCount == CustomConfig.GENOCIDE_KILL_COUNT)
				updateSpreeLevel(winner, 3);
			if (newRawKillCount == CustomConfig.RAMPAGE_KILL_COUNT)
				updateSpreeLevel(winner, 4);
			if (newRawKillCount == CustomConfig.DOMINATING_KILL_COUNT)
				updateSpreeLevel(winner, 5);
			if (newRawKillCount == CustomConfig.UNSTOPPABLE_KILL_COUNT)
				updateSpreeLevel(winner, 6);
			if (newRawKillCount == CustomConfig.INSANEMONSTER_KILL_COUNT)
				updateSpreeLevel(winner, 7);
			if (newRawKillCount == CustomConfig.GODLIKE_KILL_COUNT)
				updateSpreeLevel(winner, 8);
			if (newRawKillCount == CustomConfig.WICKEDSICK_KILL_COUNT)
				updateSpreeLevel(winner, 9);
			if (newRawKillCount == CustomConfig.MUTHAFAKAAAS_KILL_COUNT)
				updateSpreeLevel(winner, 10);
		}
	}

	private static void updateSpreeLevel(Player winner, int level) {
		winner.setSpreeLevel(level);
		sendUpdateSpreeMessage(winner, level);
	}
    private static void sendUpdateSpreeMessage(Player winner, int level) {
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
		for (@SuppressWarnings("unused") Player p : World.getInstance().getAllPlayers()) {
			if (level == 1)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE1) + STRING_SPREE1 + LanguageHandler.translate(CustomMessageId.MSG_SPREE1_1));
					}
			if (level == 2)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE2) + STRING_SPREE2 + LanguageHandler.translate(CustomMessageId.MSG_SPREE2_1));
					}
			if (level == 3)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE3) + STRING_SPREE3 + LanguageHandler.translate(CustomMessageId.MSG_SPREE3_1));
					}
			if (level == 4)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE4) + STRING_SPREE4 + LanguageHandler.translate(CustomMessageId.MSG_SPREE4_1));		
					}
			if (level == 5)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE5) + STRING_SPREE5 + LanguageHandler.translate(CustomMessageId.MSG_SPREE5_1));			
					}
			if (level == 6)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE6) + STRING_SPREE6 + LanguageHandler.translate(CustomMessageId.MSG_SPREE6_1));						}
					}
			if (level == 7)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE7) + STRING_SPREE7 + LanguageHandler.translate(CustomMessageId.MSG_SPREE7_1));
					}
			if (level == 8)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE8) + STRING_SPREE8 + LanguageHandler.translate(CustomMessageId.MSG_SPREE8_1));
					}
			if (level == 9)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE9) + STRING_SPREE9 + LanguageHandler.translate(CustomMessageId.MSG_SPREE9_1));
					}
			if (level == 10)
					while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), winner.getName() + LanguageHandler.translate(CustomMessageId.CUSTOM_MSG1) + winner.getCommonData().getRace().toString().toLowerCase() + LanguageHandler.translate(CustomMessageId.MSG_SPREE10) + STRING_SPREE10 + LanguageHandler.translate(CustomMessageId.MSG_SPREE10_1));
					}
        log.info("[PvP][Spree] {Player : " + winner.getName() + "} have now " + level + " Killing Spree Level");
    }
    public static void cancelSpree(Player victim, Creature killer, boolean isPvPDeath) {
        int killsBeforeDeath = victim.getRawKillCount();
        victim.setRawKillCount(0);
        if (victim.getSpreeLevel() > 0) {
            victim.setSpreeLevel(0);
            sendEndSpreeMessage(victim, killer, isPvPDeath, killsBeforeDeath);
        }
    }
    private static void sendEndSpreeMessage(Player victim, Creature killer, boolean isPvPDeath, int killsBeforeDeath) {
        String spreeEnder = isPvPDeath ? ((Player) killer).getName() : LanguageHandler.translate(CustomMessageId.SPREE_MONSTER_MSG);
        for (Player p : World.getInstance().getAllPlayers()) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(p, LanguageHandler.translate(CustomMessageId.SPREE_END_MSG1) + victim.getName() + LanguageHandler.translate(CustomMessageId.SPREE_END_MSG2) + spreeEnder + LanguageHandler.translate(CustomMessageId.SPREE_END_MSG3) + killsBeforeDeath + LanguageHandler.translate(CustomMessageId.SPREE_END_MSG4));
        }
        log.info("[PvP][Spree] {The Spree from " + victim.getName() + "} was ended by " + spreeEnder + "}");
    }
}


