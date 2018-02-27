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

package playercommands;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by Ghostfur
 */
public class cmd_help extends PlayerCommand {

    public cmd_help() {
        super("help");
    }

    @Override
    public void execute(Player player, String... params){
        if (params.length != 0) {
            onFail(player, null);
            return;
        }


		if (player.getRace() == Race.ASMODIANS ||player.getRace() == Race.ELYOS){
			PacketSendUtility.sendWhiteMessage(player, "" +
			"\n" +
				"========================\n" +
                "Available .[dot] Commands for Players!" +
                "\n========================\n" +
				//" .givestigma add : will give you your class stigma's\n" +
				//" .givestigma unlock :  will open your stigma slots.\n" +
                " .skills : refresh or get new skills.\n" +
                " .arena : to join battleground 1vs1\n" +
		        " .ffa or .2v2 : to leave ffa or 2vs2\n" +
		        " .pvp : will teleport you to insane pvp map [beluslan/heiron]\n" +
                " .clean <item id/link> : to delete an item\n" +
                " .dye <color> : to dye yourself.\n" +
                " .unstuck : go to obelisk location\n" +
                " .skin : will remove your candy look,\n" +
                " .unlockcube : opens all slots of your inventory\n" +
                " .asmo : asmodian world chat\n" +
                " .ely : elyos world chat\n" +
                " .world : open world chat\n" +               
                " .augmentme : will augment all your equiptment\n" +
                " .enchant 15 : will enchant your equiptment to 15.\n" +
                " .gmlist : shows available gm's \n" +
                " .drop : shows the drop list of an npc\n" +
                " .divorce : divorces from a Player\n" +
                " .marry : marry another player\n" +
                " .showgp : shows your current GP\n" +
                " .support : issues a short ticket for gm to view\n" +
                " .fixwep : gets weapon buff if you dont have one.");
            PacketSendUtility.sendWhiteMessage(player,
                " .toll : shows current toll you have in you're account.\n" +
                " .queue : registers you in an on-going event hosted by a gm.\n" +
				" .remodel : cross remodel with use of tiamat bloody tear \n" +
				" .reskinvip : reskin two handed weapons with use of tiamat bloody tear [VIP]\n");

            PacketSendUtility.sendWhiteMessage(player,
                "========================\n" +
                "Available // [add] Commands for Players!" +
                "\n========================\n" +
                " //add <player> <item id> <quantity> : add items\n" +
                " //addset <player> <item id> : add sets to inv\n" +
		        " //add kinah <quantity> : adds kinah to your character\n");
			PacketSendUtility.sendWhiteMessage(player,
			"==========New Commands==========\n" +
			".givepack help : Informs you with some Important Information about how to get Gears.\n" +
			".pet add : Adds You a scroll Buffer Pet.\n");
			
     
	}
			
    }
    public void onFail(Player player, String msg){
        PacketSendUtility.sendWhiteMessage(player, "Syntax : .help");
    }
}

