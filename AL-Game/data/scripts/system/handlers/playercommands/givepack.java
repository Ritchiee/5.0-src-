package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.services.item.ItemService;


/**
 * Created by Ataba
 */
public class givepack extends PlayerCommand {

    public givepack() {
        super("givepack");
    }

	@Override
	public void execute(final Player player, String...param){
    if(param.length < 1){
        PacketSendUtility.sendMessage(player, "synax : .givepack help");
        return;
		}
					
    if(param[0].equals("help")){
			PacketSendUtility.sendMessage(player, "--------\uE079Givepack\uE079--------\n" +
			"<Glad\uE079Pack> .givepack Gladiator\n" +
			"<Temp\uE079Pack> .givepack Templar\n" +
			"<Clrc\uE079Pack> .givepack Cleric\n" +
			"<Chtr\uE079Pack> .givepack Chanter\n" +
			"<Sin\uE079Pack> .givepack Assassin\n" +
			"<Rgr\uE079Pack> .givepack Ranger\n" +
			"<Sorc\uE079Pack> .givepack Sorcerer\n" +
			"<Sm\uE079Pack> .givepack Spiritmaster\n" +
			"<Gun\uE079Pack> .givepack Gunner\n" +
			"<A.Tech\uE079Pack> .givepack Aethertech\n" +
			"<Sw\uE079Pack> .givepack Songweaver\n" +
			"------------------------------------------------------------------\n" +
			"For Food, Juice, Candy, Scrolls, Potions, and Other Stuff..\n" +
			"Type This Commands :\n" +
			"For Combat [ .givepack Combat ]\n" +
			"For Magic [ .givepack Magic ]\n" +
			"For Both [ .givepack me ]\n" +
			"------------------------------------------------------------------\n" +
			"If You Can't Discard Items Like Plume Type [ .clean @Link ]\n" +
			"Make Sure You Type the First Letter with Capital, and also make sure to have Space In Your Inventory, if Not, Type [ .unlockcube ] !\n" +
			"\uE020 Good Luck, And Stay Awesome! \uE020");
		}
		

		if(param[0].equals("Godstone")){
			ItemService.addItem(player,168000162, 10); //<Illusion Godstone: Ereshkigal's Honor>
			ItemService.addItem(player,168000164, 10); //<Illusion Godstone: Beritra's Plot>
			ItemService.addItem(player,168000165, 10); //<Illusion Godstone: Fregion's Trick>
			ItemService.addItem(player,168000166, 10); //<Illusion Godstone: Lumiel's Intervention>
			ItemService.addItem(player,168000167, 10); //<Illusion Godstone: Kaisinel's Fantasy>
			ItemService.addItem(player,168000161, 10); //<Illusion Godstone: Meslamtaeda's Greed>
			ItemService.addItem(player,168000176, 10); //<Illusion Godstone: Vaizel's Vow>
			ItemService.addItem(player,168000174, 10); //<Illusion Godstone: Tiamat's Fury>
			PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added All The Famous Godstones! \uE022");
		}
		
		if(param[0].equals("Combat")){
			ItemService.addItem(player,164002111, 1000); //<Greater Running Scroll (1 hour)>
			ItemService.addItem(player,164002112, 1000); //<Greater Courage Scroll (1 hour)>
			ItemService.addItem(player,164002114, 1000); //<Major Crit Strike Scroll (1 hour)>
			ItemService.addItem(player,164000259, 1000); //<Premium Anti-Shock Scroll>
			ItemService.addItem(player,164000021, 1000); //<Boost Elemental Defense>
			ItemService.addItem(player,164000114, 1000); //<Fine Fireproof Scroll>
			ItemService.addItem(player,164000115, 1000); //<Fine Earthproof Scroll>
			ItemService.addItem(player,164000116, 1000); //<Fine Waterproof Scroll>
			ItemService.addItem(player,164000117, 1000); //<Fine Windproof Scroll>
			ItemService.addItem(player,164000257, 1000); //<Fine Strike Resist Scroll>
			ItemService.addItem(player,162000083, 1000); //<Leader's Recovery Scroll>
			ItemService.addItem(player,160002475, 1000); //<Fresh Abex Cheese Soup>
			ItemService.addItem(player,160009017, 10); //<Vinna Juice>
			ItemService.addItem(player,162000124, 1000); //<Superior Recovery Serum>
			ItemService.addItem(player,162000121, 1000); //<Superior Recovery Potion>
			ItemService.addItem(player,162000107, 1000); //<Saam King's Herbs>
			ItemService.addItem(player,162000137, 1000); //<Sublime Life Serum>
			ItemService.addItem(player,162000139, 1000); //<Sublime Mana Serum>
			ItemService.addItem(player,160003587, 1000); //<Pepento Jelly>
			ItemService.addItem(player,160010337, 1); //<[Event] Heartfelt Chocolate>
			ItemService.addItem(player,160010334, 1); //<[Event] Constitutional Chocolate>
			ItemService.addItem(player,164000266, 100); //<Enhanced Seed of Detection>
			ItemService.addItem(player,169000011, 20000); //<Ancient Power Shard>
			ItemService.addItem(player,169300007, 20000); //<Master Odella Powder>
			ItemService.addItem(player,166050129, 50); //<Blazing Warlord's Idian: Physical/Magical Attack>
			ItemService.addItem(player,166050137, 50); //<Honorable Elim's Idian: Physical Attack>
			ItemService.addItem(player,161001001, 100); //<Revival Stone>
			ItemService.addItem(player,160010325, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010326, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010329, 1000); //<Kei Form Candy>
			ItemService.addItem(player,160010330, 1000); //<Kei Form Candy>
			ItemService.addItem(player,187060166, 1); //<Trillirunerk's Ice Flame Feather>
			ItemService.addItem(player,187060177, 1); //<Aether Glider>
			ItemService.addItem(player,169610146, 1); //<[Title] Glorious Number One (30 days)>
			ItemService.addItem(player,187100015, 1); //<Kaisinel's Plume: Attack>
			ItemService.addItem(player,187100017, 1); //<Marchutan's Plume: Attack>
			PacketSendUtility.sendMessage(player, "\uE022 Enjoy Being Insane, Insaner! \uE022");
		}
		
		if(param[0].equals("Magic")){
			ItemService.addItem(player,164002111, 1000); //<Greater Running Scroll (1 hour)>
			ItemService.addItem(player,164002113, 1000); //<Greater Awakening Scroll (1 hour)>
			ItemService.addItem(player,164002115, 1000); //<Major Crit Spell Scroll (1 hour)>
			ItemService.addItem(player,164000260, 1000); //<Premium Magic Suppression Scroll>
			ItemService.addItem(player,164000021, 1000); //<Boost Elemental Defense>
			ItemService.addItem(player,164000114, 1000); //<Fine Fireproof Scroll>
			ItemService.addItem(player,164000115, 1000); //<Fine Earthproof Scroll>
			ItemService.addItem(player,164000116, 1000); //<Fine Waterproof Scroll>
			ItemService.addItem(player,164000117, 1000); //<Fine Windproof Scroll>
			ItemService.addItem(player,162000083, 1000); //<Leader's Recovery Scroll>
			ItemService.addItem(player,160002505, 1000); //<Basil Dragon Meat Soup>
			ItemService.addItem(player,160009017, 10); //<Vinna Juice>
			ItemService.addItem(player,162000124, 1000); //<Superior Recovery Serum>
			ItemService.addItem(player,162000121, 1000); //<Superior Recovery Potion>
			ItemService.addItem(player,162000107, 1000); //<Saam King's Herbs>
			ItemService.addItem(player,162000137, 1000); //<Sublime Life Serum>
			ItemService.addItem(player,162000139, 1000); //<Sublime Mana Serum>
			ItemService.addItem(player,160003587, 1000); //<Pepento Jelly>
			ItemService.addItem(player,160010337, 1); //<Event Heartfelt Chocolate>
			ItemService.addItem(player,160010333, 1); //<Event Scintillating Chocolate>
			ItemService.addItem(player,164000266, 100); //<Enhanced Seed of Detection>
			ItemService.addItem(player,169000011, 20000); //<Ancient Power Shard>
			ItemService.addItem(player,169300007, 20000); //<Master Odella Powder>
			ItemService.addItem(player,166050129, 50); //<Blazing Warlord's Idian: Physical/Magical Attack>
			ItemService.addItem(player,166050138, 50); //<Honorable Elim's Idian: Magical Attack>
			ItemService.addItem(player,161001001, 100); //<Revival Stone>
			ItemService.addItem(player,160010327, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010328, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010331, 1000); //<Kei Form Candy>
			ItemService.addItem(player,160010332, 1000); //<Kei Form Candy>
			ItemService.addItem(player,187060170, 1); //<Trillirunerk's Blue Flame Feather>
			ItemService.addItem(player,187060177, 1); //<Aether Glider>
			ItemService.addItem(player,169610146, 1); //<[Title] Glorious Number One (30 days)>
			ItemService.addItem(player,187100016, 1); //<Kaisinel's Plume: Magic Boost>
			ItemService.addItem(player,187100018, 1); //<Marchutan's Plume: Magic Boost>
			PacketSendUtility.sendMessage(player, "\uE022 Enjoy Being Insane, Insaner! \uE022");
		}
		
		if(param[0].equals("me")){
			ItemService.addItem(player,164002111, 1000); //<Greater Running Scroll (1 hour)>
			ItemService.addItem(player,164002113, 1000); //<Greater Awakening Scroll (1 hour)>
			ItemService.addItem(player,164002112, 1000); //<Greater Courage Scroll (1 hour)>
			ItemService.addItem(player,164002115, 1000); //<Major Crit Spell Scroll (1 hour)>
			ItemService.addItem(player,164002114, 1000); //<Major Crit Strike Scroll (1 hour)>
			ItemService.addItem(player,164000259, 1000); //<Premium Anti-Shock Scroll>
			ItemService.addItem(player,164000260, 1000); //<Premium Magic Suppression Scroll>
			ItemService.addItem(player,164000021, 1000); //<Boost Elemental Defense>
			ItemService.addItem(player,164000114, 1000); //<Fine Fireproof Scroll>
			ItemService.addItem(player,164000115, 1000); //<Fine Earthproof Scroll>
			ItemService.addItem(player,164000116, 1000); //<Fine Waterproof Scroll>
			ItemService.addItem(player,164000117, 1000); //<Fine Windproof Scroll>
			ItemService.addItem(player,164000257, 1000); //<Fine Strike Resist Scroll>
			ItemService.addItem(player,162000083, 1000); //<Leader's Recovery Scroll>
			ItemService.addItem(player,160002475, 1000); //<Fresh Abex Cheese Soup>
			ItemService.addItem(player,160002505, 1000); //<Basil Dragon Meat Soup>
			ItemService.addItem(player,160009017, 10); //<Vinna Juice>
			ItemService.addItem(player,162000124, 1000); //<Superior Recovery Serum>
			ItemService.addItem(player,162000121, 1000); //<Superior Recovery Potion>
			ItemService.addItem(player,162000107, 1000); //<Saam King's Herbs>
			ItemService.addItem(player,162000137, 1000); //<Sublime Life Serum>
			ItemService.addItem(player,162000139, 1000); //<Sublime Mana Serum>
			ItemService.addItem(player,160003587, 1000); //<Pepento Jelly>
			ItemService.addItem(player,160010337, 1); //<Event Heartfelt Chocolate>
			ItemService.addItem(player,160010333, 1); //<Event Scintillating Chocolate>
			ItemService.addItem(player,160010334, 1); //<Event Constitutional Chocolate>
			ItemService.addItem(player,164000266, 100); //<Enhanced Seed of Detection>
			ItemService.addItem(player,169000011, 20000); //<Ancient Power Shard>
			ItemService.addItem(player,169300007, 20000); //<Master Odella Powder
			ItemService.addItem(player,166050129, 50); //<Blazing Warlord's Idian: Physical/Magical Attack>
			ItemService.addItem(player,161001001, 100); //<Revival Stone>
			ItemService.addItem(player,160010325, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010326, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010329, 1000); //<Kei Form Candy>
			ItemService.addItem(player,160010330, 1000); //<Kei Form Candy>
			ItemService.addItem(player,160010327, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010328, 1000); //<Captain Harlock Form Candy>
			ItemService.addItem(player,160010331, 1000); //<Kei Form Candy>
			ItemService.addItem(player,160010332, 1000); //<Kei Form Candy>
			ItemService.addItem(player,187060166, 1); //<Trillirunerk's Ice Flame Feather>
			ItemService.addItem(player,187060170, 1); //<Trillirunerk's Blue Flame Feather>
			ItemService.addItem(player,187060177, 1); //<Aether Glider>
			ItemService.addItem(player,187100015, 1); //<Kaisinel's Plume: Attack>
			ItemService.addItem(player,187100017, 1); //<Marchutan's Plume: Attack>
			ItemService.addItem(player,187100016, 1); //<Kaisinel's Plume: Magic Boost>
			ItemService.addItem(player,187100018, 1); //<Marchutan's Plume: Magic Boost>
			ItemService.addItem(player,169610146, 1); //<[Title] Glorious Number One (30 days)>
			PacketSendUtility.sendMessage(player, "\uE022 Enjoy Being Insane, Insaner! \uE022");
		}
		
    	if(param[0].equals("Gladiator")){
			 ItemService.addItem(player,110601715, 1); //Breastplate
    		 ItemService.addItem(player,111601679, 1); //Gauntlets
    		 ItemService.addItem(player,113601662, 1); //Greaves
    		 ItemService.addItem(player,114601668, 1); //Sabatons
    		 ItemService.addItem(player,112601660, 1); //Shoulderplates
			 ItemService.addItem(player,101301217, 1); //Spear
			 ItemService.addItem(player,101301286, 1); //Spear-Extend-Armfuse
			 ItemService.addItem(player,125004009, 1); //<Awakened Ahserion's Helm>
			 ItemService.addItem(player,120001492, 2); //Dawnwing Divine Corundum Earrings>
			 ItemService.addItem(player,122001633, 2); //Dawnwing Divine Corundum Ring>
			 ItemService.addItem(player,121001364, 1); //<Dawnwing Divine Corundum Necklace>
			 ItemService.addItem(player,123001410, 1); //<Dawnwing Divine Belt>
			 ItemService.addItem(player,167010181, 1000); //<Manastone: Attack +5 / Crit Strike +10>
			 ItemService.addItem(player,167010192, 1000); //<Manastone: Crit Strike +19 / Attack +3>
			 ItemService.addItem(player,167020079, 100); //<Ancient Manastone: Attack +8>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Gladiator's Set Pack! \uE022");
		}
		
        if(param[0].equals("Templar")){
			 ItemService.addItem(player,110601716, 1); //Breastplate
    		 ItemService.addItem(player,111601680, 1); //Gauntlets
    		 ItemService.addItem(player,113601663, 1); //Greaves
    		 ItemService.addItem(player,114601669, 1); //Sabatons
    		 ItemService.addItem(player,112601661, 1); //Shoulderplates
			 ItemService.addItem(player,100901312, 1); //Greatsword
			 ItemService.addItem(player,100901395, 1); //Greatsword-Extend-Armfuse
			 ItemService.addItem(player,100101264, 1); //Mace
			 ItemService.addItem(player,115001805, 1); //Physical Shield
			 ItemService.addItem(player,125004009, 1); //<Awakened Ahserion's Helm>
			 ItemService.addItem(player,120001492, 2); //Dawnwing Divine Corundum Earrings>
			 ItemService.addItem(player,122001633, 2); //Dawnwing Divine Corundum Ring>
			 ItemService.addItem(player,121001364, 1); //<Dawnwing Divine Corundum Necklace>
			 ItemService.addItem(player,123001410, 1); //<Dawnwing Divine Belt>
			 ItemService.addItem(player,167010181, 1000); //<Manastone: Attack +5 / Crit Strike +10>
			 ItemService.addItem(player,167010222, 1000); //<Manastone: HP +95 / Block +16>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Templar's Set Pack !\uE022");
		}
		
        if(param[0].equals("Cleric")){
			 ItemService.addItem(player,110551262, 1); //Hauberk
			 ItemService.addItem(player,113501841, 1); //Chausses
			 ItemService.addItem(player,111501822, 1); //Handguards
			 ItemService.addItem(player,112501758, 1); //Spaulders
			 ItemService.addItem(player,114501849, 1); //Brogans
			 ItemService.addItem(player,101501409, 1); //Staff
			 ItemService.addItem(player,101501286, 1); //Staff-MagicalDamage-Armfuse
			 ItemService.addItem(player,100101264, 1); //Mace
			 ItemService.addItem(player,115001704, 1); //Magical Shield
			 ItemService.addItem(player,125004010, 1); //<Awakened Ahserion's Chain Helm>
			 ItemService.addItem(player,120001493, 2); //<Dawnwing Divine Turquoise Earrings>
			 ItemService.addItem(player,122001634, 2); //<Dawnwing Divine Turquoise Ring>
			 ItemService.addItem(player,121001365, 1); //<Dawnwing Divine Turquoise Necklace>
			 ItemService.addItem(player,123001411, 1); //<Dawnwing Divine Band>
			 ItemService.addItem(player,167010207, 1000); //<Manastone: Magic Boost +27 / Magical Accuracy +9>
			 ItemService.addItem(player,167000738, 1000); //<Manastone: Magical Accuracy +16 / Magic Boost +14>
			 ItemService.addItem(player,167020081, 100); //<Ancient Manastone: Magic Boost +35>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Cleric's Set Pack! \uE022");
		}
		
        if(param[0].equals("Chanter")){
			 ItemService.addItem(player,110551263, 1); //Hauberk
			 ItemService.addItem(player,113501842, 1); //Chausses
			 ItemService.addItem(player,111501823, 1); //Handguards
			 ItemService.addItem(player,112501759, 1); //Spaulders
			 ItemService.addItem(player,114501850, 1); //Brogans
			 ItemService.addItem(player,101501306, 1); //Staff
			 ItemService.addItem(player,101501376, 1); //Staff-Extend-Armfuse
			 ItemService.addItem(player,100101264, 1); //Mace
			 ItemService.addItem(player,115001805, 1); //Physical Shield
			 ItemService.addItem(player,125004201, 1); //<Elite Evangale Divine Chain Helm>
			 ItemService.addItem(player,120001492, 2); //Dawnwing Divine Corundum Earrings>
			 ItemService.addItem(player,122001633, 2); //Dawnwing Divine Corundum Ring>
			 ItemService.addItem(player,121001364, 1); //<Dawnwing Divine Corundum Necklace>
			 ItemService.addItem(player,123001410, 1); //<Dawnwing Divine Belt>
			 ItemService.addItem(player,167010181, 1000); //<Manastone: Attack +5 / Crit Strike +10>
			 ItemService.addItem(player,167010192, 1000); //<Manastone: Crit Strike +19 / Attack +3>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Chanter's Set Pack! \uE022");
		}
		
        if(param[0].equals("Assassin")){
			 ItemService.addItem(player,110301922, 1); //Jerkin
			 ItemService.addItem(player,113301892, 1); //Breeches
			 ItemService.addItem(player,112301799, 1); //Shoulderguards
			 ItemService.addItem(player,111301861, 1); //Vambrace
			 ItemService.addItem(player,114301929, 1); //Boots
			 ItemService.addItem(player,100001684, 1); //Sword
			 ItemService.addItem(player,100001760, 1); //Sword-Extend
			 ItemService.addItem(player,100201457, 1); //Dagger
			 ItemService.addItem(player,100201529, 1); //Dagger-Extend
			 ItemService.addItem(player,101701305, 1); //Bow
			 ItemService.addItem(player,101701411, 1); //Bow-PhyiscalDamage-Armfuse
			 ItemService.addItem(player,125004210, 1); //<Special Elite Ambassador's Divine Leather Hat>
			 ItemService.addItem(player,120001492, 2); //Dawnwing Divine Corundum Earrings>
			 ItemService.addItem(player,122001633, 2); //Dawnwing Divine Corundum Ring>
			 ItemService.addItem(player,121001364, 1); //<Dawnwing Divine Corundum Necklace>
			 ItemService.addItem(player,123001410, 1); //<Dawnwing Divine Belt>
			 ItemService.addItem(player,167010181, 1000); //<Manastone: Attack +5 / Crit Strike +10>
			 ItemService.addItem(player,167010183, 1000); //<Manastone: Attack +5 / Magical Accuracy +9>
			 ItemService.addItem(player,167020079, 100); //<Ancient Manastone: Attack +8>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Assassin's Set Pack! \uE022");
		}
		
        if(param[0].equals("Ranger")){
			 ItemService.addItem(player,110301922, 1); //Jerkin
			 ItemService.addItem(player,113301892, 1); //Breeches
			 ItemService.addItem(player,112301799, 1); //Shoulderguards
			 ItemService.addItem(player,111301861, 1); //Vambrace
			 ItemService.addItem(player,114301929, 1); //Boots
			 ItemService.addItem(player,101701305, 1); //Bow
			 ItemService.addItem(player,101701411, 1); //Bow-PhyiscalDamage-Armfuse
			 ItemService.addItem(player,100001684, 1); //Sword
			 ItemService.addItem(player,100001760, 1); //Sword-Extend
			 ItemService.addItem(player,100201457, 1); //Dagger
			 ItemService.addItem(player,100201529, 1); //Dagger-Extend
			 ItemService.addItem(player,125004011, 1); //<Awakened Ahserion's Hat>
			 ItemService.addItem(player,120001492, 2); //Dawnwing Divine Corundum Earrings>
			 ItemService.addItem(player,122001633, 2); //Dawnwing Divine Corundum Ring>
			 ItemService.addItem(player,121001364, 1); //<Dawnwing Divine Corundum Necklace>
			 ItemService.addItem(player,123001410, 1); //<Dawnwing Divine Belt>
			 ItemService.addItem(player,167010181, 1000); //<Manastone: Attack +5 / Crit Strike +10>
			 ItemService.addItem(player,167010183, 1000); //<Manastone: Attack +5 / Magical Accuracy +9>
			 ItemService.addItem(player,167020079, 100); //<Ancient Manastone: Attack +8>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Ranger's Set Pack! \uE022");
		}
		
        if(param[0].equals("Sorcerer")){
			 ItemService.addItem(player,110101925, 1); //Tunic
			 ItemService.addItem(player,113101752, 1); //Leggings
			 ItemService.addItem(player,112101687, 1); //Pauldrons
			 ItemService.addItem(player,111101741, 1); //Gloves
			 ItemService.addItem(player,114101786, 1); //Shoes
			 ItemService.addItem(player,100601475, 1); //Spellbook
			 ItemService.addItem(player,100601474, 1); //Spellbook-Extend
			 ItemService.addItem(player,100501357, 1); //Orb
			 ItemService.addItem(player,100501270, 1); //Orb-Extend
			 ItemService.addItem(player,125004012, 1); //<Awakened Ahserion's Headband>
			 ItemService.addItem(player,120001493, 2); //<Dawnwing Divine Turquoise Earrings>
			 ItemService.addItem(player,122001634, 2); //<Dawnwing Divine Turquoise Ring>
			 ItemService.addItem(player,121001365, 1); //<Dawnwing Divine Turquoise Necklace>
			 ItemService.addItem(player,123001411, 1); //<Dawnwing Divine Band>
			 ItemService.addItem(player,167010207, 1000); //<Manastone: Magic Boost +27 / Magical Accuracy +9>
			 ItemService.addItem(player,167000738, 1000); //<Manastone: Magical Accuracy +16 / Magic Boost +14>
			 ItemService.addItem(player,167020081, 100); //<Ancient Manastone: Magic Boost +35>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Sorcerer's Set Pack! \uE022");
		}
		
        if(param[0].equals("Spiritmaster")){
			 ItemService.addItem(player,110101925, 1); //Tunic
			 ItemService.addItem(player,113101752, 1); //Leggings
			 ItemService.addItem(player,112101687, 1); //Pauldrons
			 ItemService.addItem(player,111101741, 1); //Gloves
			 ItemService.addItem(player,114101786, 1); //Shoes
			 ItemService.addItem(player,100601475, 1); //Spellbook
			 ItemService.addItem(player,100601380, 1); //Spellbook-Extend
			 ItemService.addItem(player,100501358, 1); //Orb
			 ItemService.addItem(player,100501270, 1); //Orb-Extend
			 ItemService.addItem(player,125004012, 1); //<Awakened Ahserion's Headband>
			 ItemService.addItem(player,120001493, 2); //<Dawnwing Divine Turquoise Earrings>
			 ItemService.addItem(player,122001634, 2); //<Dawnwing Divine Turquoise Ring>
			 ItemService.addItem(player,121001365, 1); //<Dawnwing Divine Turquoise Necklace>
			 ItemService.addItem(player,123001411, 1); //<Dawnwing Divine Band>
			 ItemService.addItem(player,167010207, 1000); //<Manastone: Magic Boost +27 / Magical Accuracy +9>
			 ItemService.addItem(player,167000738, 1000); //<Manastone: Magical Accuracy +16 / Magic Boost +14>
			 ItemService.addItem(player,167020081, 100); //<Ancient Manastone: Magic Boost +35>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Spiritmaster's Set Pack! \uE022");
		}
		
        if(param[0].equals("Gunner")){
			 ItemService.addItem(player,110301923, 1); //Jerkin
			 ItemService.addItem(player,113301893, 1); //Breeches
			 ItemService.addItem(player,112301800, 1); //Shoulderguards
			 ItemService.addItem(player,111301862, 1); //Vambrace
			 ItemService.addItem(player,114301930, 1); //Boots
			 ItemService.addItem(player,101801172, 1); //PistolOne
			 ItemService.addItem(player,101801172, 1); //PistolTwo
			 ItemService.addItem(player,101901174, 1); //AetherCannon
			 ItemService.addItem(player,101901083, 1); //AetherCannon-Extend
			 ItemService.addItem(player,125004011, 1); //<Awakened Ahserion's Hat>
			 ItemService.addItem(player,120001493, 2); //<Dawnwing Divine Turquoise Earrings>
			 ItemService.addItem(player,122001634, 2); //<Dawnwing Divine Turquoise Ring>
			 ItemService.addItem(player,121001365, 1); //<Dawnwing Divine Turquoise Necklace>
			 ItemService.addItem(player,123001411, 1); //<Dawnwing Divine Band>
			 ItemService.addItem(player,167010207, 1000); //<Manastone: Magic Boost +27 / Magical Accuracy +9>
			 ItemService.addItem(player,167000738, 1000); //<Manastone: Magical Accuracy +16 / Magic Boost +14>
			 ItemService.addItem(player,167020081, 100); //<Ancient Manastone: Magic Boost +35>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Gunner's Set Pack! \uE022");
		}
		
        if(param[0].equals("Aethertech")){
			 ItemService.addItem(player,110551264, 1); //Hauberk
			 ItemService.addItem(player,113501843, 1); //Chausses
			 ItemService.addItem(player,111501824, 1); //Handguards
			 ItemService.addItem(player,112501760, 1); //Spaulders
			 ItemService.addItem(player,114501851, 1); //Brogans
			 ItemService.addItem(player,102101112, 1); //Key
			 ItemService.addItem(player,102101005, 1); //Key-Extend
			 ItemService.addItem(player,125004010, 1); //<Awakened Ahserion's Chain Helm>
			 ItemService.addItem(player,120001493, 2); //<Dawnwing Divine Turquoise Earrings>
			 ItemService.addItem(player,122001634, 2); //<Dawnwing Divine Turquoise Ring>
			 ItemService.addItem(player,121001365, 1); //<Dawnwing Divine Turquoise Necklace>
			 ItemService.addItem(player,123001411, 1); //<Dawnwing Divine Band>
			 ItemService.addItem(player,167010207, 1000); //<Manastone: Magic Boost +27 / Magical Accuracy +9>
			 ItemService.addItem(player,167000738, 1000); //<Manastone: Magical Accuracy +16 / Magic Boost +14>
			 ItemService.addItem(player,167020081, 100); //<Ancient Manastone: Magic Boost +35>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Aethertech's Set Pack! \uE022");
		}
		
		if(param[0].equals("Songweaver")){
			 ItemService.addItem(player,110101925, 1); //Tunic
			 ItemService.addItem(player,113101752, 1); //Leggings
			 ItemService.addItem(player,112101687, 1); //Pauldrons
			 ItemService.addItem(player,111101741, 1); //Gloves
			 ItemService.addItem(player,114101786, 1); //Shoes
			 ItemService.addItem(player,102001295, 1); //Harp
			 ItemService.addItem(player,102001199, 1); //Harp-Extend
			 ItemService.addItem(player,125004012, 1); //<Awakened Ahserion's Headband>
			 ItemService.addItem(player,120001493, 2); //<Dawnwing Divine Turquoise Earrings>
			 ItemService.addItem(player,122001634, 2); //<Dawnwing Divine Turquoise Ring>
			 ItemService.addItem(player,121001365, 1); //<Dawnwing Divine Turquoise Necklace>
			 ItemService.addItem(player,123001411, 1); //<Dawnwing Divine Band>
			 ItemService.addItem(player,167010207, 1000); //<Manastone: Magic Boost +27 / Magical Accuracy +9>
			 ItemService.addItem(player,167000738, 1000); //<Manastone: Magical Accuracy +16 / Magic Boost +14>
			 ItemService.addItem(player,167020081, 100); //<Ancient Manastone: Magic Boost +35>
			 PacketSendUtility.sendMessage(player, "\uE022 Congratulations, You Just Added Songweaver's Set Pack! \uE022");
		}
	}
	@Override
	public void onFail(Player player, String message) {
        PacketSendUtility.sendMessage(player, "Syntax : .givepack help");
	}
}