package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

import java.util.Iterator;

/**
 * Created by Kill3r
 */
public class UrlLink extends AdminCommand  {
    public UrlLink() {
        super("urllink");
    }
    public void execute(Player player,String...params){
        if(params.length < 1){
            onFail(player, "");
        }
        if(params[0].equals("send")){
            if(params[1].equals("all")){
                try{
                    String url = params[2];

                    Iterator<Player> ita = World.getInstance().getPlayersIterator();

                    while(ita.hasNext()){
                        Player p1 = ita.next();
                            HTMLService.showHTML(p1, "<poll>\n" +
                                    "\t<poll_introduction>\n" +
                                    "\t\t\n" +
                                    "\t\t<![CDATA[<h1 style=\"text-align: center;\"><span style=\"color:#FF0000;\">Insane Aion URL Maker!</span></h1>]]>\n" +
                                    "\t</poll_introduction>\n" +
                                    "\t<poll_title>\n" +
                                    "\t\t<font color='ffc519'></font>\n" +
                                    "\t</poll_title>\n" +
                                    "\t<questions><question>\n" +
                                    "\t<title>\n" +
                                    "\t\t<![CDATA[\n" +
                                    "<p style=\"margin-left: 40px;\"><span style=\"color:#000000;\"><strong>Here&#39;s the URL that was Created :</strong></span></p>\n" +
                                    "\n" +
                                    "<p style=\"margin-left: 40px;\"><span style=\"color:#000000;\"><strong>You can go to this URL by just simply clicking it. :)</strong></span></p>\n" +
                                    "\n" +
                                    "<p style=\"margin-left: 40px;\"><strong><span style=\"color:#0000FF;\"><a href=\""+url+"\">"+url+"</a></span></strong></p>\n" +
                                    "\n" +
                                    "<p style=\"margin-left: 40px;\"><strong><span style=\"color:#FF0000;\">Thanks Insane PvPers!!</span></strong></p>" +
                                    "\t\t]]>\n" +
                                    "\t</title>\n" +
                                    "\t<select>\n" +
                                    "\t\t<input type='radio'>Thanks for the awesome URL</input>\n" +
                                    "\t</select>\n" +
                                    "\t</question></questions>\n" +
                                    "</poll>\n");
                            PacketSendUtility.sendMessage(player, "This player got: " + p1.getName());
                    }
                }catch (NumberFormatException e){
                    PacketSendUtility.sendMessage(player, "Wrong URL or Missing Parameters");
                }
            }else if(params[1].equals("inzone")){
                try{
                    String url = params[2];

                    Iterator<Player> ita = World.getInstance().getPlayersIterator();

                    while(ita.hasNext()){
                        Player p1 = ita.next();
                        if(player.getWorldId() == p1.getWorldId()){
                            HTMLService.showHTML(p1, "<poll>\n" +
                                    "\t<poll_introduction>\n" +
                                    "\t\t\n" +
                                    "\t\t<![CDATA[<h1 style=\"text-align: center;\"><span style=\"color:#FF0000;\">Insane Aion URL Maker!</span></h1>]]>\n" +
                                    "\t</poll_introduction>\n" +
                                    "\t<poll_title>\n" +
                                    "\t\t<font color='ffc519'></font>\n" +
                                    "\t</poll_title>\n" +
                                    "\t<questions><question>\n" +
                                    "\t<title>\n" +
                                    "\t\t<![CDATA[\n" +
                                    "<p style=\"margin-left: 40px;\"><span style=\"color:#000000;\"><strong>Here&#39;s the URL that was Created :</strong></span></p>\n" +
                                    "\n" +
                                    "<p style=\"margin-left: 40px;\"><span style=\"color:#000000;\"><strong>You can go to this URL by just simply clicking it. :)</strong></span></p>\n" +
                                    "\n" +
                                    "<p style=\"margin-left: 40px;\"><strong><span style=\"color:#0000FF;\"><a href=\""+url+"\">"+url+"</a></span></strong></p>\n" +
                                    "\n" +
                                    "<p style=\"margin-left: 40px;\"><strong><span style=\"color:#FF0000;\">Thanks Insane PvPers!!</span></strong></p>" +
                                    "\t\t]]>\n" +
                                    "\t</title>\n" +
                                    "\t<select>\n" +
                                    "\t\t<input type='radio'>Thanks for the awesome URL</input>\n" +
                                    "\t</select>\n" +
                                    "\t</question></questions>\n" +
                                    "</poll>\n");
                            PacketSendUtility.sendMessage(player, "The Players : " + p1.getName());
                        }
                    }
                }catch (NumberFormatException e){
                    PacketSendUtility.sendMessage(player, "Wrong URL or Missing Parameters");
                }
            }else{
                PacketSendUtility.sendMessage(player, "Missing parameters");
                onFail(player, "");
            }
        }
    }

    public void onFail(Player player, String msg){
        PacketSendUtility.sendMessage(player, "synax !!" +
                "\n//urllink send <all/inzone> URLHERE" +
                "\n\nExample !" +
                "\n//urllink send all http://www.insaneaion.com/" +
                "\nNOTE: Remember to use http:// or it will not work!");
    }
}

