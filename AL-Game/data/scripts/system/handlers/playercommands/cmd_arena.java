package playercommands;

import java.util.concurrent.TimeUnit;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

public class cmd_ffa extends PlayerCommand {

    public cmd_ffa() {
        super("ffa");
    }

    @Override
    public void execute(final Player player, String... params) {     
        
        if (player.isInFFA()) {        	
        	if (player.getLifeStats().isAlreadyDead()) {
        		PacketSendUtility.sendMessage(player, "FFA: You cannot use this command while dead.");
				player.setCoolDownZero(false);
        		return;
        	}
        	
        	PVPManager.getInstance().AddProtection(player, 6000);
        	
          	if (player.canLeaveArena() && player.getEnemy().getLifeStats().isAlreadyDead()) {    		
        		PacketSendUtility.sendYellowMessageOnCenter(player, "You are the winner of Arena.");
        		player.setCanWinArena(true);
        	} else {
         		PacketSendUtility.sendYellowMessageOnCenter(player, String.format("You have lost."));
        	}
        	
        	PVPManager.getInstance().paralizePlayer(player, true);        	
	        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(5), 0, 0));
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                	PVPManager.getInstance().paralizePlayer(player, false);
                	if (!player.canLeaveArena()) {    	
                		PacketSendUtility.sendSpecMessage("Arena", String.format("%s gives up the solo battle. Type '.arena' to join again.", player.getName()));
                	} 
                	PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, 0, 0, 1, 0));
                    ArenaService.getInstance().TeleOut(player);
                }
            }, (int) TimeUnit.SECONDS.toMillis(5));
            return;
        } else {                	
        	if (player.getController().isInCombat() 
        			|| player.isInPkMode() 
        			|| player.isInDuoFFA() 
        			|| player.isInPrison()
        			|| player.getLifeStats().isAlreadyDead()) {
        		PacketSendUtility.sendMessage(player, "You can not participate in ffa, while dead/in prison/in combat/in another battleground.");
                return;
            }    		
            if (player.getAccessLevel() > 0 && player.getAccessLevel() <= 5) {
            	PacketSendUtility.sendMessage(player, "GM cannot join in ffa. Log in normal account.");
            	return;
            }                     
	        if (player.arenaWaiting) {
	        	ArenaService.getInstance().unRegister(player);
	        	PacketSendUtility.sendBrightYellowMessageOnCenter(player, String.format("FFA: %s removed from the waiting list. Type '.arena' to join again.", player.getName()));
	        } else {
 		
        		//remove from group and alliance
                if (player.isInGroup2()) {            	
                	PlayerGroupService.removePlayer(player);
                }
                else if (player.isInAlliance2()){
                	PlayerAllianceService.removePlayer(player);
                }                
	        	player.arenaWaiting = true;
	        	ArenaService.getInstance().register(player);
	        }
        }
	}
    @Override
    public void onFail(Player player, String message) {
        PacketSendUtility.sendMessage(player, "Syntax: Type '.ffa' to signup/cancel.");
    }
}