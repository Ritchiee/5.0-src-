package playercommands;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.CubeExpandService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Kill3r
 */


public class cmd_unlockcube extends PlayerCommand {

    public cmd_unlockcube() {
        super("unlockcube");
    }

    public void execute(Player player, String... params){


            if (player.getNpcExpands() < CustomConfig.BASIC_CUBE_SIZE_LIMIT){

                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);
                CubeExpandService.expand(player, true);

                PacketSendUtility.sendMessage(player,"You have successfully added 9 cubes!");
            }else{
                PacketSendUtility.sendMessage(player, "You have fully unlocked the cube!\nThere is no more cubes to unlock!");
                return;
            }
        } 



    public void execute(Player player, String msg){
        PacketSendUtility.sendMessage(player, "synax : .unlockcube");
        return;
    }

}
