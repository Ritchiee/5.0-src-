/**
 *
 * @author Xnemonix
 */

package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.cp.PlayerCpList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.services.CombatPointService;
import com.aionemu.gameserver.skillengine.model.SkillEnchantTemplate;


/**
 *
 * @author Xnemonix
 */
public class CM_UPDATE_CP extends AionClientPacket {

	private int action;
	private int slotId;
	private int cpPoint;
	private int size;
	private Player activePlayer;

	public CM_UPDATE_CP(int opcode, AionConnection.State state, AionConnection.State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		activePlayer = getConnection().getActivePlayer();
		PlayerCpList playerCpList = activePlayer.getCpList();
		action = readC(); // 1:Reset || 0: Add
		size = readH();
	
		if (action == 0){
			for (int i = 0; i < size; i++) { 
				slotId = readD(); 
				cpPoint = readH(); 
				SkillEnchantTemplate[] slotIds = DataManager.SKILL_ENCHANT_DATA.getTemplatesForGroup(slotId);
				if (slotIds[0].getCategory().equals("stat_up")){
					playerCpList.setCpStat(activePlayer, slotId, cpPoint);
				}else if (slotIds[0].getCategory().equals("learn_skill")){	
					playerCpList.setCpLearn(activePlayer, slotId, cpPoint);
				}else{
					playerCpList.setCpEnchant(activePlayer, slotId, cpPoint);
				}
			}
		}else{
			CombatPointService.getInstance().resetCp(activePlayer);
		}
		
	}

	@Override
	protected void runImpl() {
		
		if(action == 0){
			CombatPointService.getInstance().executeCp(activePlayer);
		}else{
			CombatPointService.getInstance().resetCp(activePlayer);
		}
		
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
