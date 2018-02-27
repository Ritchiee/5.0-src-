package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.services.item.ItemService;



/**
 * Created By Ataba
 */
public class pet extends PlayerCommand {	
	public pet() {
		super("pet");
	}
	//@Override
	//public void execute(Player player, String...params){
	//	if(params[0].equals("add")){
	//		ItemService.addItem(player,190000000, 1); //Pet
	//		PacketSendUtility.sendMessage(player, "\uE020 You Just Added a Buffer Pet! \uE020");
	//	}
	// }
		@Override
		public void execute(final Player player, String...param){
        if(param.length < 1){
            PacketSendUtility.sendMessage(player, " You can always remove the remaining items:\n .givestigma clean\n .givestigma add\n .givestigma unlock\n .givestigma class");
            return;
			}
	if(param[0].equals("add")){
      		ItemService.addItem(player,190000000, 1); //Pet
			PacketSendUtility.sendMessage(player, "\uE020 You Just Added a Buffer Pet! \uE020");
		}
	 }
	
    public void onFail(Player player, String msg){
        PacketSendUtility.sendMessage(player, " " +
                "synax : .pet add  -- To Add a Buffer Pet\n");
    }
}

