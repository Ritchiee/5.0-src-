/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
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
package playercommands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eloann
 * @reworked Kill3r
 */
public class cmd_gmlist extends PlayerCommand {

    public cmd_gmlist() {
        super("gmlist");
    }

    @Override
    public void execute(Player player, String... params) {
        final List<Player> admins = new ArrayList<Player>();
        final List<Player> helpers = new ArrayList<Player>();
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player object) {
                if (object.getAccessLevel() >= 2 && object.getFriendList().getStatus() != FriendList.Status.OFFLINE) {
                    admins.add(object);
                }
            }
        });
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (player.getAccessLevel() == 1){
                    helpers.add(player);
                }
            }
        });

        if (helpers.size() > 0) {
            PacketSendUtility.sendMessage(player, "===== Helpers =====");
            if (helpers.size() == 1){
                PacketSendUtility.sendMessage(player, "There is only 1 Helper Online!");
            } else {
                PacketSendUtility.sendMessage(player, "There are some Helper's Online!");
            }

            for (Player helper : helpers){
                String tag = "";
                if (helper.getAccessLevel() == 1){
                    tag = AdminConfig.CUSTOMTAG_ACCESS1;
                }
                tag = tag.substring(0, tag.length() - 2);
                PacketSendUtility.sendMessage(player, tag + helper.getName());
            }
            PacketSendUtility.sendMessage(player, "===================");
        }else{
            PacketSendUtility.sendMessage(player, "====== Helpers =====");
            PacketSendUtility.sendMessage(player, "There are no Helpers Online!");
            PacketSendUtility.sendMessage(player, "====================");
        }

        if (admins.size() > 0) {
            PacketSendUtility.sendMessage(player, "==== GameMasters ===");
            if (admins.size() == 1) {
                PacketSendUtility.sendMessage(player, "There is only 1 GM Online!");
            } else {
                PacketSendUtility.sendMessage(player, "There are some GM's Online!");
            }

            for (Player admin : admins) {
                String tag = "";
                String tagEnd = "";
                if (admin.getAccessLevel() == 2){ //trialGM
                    tag = AdminConfig.CUSTOMTAG_ACCESS2;
                }else if (admin.getAccessLevel() == 3){ // GM
                    tag = AdminConfig.CUSTOMTAG_ACCESS3;
                }else if (admin.getAccessLevel() == 4){ // HGM
                    tag = AdminConfig.CUSTOMTAG_ACCESS4;
                }else if (admin.getAccessLevel() == 5){ //Dev
                    tag = AdminConfig.CUSTOMTAG_ACCESS5;
                }else if (admin.getAccessLevel() == 6){ //Custom
                    tag = AdminConfig.CUSTOMTAG_ACCESS6;
                }else if (admin.getAccessLevel() == 7){ //Custom
                    tag = AdminConfig.CUSTOMTAG_ACCESS7;
                }else if (admin.getAccessLevel() == 8){ //Custom
                    tag = AdminConfig.CUSTOMTAG_ACCESS8;
                }else if (admin.getAccessLevel() == 9){ //Custom
                    tag = AdminConfig.CUSTOMTAG_ACCESS9;
                }else if (admin.getAccessLevel() == 10){ //Custom
                    tag = AdminConfig.CUSTOMTAG_ACCESS10;
                }// "\uE050 \uE042 Senior-GM \uE043 %s \uE050")
                tagEnd = tag.substring(tag.length() - 2);
                tag = tag.substring(0, tag.length() - 4);
                PacketSendUtility.sendMessage(player, tag + admin.getName() + tagEnd);
            }
            PacketSendUtility.sendMessage(player, "====================");

        } else {
            PacketSendUtility.sendMessage(player, "==== GameMasters ===");
            PacketSendUtility.sendMessage(player, "There are no GM Online!");
            PacketSendUtility.sendMessage(player, "====================");
        }

    }
}
