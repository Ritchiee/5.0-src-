package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by Kill3r
 */
public class Showgp extends PlayerCommand {
    public Showgp() {
        super("showgp");
    }

    public void execute(Player player, String...params){
        int gp = player.getAbyssRank().getGp();

        PacketSendUtility.sendMessage(player, "You have "+gp+" in total!");
    }


}
