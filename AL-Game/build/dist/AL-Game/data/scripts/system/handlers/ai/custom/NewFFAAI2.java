package ai.custom;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaService;
import com.aionemu.gameserver.services.ecfunctions.ffa.FFAService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI2;

/**
 *
 * @author Ghostfur
 */
@AIName("ffa_portal")
public class NewFFAAI2 extends ActionItemNpcAI2 {   
    @Override
    protected void handleDialogStart(final Player player) {
        Npc owner = getOwner();        
        int memberBoss = 4;
        if (owner.getMasterName().equalsIgnoreCase("FFA-" + FFAService.getInstance().worldName)) {  
        	  if (player.getController().isInCombat() 
          		|| player.isInPkMode() 
          		|| player.isInArena() 
          		|| player.isInDuoFFA() 
          		|| player.isInPrison() 
          		|| player.getLifeStats().isAlreadyDead()) {
  	            PacketSendUtility.sendMessage(player, "You can not participate in FFA, while dead/in prison/in combat/in another battleground.");
  	            return;
              }    
			 if (player.isInGroup2()){
              	PacketSendUtility.sendMessage(player, "Only solo allowed to join FFA, leave group to join");
              	return;
              }  
        	  if (player.getAccessLevel() > 0 && player.getAccessLevel() <= 5) {
              	PacketSendUtility.sendMessage(player, "GM cannot join in ffa. Log in normal account.");
              	return;
              }   

              if (player.isInAlliance2()){
              	PacketSendUtility.sendMessage(player, "Only solo allowed to join FFA, leave Alliance to join");
              	return;
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
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0,  "In Free For All, everyone is your enemy. No teaming/hack/cheat allow  Do you want to join FFA?<br><br><br>Location: " + FFAService.getInstance().worldName + "<br>Total Player: " + FFAService.getInstance().getFFASize()));            
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
      if (owner.getMasterName().equalsIgnoreCase("FFA-" + FFAService.getInstance().worldName)) { 
		if (player.battlegroundWaiting) {
        		ArenaService.getInstance().unRegister(player);  
    		}        	
        	FFAService.getInstance().TeleIn(player);           	
       }
       super.handleUseItemFinish(player);
    }    

}
