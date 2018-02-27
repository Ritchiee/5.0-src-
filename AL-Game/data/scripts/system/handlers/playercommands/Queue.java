package playercommands;

import java.util.Collection;
import java.util.Iterator;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;


/**
 * Created by Kill3r
 */
public class Queue extends PlayerCommand {

    public Queue(){
        super("queue");
    }

    public void execute(Player player,String...params){
        boolean anyEventfound = false;
        if(player.isRegedEvent()){
            PacketSendUtility.sendMessage(player, "You've already registered to the event!");
            return;
        }
        if(player.isInPrison()){
            PacketSendUtility.sendMessage(player, "You cant register inside Prison!");
            return;
        }
        if(player.isInFFA()){
            PacketSendUtility.sendMessage(player, "You cannot register while in FFA Arena");
            return;
        }
        Iterator<Player> ita = World.getInstance().getPlayersIterator();

        while(ita.hasNext()){
            Player player1 = ita.next();

            if(player1.getAccessLevel() >= 2 && player1.isEventStarted()){
                int playerCounter = player1.getountPlayers();
                if (player1.getountPlayers() != checkRegedPlayers()){
                    PacketSendUtility.sendMessage(player, "Found an Event! Registering to " + player1.getName() + "'s Event!");
                    anyEventfound = true;
                    if (player1.getountPlayers() != 500){
                        checkPosition(player1, player, playerCounter);
                    }
                }else{
                    PacketSendUtility.sendMessage(player, "Sorry all slots are taken now! Better luck next time <3");
                }
            }
        }

        // Find a way to fix the problem when u type .queue when the gm has enabled the registration for unlimited players..
        // since the getCountplayers are set to a value when .queue is working.. and it ends when its 0.. and by default its by 0..

        if(anyEventfound == true){
            PacketSendUtility.sendMessage(player, "You've registered to the upcoming event!");
            player.setRegedEvent(true);
        }else{
            PacketSendUtility.sendMessage(player, "Currently there are no event running!");
        }
    }

    private void checkPosition(Player admin, Player oneWhoExecute, int countPosition){
        int count = admin.getountPlayers();
        count = count - checkRegedPlayers();

        PacketSendUtility.sendMessage(oneWhoExecute, "There are " + count + " slots remaining!");
    }

    private int checkRegedPlayers(){
        int count = 0;
        Collection<Player> players = World.getInstance().getAllPlayers();
        for(Player p : players){
            if(p.isRegedEvent()){
                count = count + 1;
            }
        }
        return count;
    }
}
