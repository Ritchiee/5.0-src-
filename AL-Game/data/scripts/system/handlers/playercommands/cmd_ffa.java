package playercommands;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.ecfunctions.ffa.FFAService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Ghostfur
 */

public class cmd_ffa extends PlayerCommand {
    
    public cmd_ffa() {
        super("ffa");
    }

    private int count;
    public String worldName;
    private WorldMapInstance instance;
	
    @Override
    public void execute(final Player player, String... params) {
        if (params.length < 1 || params == null) {
            onFail(player, null);
            return;
        }
         
       if (params[0].equals("enter")) {
    	   
    	   PacketSendUtility.sendMessage(player, "FFA: Please wait 5 seconds ");
    	   PVPManager.getInstance().paralizePlayer(player, true);
    	   PlayerGroupService.removePlayer(player); //remove player from group in FFA
    	   PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(5), 0, 0));
           ThreadPoolManager.getInstance().schedule(new Runnable() {
               @Override
               public void run() {
               	   PVPManager.getInstance().paralizePlayer(player, false);
                   PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, 0, 1, 0));
                   FFAService.getInstance().TeleIn(player);  
               }
           }, (int) TimeUnit.SECONDS.toMillis(5));
      	}	  
        	
       if (params[0].equals("leave")) {
    	   
        	PacketSendUtility.sendMessage(player, "FFA: Please wait 5 seconds.");
        	PVPManager.getInstance().paralizePlayer(player, false);
        	PlayerGroupService.removePlayer(player); //remove player from group in FFA
            PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(5), 0, 0));
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, 0, 1, 0));
                    FFAService.getInstance().TeleOut(player);  
                }
            }, (int) TimeUnit.SECONDS.toMillis(5));
       	}
       
       if (params[0].equals("info")) {
        PacketSendUtility.sendMessage(player, "Total Number Of Players Currently In FFA: " + getPlayerCountFFA(instance) + "\n Instance Name: " + worldName );
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

	public void onFail(Player player, String msg){
        PacketSendUtility.sendMessage(player, " " +
                "synax : .ffa enter -- Join Free For All \n " +
                ".ffa leave -- Leave Free For All \n " +
                ".ffa info -- Shows How Many Online + Information ");
    }
}
