package playercommands;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Kill3r
 */
public class FixWep extends PlayerCommand {

    public FixWep(){
        super("fixwep");
    }

    @Override
    public void execute(Player player, String... params) {
        Item mat, tool, wep;
        if (player.getEquipment().getOffHandWeapon() != null){
            PacketSendUtility.sendMessage(player, "Please unequip offhand weapon and use it on main hand while using the command.");
            PacketSendUtility.sendMessage(player, "You can only use the command per weapon, not both at once.");
            return;
        }
        if (player.getEquipment().getMainHandWeapon().isAmplified()){
            wep = player.getEquipment().getMainHandWeapon();
            if(wep.getBuffSkill() != 0){
                PacketSendUtility.sendMessage(player, "You can only use this command once and if the main hand weapon has no buff skill!");
                return;
            }
        }else{
            PacketSendUtility.sendMessage(player, "You're main hand wep is not amplified!");
            return;
        }

        ItemService.addItem(player, 166500002, 1);//material
        ItemService.addItem(player, 165030001, 1); // tool

        mat = player.getInventory().getFirstItemByItemId(166500002);
        tool = player.getInventory().getFirstItemByItemId(165030001);



        EnchantService.amplifyItem(player, wep, mat, tool);
    }
}
