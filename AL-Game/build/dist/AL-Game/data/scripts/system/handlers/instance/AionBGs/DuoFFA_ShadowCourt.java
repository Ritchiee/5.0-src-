package instance.AionBGs;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.ecfunctions.arena.ArenaManager;
import com.aionemu.gameserver.services.ecfunctions.tvt.TVTService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

@InstanceID(320120000)
public class DuoFFA_ShadowCourt extends ArenaManager {
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
	}
	@Override
	public void onEnterInstance(Player player) {
		player.getController().cancelCurrentSkill();

	}
    @Override
    public boolean isEnemy(Player attacker, Player target) {
        if (attacker != target) {
            return true;
        }
        return super.isEnemy(attacker, target);
    }
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
    	if (player.isInDuoFFA()) {
    		TVTService.getInstance().onDead(player, lastAttacker); 
    	} else {
            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
            PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));
    	}
		return true;
    }
    @Override
    public boolean onReviveEvent(Player player) {
 if (player.isInDuoFFA()) {
    		TVTService.getInstance().onRevive(player);
    	}
        return true;
    }
    @Override
    public void onPlayerLogOut(Player player) {
   TeleportService2.moveToBindLocation(player, false);
    }
}