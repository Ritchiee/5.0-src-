package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by Kill3r
 */
public class Skin extends PlayerCommand {
    public Skin(){
        super("skin");
    }

    public void execute(Player player, String...params){
        int skin = 0;
        player.getTransformModel().setModelId(skin);
        PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));

        PacketSendUtility.sendMessage(player, "You have removed the candy form but you will still have the stats.");

    }
}
