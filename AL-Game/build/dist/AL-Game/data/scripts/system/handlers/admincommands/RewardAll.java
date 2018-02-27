package admincommands;


import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

import java.util.Iterator;

/**
 * Created by Kill3r
 */
public class RewardAll extends AdminCommand {
    public RewardAll() {
        super("rewardall");
    }
    public void execute(Player player, String... params){




        long itemCount = 1;
        itemCount = Long.parseLong(params[1]);
        int TollAmount = 1;
        TollAmount = Integer.parseInt(params[1]);
        int ss = 160009017;
        int ItemToAdd = 0;

        ItemToAdd = Integer.parseInt(params[1]);


        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        Iterator<Player> Newiter = World.getInstance().getPlayersIterator();

        if (params.length < 2){
            onFail(player, null);

        }

        if (params[0].equals("any")){
            long newItem = 1;
            newItem = Long.parseLong(params[2]);
            if (player.getAccessLevel() <= 1){
                PacketSendUtility.sendWhiteMessage(player, "You need more Access Level to execute this Command.");
                return;
            }



            while (iter.hasNext()){
                ItemService.addItem(iter.next(), ItemToAdd,newItem);
            }

            while (Newiter.hasNext()){
                PacketSendUtility.sendMessage(Newiter.next(), "You have been rewarded " + newItem + " x [item:" + ItemToAdd + "] from GM " + player.getName());
            }

            PacketSendUtility.sendMessage(player, "You have successfully added the items to all players.");

        }else if (params[0].equals("food")){
            if (itemCount > 5){
                PacketSendUtility.sendMessage(player, "You cannot send more than 5 food at same time.\nChanging the value to 5 and sending the items.");
                itemCount = 5;
            }else{

            }
            while (iter.hasNext()){
                ItemService.addItem(iter.next(),160009017,itemCount);
            }
            while (Newiter.hasNext()){
                PacketSendUtility.sendMessage(iter.next(), "You have been rewarded "+ itemCount +" x [item:" + ss + "] from GM "+ player.getName());
            }

            PacketSendUtility.sendMessage(player, "You have successfully added the items to all players.");

        }else if (params[0].equals("cera")){
            while (iter.hasNext()){
                ItemService.addItem(iter.next(),186000242,itemCount);

            }
            while (Newiter.hasNext()){
                PacketSendUtility.sendMessage(Newiter.next(), "You have been rewarded " + itemCount + " x [item:" + 186000242 + "] from GM " + player.getName());
            }
            PacketSendUtility.sendMessage(player, "You have successfully added the items to all players.");
        }else if (params[0].equals("toll")){
            if (player.getAccessLevel() >= 4){

                while (iter.hasNext()){
                    giveToll(iter.next(), TollAmount);
                    PacketSendUtility.sendMessage(Newiter.next(), "You received "+TollAmount+" tolls.");
                }

                PacketSendUtility.sendMessage(player, "You have successfully added the items to all players.");
            }else{
                PacketSendUtility.sendMessage(player, "## Sending toll requires Access level 3 or higher."
                        + "\n Access Levels"
                        + "\n 1 : TrialGM\n 2 : GM\n 3 : HeadGM 4 : Dev\n 5 : InsaneGod");

                return;
            }

        }else{

            PacketSendUtility.sendMessage(player,"An Error Occured");

        }


    }

    private void giveToll(Player player, int toll ){
        InGameShopEn.getInstance().addToll(player, toll);
    }
    public void onFail(Player player, String message){
        PacketSendUtility.sendMessage(player, "Synax : //rewardall <any| food | toll | cera> <value>"
                + "\n any : \"any\" is for adding any item by giving itemID.\n//rewardall any (ItemId) (itemCount)\n" +
                "\n food : \"food\" is for giving Vinna Juice to everyone." +
                "\n toll : \"toll\" is for giving Tolls to everyone with a given amount." +
                "\n cera : \"cera\" is for giving Ceramium Medals to everyone with a given amount.\n food,Toll,Cera will work like this\n //rewardall food <itemCount>");
    }
}
