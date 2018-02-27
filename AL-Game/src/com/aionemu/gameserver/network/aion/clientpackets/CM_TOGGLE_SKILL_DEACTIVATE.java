/**
*
* Develpment Reword By Archsoft
*
*
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STANCE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class CM_TOGGLE_SKILL_DEACTIVATE extends AionClientPacket {

    private int skillId;

    public CM_TOGGLE_SKILL_DEACTIVATE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        PacketLoggerService.getInstance().logPacketCM(this.getPacketName());
        skillId = readH();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
    	int[] ToggleBuff = {2440, 2441, 2442, 2443, 2444, 2445, 2446, 2447, 2448, 2449, 2579, 2580, 2581,2421 ,2422, 2736, 2737, 2738, 2739, 2740}; //Toggle With Buff Slot
    	
        Player player = getConnection().getActivePlayer();
        
        for (int id : ToggleBuff){
            if (id == skillId){
               player.getEffectController().removeEffect(skillId);
            }else{
            	player.getEffectController().removeNoshowEffect(skillId);
            }
        }


        if (player.getController().getStanceSkillId() == skillId) {
            PacketSendUtility.sendPacket(player, new SM_PLAYER_STANCE(player, 0));
            player.getController().startStance(0);
        }
    }
}