package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemChargeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by Kill3r
 */
public class Augmentme extends PlayerCommand {
    public Augmentme() {
        super("augmentme");
    }
    public void execute(Player player, String...params){

        ItemChargeService.chargeItems(player,player.getEquipment().getEquippedItems(), 2);
        PacketSendUtility.sendMessage(player, "You've Successfuly Augmented you're Gear!");
        player.getInventory().decreaseKinah(500000);
    }
}
