/*
 * This file is part of aion-engine <aion-engine.net>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package playercommands;

import java.util.concurrent.TimeUnit;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.ecfunctions.tvt.TVTService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Ghostfur
 */
public class cmd_2v2 extends PlayerCommand {
    
    public cmd_2v2() {
        super("2v2");
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
			
		  	   if (!player.isInGroup2()) {
		           	PacketSendUtility.sendMessage(player, "Only group allow to joining 2v2v2.");
		           	return;
		           }
		  	   
		  	   if (player.isInAlliance2()){
	            	PacketSendUtility.sendMessage(player, "Only group allow to joining 2v2v2. Exit alliance and make group");
	            	return;
	               }  
            
    	   PacketSendUtility.sendMessage(player, "2v2v2: Please wait 5 seconds ");
    	   PVPManager.getInstance().paralizePlayer(player, true);
		   //group access need to be added
    	   PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(5), 0, 0));
           ThreadPoolManager.getInstance().schedule(new Runnable() {
               @Override
               public void run() {
               	   PVPManager.getInstance().paralizePlayer(player, false);
                   PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, 0, 1, 0));
                   TVTService.getInstance().TeleIn(player);  
               }
           }, (int) TimeUnit.SECONDS.toMillis(5));
      	}	  
			
		if (params[0].equals("leave")) {
        	PacketSendUtility.sendMessage(player, "2v2v2: Please wait 5 seconds.");
        	PVPManager.getInstance().paralizePlayer(player, false);
        	PlayerGroupService.removePlayer(player); //remove player from group in 2v2v2
            PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(5), 0, 0));
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                	PVPManager.getInstance().paralizePlayer(player, false);
                    PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, 0, 1, 0));
                    TVTService.getInstance().TeleOut(player);  
                }
            }, (int) TimeUnit.SECONDS.toMillis(5));
       }

	       if (params[0].equals("info")) {
	           PacketSendUtility.sendMessage(player, "Total Number Of Players Currently In FFA: " + getPlayerCountDFFA(instance) + "\n Instance Name: " + worldName );
	          			}
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
 
	public void onFail(Player player, String msg){
        PacketSendUtility.sendMessage(player, " " +
                "synax : .2v2 enter -- Join the 2v2 Battles \n " +
                ".2v2 leave -- Leaves the FFA \n " +
                ".2v2 info -- shows how many online ");
    }
}
