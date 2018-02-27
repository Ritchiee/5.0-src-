package ai.custom;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaService;
import com.aionemu.gameserver.services.ecfunctions.tvt.TVTService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI2;

/**
 *
 * @author Ghostfur
 */
@AIName("pvp_portal")
public class NewDuoFFAAI2 extends ActionItemNpcAI2 {   
    @Override
    protected void handleDialogStart(final Player player) {
        Npc owner = getOwner();        
        int memberBoss = 4;
        if (owner.getMasterName().equalsIgnoreCase("2v2v2-" + TVTService.getInstance().worldName)) {
            if (player.getController().isInCombat() 
        		|| player.isInPkMode() 
        		|| player.isInArena() 
        		|| player.isInFFA() 
        		|| player.isInPrison() 
        		|| player.getLifeStats().isAlreadyDead()) {
	            PacketSendUtility.sendMessage(player, "You can not participate in 2v2v2, while dead/in prison/in combat/in another battleground.");
	            return;
            }       
            if (player.getAccessLevel() > 0 && player.getAccessLevel() <= 5) {
            	PacketSendUtility.sendMessage(player, "GM cannot join in 2v2v2. Log in normal account.");
            	return;
            }   

            if (player.isInAlliance2()){
            	PacketSendUtility.sendMessage(player, "Only group allow to joining 2v2v2. Exit alliance and make group");
            	return;
            }  
            
            if (!player.isInGroup2()) {
            	PacketSendUtility.sendMessage(player, "Only group allow to joining 2v2v2.");
            	return;
            }
            
            PlayerGroup group = player.getPlayerGroup2();

            if (group.size() > 2) {
	            PacketSendUtility.sendMessage(player,"Group size is limited to 2 members.");
	            return;	 
            }
        	if (player.battlegroundWaiting) {
        		ArenaService.getInstance().unRegister(player);
        	}
            RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
                @Override
                public void acceptRequest(Creature p2, Player p) {
                    start(p);
                }
                @Override
                public void denyRequest(Creature p2, Player p) {
                }
            };
            boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
            if (requested) {
                PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0,  "In 2v2v2 event, everyone is your enemy except your team mate. No teaming/hack/cheat allow Do you want to join 2v2v2?<br><br><br>Location: " + TVTService.getInstance().worldName + "<br>Total Player: " + TVTService.getInstance().getDFFASize()));
            }            
            return;
        }
        super.handleDialogStart(player);
    }

    private void start(Player player) {
        super.handleDialogStart(player);
    }

    @Override
    protected void handleUseItemFinish(Player player) {
      Npc owner = getOwner();        
      if (owner.getMasterName().equalsIgnoreCase("2v2v2-" + TVTService.getInstance().worldName)) {
        	PlayerGroup group = player.getPlayerGroup2();        	
        	for (Player member : group.getMembers()) {    
	        	member.setPrevLoc();        
    		}        	
        	TVTService.getInstance().TeleIn(player);           	
       }
       super.handleUseItemFinish(player);
    }    

}
