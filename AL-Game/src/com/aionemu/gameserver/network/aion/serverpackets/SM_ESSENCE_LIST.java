/**
 * This file is part of Archsoft Development.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.cp.PlayerCpEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Xnemonix
 */
public class SM_ESSENCE_LIST extends AionServerPacket {

    private PlayerCpEntry[] cpList;


    @SuppressWarnings("unused")
    public SM_ESSENCE_LIST(Player player, PlayerCpEntry[] getAllCps) {
        this.cpList =  player.getCpList().getAllCps();
    }

    @Override
    protected void writeImpl(AionConnection con) {
    	PacketLoggerService.getInstance().logPacketSM(this.getPacketName());
        int size = cpList.length;
        
        writeH(1); 
        writeH(size); 
        for (PlayerCpEntry entry : cpList) {
	        writeD(entry.getSlotId());
	        writeH(entry.getCpPoint());
        }
    }
}
