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
package com.aionemu.gameserver.network.factories;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.AionPacketHandler;
import com.aionemu.gameserver.network.aion.clientpackets.*;

/**
 * This factory is responsible for creating {@link AionPacketHandler} object. It also initializes created handler with a
 * set of packet prototypes.<br>
 * Object of this classes uses <tt>Injector</tt> for injecting dependencies into prototype objects.<br>
 * <br>
 *
 * @author Luno, Alcapwnd, Ever, Falke34, FrozenKiller
 * @version .x.x
 */
public class AionPacketHandlerFactory {

    private AionPacketHandler handler;

    public static AionPacketHandlerFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
     */
    public AionPacketHandlerFactory() {

        handler = new AionPacketHandler();

        //============================== TODO MISSING PACKET CLIENT OPCODE ========================\\
        
        
        addPacket(new CM_FRIEND_EDIT(0xA1A9, State.IN_GAME)); // 	
		addPacket(new CM_MARK_FRIENDLIST(0xA128, State.IN_GAME)); //
		
		addPacket(new CM_GROUP_DATA_EXCHANGE(0xA109, State.IN_GAME)); //TODO find 18B
		addPacket(new CM_FAST_TRACK_CHECK(0xA191, State.IN_GAME)); // 	
		addPacket(new CM_FAST_TRACK(0xA196, State.IN_GAME)); // 
		addPacket(new CM_DIRECT_ENTER_WORLD(0xA191, State.IN_GAME)); // 
		addPacket(new CM_SELECTITEM_OK(0xA1AA, State.IN_GAME)); // 
		addPacket(new CM_SUBZONE_CHANGE(0xA17D, State.IN_GAME)); // 
		addPacket(new CM_APPEARANCE(0x0184, State.IN_GAME)); // 5.0
		addPacket(new CM_CAPTCHA(0xAC8, State.IN_GAME)); // 
		
		addPacket(new CM_BLOCK_SET_REASON(0xA18D, State.IN_GAME)); // 
		addPacket(new CM_CHAT_GROUP_INFO(0xA2FB, State.IN_GAME)); //  
		addPacket(new CM_CHAT_PLAYER_INFO(0xAE1, State.IN_GAME)); // 
		addPacket(new CM_FATIGUE_RECOVER(0xA135, State.IN_GAME));
		
        
        //=========================================================================================\\
        addPacket(new CM_CRAFT(0x016C, State.IN_GAME)); //5.0
        addPacket(new CM_QUEST_SHARE(0x0163, State.IN_GAME)); // 5.0
        addPacket(new CM_LEGION_SEARCH(0x1BB, State.IN_GAME)); //5.0
        addPacket(new CM_RESTORE_CHARACTER(0x0158, State.AUTHED)); // 5.0
        addPacket(new CM_TELEPORT_SELECT(0x0153, State.IN_GAME)); // 5.0
        addPacket(new CM_SET_NOTE(0x02F9, State.IN_GAME)); //  5.0 
        addPacket(new CM_LEGION_JOIN_REQUEST(0x01DC, State.IN_GAME)); //5.0
		addPacket(new CM_LEGION_JOIN_REQUEST_CANCEL(0x01DD, State.IN_GAME)); //5.0
		addPacket(new CM_ITEM_REMODEL(0x0119, State.IN_GAME)); // 5.0
		addPacket(new CM_TELEPORT_DONE(0x00EE, State.IN_GAME)); // 5.0
		addPacket(new CM_COMPOSITE_STONES(0x01AF, State.IN_GAME)); // 5.0
		addPacket(new CM_ITEM_PURIFICATION(0x01B6, State.IN_GAME)); // 5.0
		addPacket(new CM_UPGRADE_ARCADE(0x01B5, State.IN_GAME)); // 5.0
        addPacket(new CM_FILE_VERIFY(0x011D, State.IN_GAME)); // 5.0
        addPacket(new CM_ATREIAN_PASSPORT(0x01B7, State.IN_GAME)); //5.0
		addPacket(new CM_UI_SETTINGS(0x00C9, State.IN_GAME)); // 5.0
		addPacket(new CM_MOTION(0x0106, State.IN_GAME)); // 5.0
		addPacket(new CM_WINDSTREAM(0x0105, State.IN_GAME)); // 5.0
		addPacket(new CM_STOP_TRAINING(0x0113, State.IN_GAME)); // 5.0
		addPacket(new CM_REVIVE(0x00C4, State.IN_GAME)); // 5.0
		addPacket(new CM_DUEL_REQUEST(0x0131, State.IN_GAME)); // 5.0
		
		addPacket(new CM_OPEN_STATICDOOR(0x00D6, State.IN_GAME)); // 5.0
		addPacket(new CM_SPLIT_ITEM(0x017C, State.IN_GAME)); // 5.0
		addPacket(new CM_CUSTOM_SETTINGS(0x00CB, State.IN_GAME)); // 5.0
		addPacket(new CM_PLAY_MOVIE_END(0x0110, State.IN_GAME)); // 5.0
		addPacket(new CM_LEVEL_READY(0x00C8, State.IN_GAME)); // 5.0
		addPacket(new CM_ENTER_WORLD(0x00C7, State.AUTHED)); // 5.0
		addPacket(new CM_TIME_CHECK(0x00D1, State.CONNECTED, State.AUTHED, State.IN_GAME)); // 5.0
		addPacket(new CM_QUIT(0x00C2, State.AUTHED, State.IN_GAME)); // 5.0
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x0154, State.CONNECTED)); // 5.0
		addPacket(new CM_CHARACTER_LIST(0x0155, State.AUTHED)); // 5.0
		addPacket(new CM_CREATE_CHARACTER(0x0156, State.AUTHED)); // 5.0
		addPacket(new CM_MAC_ADDRESS(0x019C, State.CONNECTED, State.AUTHED, State.IN_GAME)); // 5.0
		addPacket(new CM_CHARACTER_PASSKEY(0x0191, State.AUTHED)); //5.0 
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x179, State.AUTHED)); // 5.0
		addPacket(new CM_MOVE(0x010F, State.IN_GAME, State.AUTHED)); // 5.0
		addPacket(new CM_CASTSPELL(0x00E0, State.IN_GAME)); //5.0 
		addPacket(new CM_EMOTION(0x00EA, State.IN_GAME)); // 5.0
		addPacket(new CM_TITLE_SET(0x014A, State.IN_GAME)); //  5.0
		addPacket(new CM_DELETE_ITEM(0x0133, State.IN_GAME)); // 5.0
		addPacket(new CM_DELETE_QUEST(0x0012F, State.IN_GAME)); // 5.0
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x017B, State.IN_GAME)); //5.0 
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x0135, State.IN_GAME)); // 5.0
		addPacket(new CM_PRIVATE_STORE(0x0136, State.IN_GAME)); // 5.0
		addPacket(new CM_USE_ITEM(0x00E4, State.IN_GAME)); // 5.0
		addPacket(new CM_TARGET_SELECT(0x00FE, State.IN_GAME)); // 5.0
		
		addPacket(new CM_CHECK_NICKNAME(0x0170, State.AUTHED)); // 5.0
		addPacket(new CM_PRIVATE_STORE_NAME(0x0137, State.IN_GAME)); // 5.0
		addPacket(new CM_DELETE_CHARACTER(0x0157, State.AUTHED)); // 5.0
		addPacket(new CM_MACRO_CREATE(0x0018E, State.IN_GAME)); // 5.0
		addPacket(new CM_MACRO_DELETE(0x018F, State.IN_GAME)); // 5.0
		addPacket(new CM_GATHER(0x00D2, State.IN_GAME)); // 5.0
		addPacket(new CM_INSTANCE_INFO(0x019F, State.IN_GAME)); // 5.0
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x012A, State.IN_GAME)); // 5.0
		addPacket(new CM_START_LOOT(0x0159, State.IN_GAME)); // 5.0
		
		addPacket(new CM_QUESTION_RESPONSE(0x02F1, State.IN_GAME)); // 5.0
		addPacket(new CM_BUY_ITEM(0x02F2, State.IN_GAME)); // 5.0
		addPacket(new CM_SHOW_DIALOG(0x02F3, State.IN_GAME)); // 5.0
		addPacket(new CM_CLOSE_DIALOG(0x02F4, State.IN_GAME, State.AUTHED)); // 5.0
		addPacket(new CM_DIALOG_SELECT(0x02F5, State.IN_GAME)); // 5.0
		
		addPacket(new CM_EQUIP_ITEM(0x00E5, State.IN_GAME)); // 5.0
		addPacket(new CM_LOOT_ITEM(0x015A, State.IN_GAME)); // 5.0
		addPacket(new CM_QUESTIONNAIRE(0x0150, State.IN_GAME)); // 5.0
		addPacket(new CM_ATTACK(0x00FF, State.IN_GAME)); // 5.0
		addPacket(new CM_PET(0x00D5, State.IN_GAME)); // 5.0
		addPacket(new CM_TUNE(0x01AA, State.IN_GAME)); // 5.0
		addPacket(new CM_PET_EMOTE(0x00D4, State.IN_GAME)); // 5.0
		addPacket(new CM_CHALLENGE_LIST(0x01A7, State.IN_GAME)); //5.0 
		// ********************(FRIEND LIST)*********************
		addPacket(new CM_SHOW_FRIENDLIST(0x01A5, State.IN_GAME)); // 5.0
		addPacket(new CM_FRIEND_ADD(0x014E, State.IN_GAME)); // 5.0
		addPacket(new CM_FRIEND_DEL(0x014F, State.IN_GAME)); // 5.0
		addPacket(new CM_FRIEND_STATUS(0x0169, State.IN_GAME)); // 5.0        
		addPacket(new CM_SHOW_BLOCKLIST(0x017D, State.IN_GAME)); // 5.0
		addPacket(new CM_BLOCK_ADD(0x0165, State.IN_GAME)); // 5.0
		addPacket(new CM_BLOCK_DEL(0x0166, State.IN_GAME)); // 5.0
		addPacket(new CM_PLAYER_SEARCH(0x017E, State.IN_GAME)); // 5.0			
		// ********************(LEGION)*********************
		addPacket(new CM_LEGION(0x010C, State.IN_GAME)); // 5.0
		addPacket(new CM_LEGION_WH_KINAH(0x010B, State.IN_GAME)); // 5.0
		addPacket(new CM_LEGION_UPLOAD_INFO(0x017F, State.IN_GAME)); // 5.0
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x0160, State.IN_GAME)); // 5.0
        addPacket(new CM_LEGION_SEND_EMBLEM_INFO(0x00EF, State.IN_GAME)); // 5.0
		addPacket(new CM_LEGION_SEND_EMBLEM(0x010E, State.IN_GAME)); // 5.0
		addPacket(new CM_LEGION_MODIFY_EMBLEM(0x02FA, State.IN_GAME)); // 5.0
		addPacket(new CM_LEGION_TABS(0x02F6, State.IN_GAME)); // 5.0
		addPacket(new CM_STONESPEAR_SIEGE(0x00FC, State.IN_GAME)); // 5.0
		// ******************(GROUP)******************* (BUGGY)
		addPacket(new CM_FIND_GROUP(0x012C, State.IN_GAME, State.AUTHED)); // 5.0
		addPacket(new CM_AUTO_GROUP(0x0187, State.IN_GAME)); // 5.0
		addPacket(new CM_INVITE_TO_GROUP(0x0120, State.IN_GAME)); // 5.0
		addPacket(new CM_GROUP_DISTRIBUTION(0x012B, State.IN_GAME)); // 5.0
		addPacket(new CM_GROUP_LOOT(0x0177, State.IN_GAME)); // 5.0
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x0178, State.IN_GAME)); // 5.0
		addPacket(new CM_SHOW_BRAND(0x0174, State.IN_GAME)); // 5.0 (Group Mark Target etc)	
		// ******************(BROKER)******************
		addPacket(new CM_BROKER_LIST(0x013A, State.IN_GAME)); //5.0 
		addPacket(new CM_BROKER_SEARCH(0x013B, State.IN_GAME)); // 5.0
		addPacket(new CM_REGISTER_BROKER_ITEM(0x015E, State.IN_GAME)); // 5.0	
		addPacket(new CM_BROKER_SETTLE_LIST(0x015C, State.IN_GAME)); // 5.0	
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x015F, State.IN_GAME)); // 5.0
		addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x0140, State.IN_GAME)); // 5.0	
		addPacket(new CM_BROKER_REGISTERED(0x134, State.IN_GAME)); // 5.0
		addPacket(new CM_BUY_BROKER_ITEM(0x015D, State.IN_GAME)); // 5.0
		addPacket(new CM_BROKER_ADD_ITEM(0xA133, State.IN_GAME)); // 
		// ******************(PING)******************
		addPacket(new CM_PING_REQUEST(0x0126, State.IN_GAME)); // 5.0
		addPacket(new CM_PING(0x00EB, State.AUTHED, State.IN_GAME)); // 5.0
		// ******************(SUMMON)******************
		addPacket(new CM_SUMMON_EMOTION(0x0189, State.IN_GAME)); // 5.0
		addPacket(new CM_SUMMON_ATTACK(0x018A, State.IN_GAME)); // 5.0
		addPacket(new CM_SUMMON_CASTSPELL(0x01AC, State.IN_GAME)); // 5.0
		addPacket(new CM_SUMMON_COMMAND(0x0138, State.IN_GAME)); // 5.0
		addPacket(new CM_SUMMON_MOVE(0x0188, State.IN_GAME)); // 5.0	
		// ******************(MAIL)******************
		addPacket(new CM_CHECK_MAIL_SIZE(0x0144, State.IN_GAME)); // 5.0
		addPacket(new CM_CHECK_MAIL_SIZE2(0x0194, State.IN_GAME)); // 5.0
		addPacket(new CM_SEND_MAIL(0x0143, State.IN_GAME)); // 5.0
		addPacket(new CM_READ_MAIL(0x0145, State.IN_GAME)); // 5.0
		addPacket(new CM_READ_EXPRESS_MAIL(0x0161, State.IN_GAME)); // 5.0
		addPacket(new CM_DELETE_MAIL(0x0148, State.IN_GAME)); // 5.0
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x0147, State.IN_GAME)); // 5.0	
		// ******************(EXCHANGE)******************
		addPacket(new CM_EXCHANGE_ADD_ITEM(0x011F, State.IN_GAME)); // 5.0
		addPacket(new CM_EXCHANGE_ADD_KINAH(0x0101, State.IN_GAME)); // 5.0
		addPacket(new CM_EXCHANGE_LOCK(0x0102, State.IN_GAME)); // 5.0
		addPacket(new CM_EXCHANGE_CANCEL(0x0104, State.IN_GAME)); // 5.0
		addPacket(new CM_EXCHANGE_OK(0x0103, State.IN_GAME)); //5.0 
		addPacket(new CM_EXCHANGE_REQUEST(0x011E, State.IN_GAME)); // 5.0
		// *************(HOUSE)***************************
		addPacket(new CM_HOUSE_OPEN_DOOR(0x01A1, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_TELEPORT_BACK(0x013E, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_SCRIPT(0x00FD, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_TELEPORT(0x01BD, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_EDIT(0x0111, State.IN_GAME)); //  5.0
		addPacket(new CM_USE_HOUSE_OBJECT(0x01BF, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_SETTINGS(0x0108, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_KICK(0x0107, State.IN_GAME)); // 5.0
		addPacket(new CM_GET_HOUSE_BIDS(0x0199, State.IN_GAME, State.AUTHED)); // 5.0
		addPacket(new CM_HOUSE_PAY_RENT(0x01BE, State.IN_GAME)); //  5.0 
		addPacket(new CM_REGISTER_HOUSE(0x019A, State.IN_GAME)); // 5.0
		addPacket(new CM_PLACE_BID(0x19B, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_DECORATE(0x010A, State.IN_GAME)); // 5.0
		addPacket(new CM_RELEASE_OBJECT(0x01A0, State.IN_GAME)); // 5.0	
		// ******************(OTHERS)******************
		addPacket(new CM_OBJECT_SEARCH(0x00CA, State.IN_GAME)); // 5.0
		addPacket(new CM_MOVE_IN_AIR(0x02F0, State.IN_GAME)); // 5.0
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x0123, State.IN_GAME)); // 5.0	
		addPacket(new CM_CHARACTER_EDIT(0x00C6, State.AUTHED)); //5.0 
		addPacket(new CM_PLAYER_STATUS_INFO(0x013F, State.IN_GAME)); // 5.0
		addPacket(new CM_MANASTONE(0x0109, State.IN_GAME)); // 5.0
		addPacket(new CM_FUSION_WEAPONS(0x01AD, State.IN_GAME)); // 5.0	
		addPacket(new CM_TOGGLE_SKILL_DEACTIVATE(0x00E1, State.IN_GAME)); // 5.0
		addPacket(new CM_RECIPE_DELETE(0x0118, State.IN_GAME)); // 5.0
		addPacket(new CM_REMOVE_ALTERED_STATE(0x00E2, State.IN_GAME)); // 5.0
		addPacket(new CM_MAY_QUIT(0x00C3, State.AUTHED, State.IN_GAME)); // 5.0
		addPacket(new CM_REPORT_PLAYER(0x00D9, State.IN_GAME)); // 5.0
		addPacket(new CM_PLAYER_LISTENER(0x00E7, State.IN_GAME)); // 5.0
		addPacket(new CM_BONUS_TITLE(0x01A8, State.IN_GAME)); //  5.0
		addPacket(new CM_BUY_TRADE_IN_TRADE(0x0117, State.IN_GAME)); // 5.0
		addPacket(new CM_BREAK_WEAPONS(0x01AE, State.IN_GAME)); // 5.0
		addPacket(new CM_CHARGE_ITEM(0x012D, State.IN_GAME)); // 5.0
		addPacket(new CM_USE_CHARGE_SKILL(0x01A9, State.IN_GAME)); // 5.0
		addPacket(new CM_RECONNECT_AUTH(0x0176, State.AUTHED)); // 5.0	
		addPacket(new CM_INSTANCE_LEAVE(0x010D, State.IN_GAME)); // 5.0	 
		addPacket(new CM_MEGAPHONE(0x01CC, State.IN_GAME)); // 5.0	
		addPacket(new CM_MOVE_ITEM(0x015B, State.IN_GAME)); // 5.0	
		addPacket(new CM_GAMEGUARD(0x0127, State.IN_GAME)); // 5.0
		// ******************(Fast Track Server)******************	
		// ******************(CHAT)******************
		addPacket(new CM_CHAT_AUTH(0x018D, State.IN_GAME)); // 5.0
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x00DA, State.IN_GAME)); // 5.0	
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x00DB, State.IN_GAME)); // 5.0	
		// ******************(CM_VERSION_CHECK)******************
		addPacket(new CM_VERSION_CHECK(0x00DF, State.CONNECTED)); // 5.0
		addPacket(new CM_UPDATE_CP(0x0125, State.IN_GAME));//5.0
		// ******************(Emu Feature)******************		
		
				
		// /////////////////// NEW 4.7 //////////////////////
		 
		addPacket(new CM_HOTSPOT_TELEPORT(0x01B3, State.IN_GAME)); //5.0 
		
        
        // /////////////////// NEW  //////////////////////
        addPacket(new CM_EXPAND_CUBE(0x01BA, State.IN_GAME)); // 5.0
        
		// /////////////////// GM PACKET ////////////////////
		addPacket(new CM_GM_COMMAND_SEND(0x00E9, State.IN_GAME)); //5.0 
		addPacket(new CM_GM_BOOKMARK(0x00E8, State.IN_GAME)); // 5.0
		
		// /////////////////////////////////////////////////
		addPacket(new CM_1A3_UNK(0xA1A3, State.IN_GAME)); //  todo
		addPacket(new CM_11A_UNK(0x011B, State.IN_GAME,  State.AUTHED)); //  5.0
        ///////////////////////REMOVED PACKETS\\\\\\\\\\\\\\\\\\\\\\\\\
        //addPacket(new CM_IN_GAME_SHOP_INFO(0x184, State.IN_GAME)); // Removed 4.7
    }
    

    public AionPacketHandler getPacketHandler() {
        return handler;
    }

    private void addPacket(AionClientPacket prototype) {
        handler.addPacketPrototype(prototype);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final AionPacketHandlerFactory instance = new AionPacketHandlerFactory();
    }
}
