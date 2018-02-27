package playercommands;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;


/**
 * Created by Kill3r
 */
public class Enchantme extends PlayerCommand {
    public Enchantme() {
        super("enchantme");
    }



    public void execute(final Player player, String... params){

        final int enchantcount = 15;
        final int price = 10000000;
        if (player.getInventory().getKinah() < price){
            PacketSendUtility.sendMessage(player, "You can't use the command.\nYou need 10,000,000 Kinah to Enchant your full gear.");
            return;
        }else{



            RequestResponseHandler RequestHim = new RequestResponseHandler(player) {
                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    enchant(player, enchantcount);
                    PacketSendUtility.sendMessage(player, "You have successfully enchanted your gear !!");
                    player.getInventory().decreaseKinah(price);
                }



                @Override
                public void denyRequest(Creature requester, Player responder) {
                    PacketSendUtility.sendMessage(player, "You canceled Enchanting Request!");
                    return;
                }
            };

            boolean areyousure = player.getResponseRequester().putRequest(1300564, RequestHim);
            if (areyousure){
                PacketSendUtility.sendPacket(player,new SM_QUESTION_WINDOW(1300564, 0, 0, "Are you Sure you want to Enchant the Gear?<b>     Required Kinah : 10,000,000"));
            }

        }

    }

    private void enchant(Player player, int enchant) {
        for (Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
            if (isUpgradeble(targetItem)) {
                if (targetItem.getEnchantLevel() == enchant)
                    continue;
                if (enchant > 15)
                    enchant = 15;
                if (enchant < 0)
                    enchant = 0;

                targetItem.setEnchantLevel(enchant);
                if (targetItem.isEquipped()) {
                    player.getGameStats().updateStatsVisually();
                }
                ItemPacketService.updateItemAfterInfoChange(player, targetItem);
            }
        }

    }

    public static boolean isUpgradeble(Item item) {
        if (item.getItemTemplate().isNoEnchant())
            return false;
        if (item.getItemTemplate().isWeapon())
            return true;
        if (item.getEnchantLevel() == 15) {
            return false;
        }
        if (item.getEnchantLevel() >= item.getItemTemplate().getMaxEnchantLevel() && item.isAmplified()) {
            return false;
        }
        if (item.getItemTemplate().isArmor()) {
            int at = item.getItemTemplate().getItemSlot();
            if (at == 1 || /* Main Hand */
                    at == 2 || /* Sub Hand */
                    at == 8 || /* Jacket */
                    at == 16 || /* Gloves */
                    at == 32 || /* Boots */
                    at == 2048 || /* Shoulder */
                    at == 4096 || /* Pants */
                    at == 131072 || /* Main Off Hand */
                    at == 262144) /* Sub Off Hand */
                return true;
        }
        return false;

    }
}
