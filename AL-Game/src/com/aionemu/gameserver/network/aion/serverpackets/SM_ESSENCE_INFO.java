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
public class SM_ESSENCE_INFO extends AionServerPacket {

    private PlayerCpEntry[] cpList;
    boolean isNew = false;
    Player player;


    @SuppressWarnings("unused")
    public SM_ESSENCE_INFO(Player player, PlayerCpEntry[] getAllCps) {
        this.cpList =  player.getCpList().getAllCps();
        this.player = player;
    }

    public SM_ESSENCE_INFO(Player player, PlayerCpEntry cpListEntry, boolean isNew) {
        this.cpList = new PlayerCpEntry[]{cpListEntry};
        this.isNew = isNew;
        this.player = player;
    }

    @Override
    protected void writeImpl(AionConnection con) {
    	PacketLoggerService.getInstance().logPacketSM(this.getPacketName());
    	 int size =  0;
         
         if (cpList !=null)
         	size = cpList.length;
         
        writeH(player.getCommonData().getMaxCp()); 
        writeH(0); //Unk
        if (size <=0){ 
        	writeH(0); 
        }else {
        	writeH(size); 
                 for (PlayerCpEntry entry : cpList) {
                     writeD(entry.getSlotId());
                     writeH(entry.getCpPoint());
                 }
             }
        }
}
