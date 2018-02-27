package ai.custom;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.services.ecfunctions.customportals.SiegePortal;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI2;

/**
 *
 * @author Ghostfur
 */
@AIName("siege_portal")
public class NewSiegeAI2 extends ActionItemNpcAI2 {   
    @Override
    protected void handleDialogStart(final Player player) {
        Npc owner = getOwner();        
        int memberBoss = 4;
        if (owner.getMasterName().equalsIgnoreCase("PVP-SIEGE")) {    
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
                PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0,  "In Siege, the other faction is your enemy. Do you want to join Siege? <br><br><br>Location: Gelkmaros"));
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
      if (owner.getMasterName().equalsIgnoreCase("PVP-SIEGE")) {   
			if (player.battlegroundWaiting) {
        		ArenaService.getInstance().unRegister(player);
        	}	  
			SiegePortal.getInstance().TeleSiege(player);	     	
       }
       super.handleUseItemFinish(player);
    }    

}