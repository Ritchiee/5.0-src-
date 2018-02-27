/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.services.ecfunctions.tvt;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.utils3d.FFA3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.ecfunctions.PVPManager;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Ghostfur
 * @author Goong
 */
public class TVTService extends PVPManager {
	
	private final Logger log = LoggerFactory.getLogger(TVTService.class);
	private static final TVTService service = new TVTService();
	
	private int worldNumber;
	private Player Killer;
	public String worldName;
	private FFA3D loc;

	public double x, y, z;
	public int h;
	
	private int randomNum = 0;
	
    private SpawnTemplate spawnEly;
    private SpawnTemplate spawnAsmo;
   
    private WorldMapInstance instance;
    private WorldMapInstance oldInstance;

    public int mediumMapID1 = 301120000; //karmar battle field done
    public int mediumMapID2 = 320100000; //fire temple done
    public int mediumMapID3 = 300170000; //beshmundir temple // done
    public int mediumMapID4 = 320150000; //Padmarashka's Cave done
    public int mediumMapID5 = 300090000; //right wing chamber done
    public int mediumMapID6 = 300230000; //Kromede Trial //done
    public int mediumMapID7 = 300280000; //rentus base done
    public int mediumMapID8 = 300150000; //Udas Temple done
    public int mediumMapID9 = 300590000; //ohpidan bridge done
    public int mediumMapID10 = 300060000; //Sulfur done
    public int mediumMapID11 = 300240000; //AturamSkyFortress done
    public int mediumMapID12 = 300050000; //Asteria Chamber done
    public int mediumMapID13 = 320110000; //Alquimia Lab done
    public int mediumMapID14 = 301270000; //LinkgateFoundry done
    public int mediumMapID15 = 320120000; //Shadow Court done
    public int mediumMapID16 = 320080000; //Draupnir Cave done
	
	WorldMapInstance mediumInstance1, mediumInstance2, mediumInstance3, mediumInstance4,
	mediumInstance5, mediumInstance6, mediumInstance7, mediumInstance8, mediumInstance9, 
	mediumInstance10, mediumInstance11, mediumInstance12, mediumInstance13, mediumInstance14, 
	mediumInstance15, mediumInstance16;
	
    public static TVTService getInstance(){
        return service;
    }    
	public void Init() {		
		mediumInstance1 = createInstance(mediumMapID1, false);
		mediumInstance2 = createInstance(mediumMapID2, false);	
		//mediumInstance3 share the same instance with largeInstance8
		mediumInstance3 = createInstance(mediumMapID3, false);	
		mediumInstance4 = createInstance(mediumMapID4, false);
		mediumInstance5 = createInstance(mediumMapID5, false);
		//mediumInstance6 share the same instance with largeInstance12
		mediumInstance6 = createInstance(mediumMapID6, true);	
		mediumInstance7 = createInstance(mediumMapID7, false);
		mediumInstance8 = createInstance(mediumMapID8, false); 
		mediumInstance9 = createInstance(mediumMapID9, false);		
		mediumInstance10 = createInstance(mediumMapID10, false);
		mediumInstance11 = createInstance(mediumMapID11, true);
		mediumInstance12 = createInstance(mediumMapID12, false);
		mediumInstance13 = createInstance(mediumMapID13, true);
		mediumInstance14 = createInstance(mediumMapID14, true);
		mediumInstance15 = createInstance(mediumMapID15, true);		
		mediumInstance16 = createInstance(mediumMapID16, true);				
		instanceTask(80);
    	randomMediumInstance();
	} 
	
	private WorldMapInstance createInstance(int worldId, boolean withDoor) {
    	if (withDoor) {
    		WorldMapInstance DuoFFAInstance = InstanceService.getNextAvailableFFAInstance(worldId, true);
    		return DuoFFAInstance;
    	} else {
    		WorldMapInstance DuoFFAInstance = InstanceService.getNextAvailableFFAInstance(worldId, false);
    		return DuoFFAInstance;
    	}
    }
	/**
	 * Medium posMedium
	 * 
	 */
	//kamar battle field 301120000
		static FFA3D[] posMedium1 = new FFA3D[]{
			new FFA3D(1571.5962f, 1392.0237f, 597.0612f, 48),
			new FFA3D(1505.6548f, 1555.5101f, 610.7626f, 70),
			new FFA3D(1413.7462f, 1566.5432f, 595.7288f, 70),
			new FFA3D(1433.7483f, 1620.9523f, 599.9493f, 71),
			new FFA3D(1192.256f, 1653.6671f, 593.6512f, 4),
			new FFA3D(1294.1083f, 1677.8041f, 596.8785f, 70),
			new FFA3D(1150.2633f, 1534.8066f, 584.875f, 10),};
		//fire temple 320100000
		static FFA3D[] posMedium2 = new FFA3D[]{
			new FFA3D(265.31871f, 200.28517f, 119.36553f, 35),
			new FFA3D(396.17816f, 274.7947f, 110.843392f, 64),
			new FFA3D(346.91217f, 351.30856f, 146.05597f, 53),
			new FFA3D(374.48663f, 85.15076f, 117.155655f, 20),
			new FFA3D(350.70593f, 262.632f, 118.461784f, 117),
			new FFA3D(385.9988f, 293.25034f, 115.05154f, 89),
			new FFA3D(405.82626f, 236.8875f, 121.88696f, 45),
			new FFA3D(416.8092f, 96.5659f, 117.30519f, 50),
			new FFA3D(207.5757f, 283.0724f, 125.883995f, 97),
			new FFA3D(123.43369f, 200.4345f, 100.31581f, 115),};
		//beshmundir temple 300170000
		static FFA3D[] posMedium3 = new FFA3D[]{
			new FFA3D(1036.9789f, 999.2163f, 287.47525f, 6),
			new FFA3D(1215.456f, 684.7193f, 251.2751f, 29),
			new FFA3D(1216.1144f, 751.0783f, 318.7667f, 29),
			new FFA3D(1398.1777f, 1320.5906f, 307.58472f, 79),
			new FFA3D(1409.508f, 1185.8265f, 294.5f, 81),
			new FFA3D(1160.6357f, 1224.2141f, 287.7014f, 105),
			new FFA3D(1131.3632f, 1107.2262f, 279.47992f, 105),
			new FFA3D(1330.9554f, 1130.5115f, 282.0f, 74),};
		//Padmarashka's Cave 320150000
		static FFA3D[] posMedium4 = new FFA3D[]{
			new FFA3D(373.51642f, 526.7331f, 66.87186f, 98),
			new FFA3D(504.54248f, 461.5506f, 76.91472f, 65),
			new FFA3D(310.20963f, 439.85443f, 77.18049f, 1),
			new FFA3D(413.03375f, 341.98602f, 81.135185f, 21),
			new FFA3D(620.0556f, 286.93393f, 68.769999f, 66),
			new FFA3D(633.11523f, 203.38213f, 67.89635f, 55),
			new FFA3D(541.0462f, 135.81851f, 69.32095f, 20),
			new FFA3D(486.1619f, 225.26633f, 69.42379f, 119),};
		//right wing chamber 300090000
		static FFA3D[] posMedium5 = new FFA3D[]{
			new FFA3D(264.3072f, 392.54553f, 103.24291f, 90),
			new FFA3D(183.22029f, 284.03632f, 103.13739f, 114),
			new FFA3D(160.68745f, 203.7044f, 103.137314f, 4),
			new FFA3D(201.93588f, 142.53561f, 103.13842f, 21),
			new FFA3D(262.58337f, 114.73569f, 103.13809f, 30),
			new FFA3D(344.65457f, 151.83511f, 103.13739f, 51),
			new FFA3D(359.42703f, 238.54097f, 103.13786f, 71),
			new FFA3D(319.14105f, 332.72894f, 103.13731f, 76),};
		//Kromede Trial 300230000
		static FFA3D[] posMedium6 = new FFA3D[]{
		    new FFA3D(529.31964f, 621.6982f, 201.54883f, 30),
		    new FFA3D(631.7712f, 584.0474f, 208.99329f, 30),
		    new FFA3D(590.533f, 592.1864f, 208.9933f, 30),
		    new FFA3D(610.62115f, 640.5872f, 208.98755f, 30),
		    new FFA3D(495.20703f, 585.71f, 216.10829f, 30),
		    new FFA3D(570.5964f, 654.35895f, 206.9643f, 30),
		    new FFA3D(512.5533f, 614.191f, 214.99915f, 30),
		    new FFA3D(558.19995f, 795.9157f, 215.88853f, 30),
		    new FFA3D(500.87433f, 783.68567f, 215.97673f, 30),};
		//rentus base 300280000
		static FFA3D[] posMedium7 = new FFA3D[]{
		    new FFA3D(562.91125f, 588.8692f, 154.125f, 70),
		    new FFA3D(508.31497f, 613.87006f, 158.67757f, 115),
		    new FFA3D(455.29355f, 529.4532f, 139.5f, 7),
		    new FFA3D(539.53296f, 506.81046f, 175.37022f, 51),
		    new FFA3D(424.4234f, 431.25925f, 149.49472f, 42),
		    new FFA3D(460.48907f, 479.49136f, 179.4234f, 55),
		    new FFA3D(362.9009f, 465.18146f, 138.66933f, 7),
		    new FFA3D(309.95837f, 638.1602f, 150.47337f, 111),
		    new FFA3D(229.74135f, 719.5911f, 164.58719f, 116),
		    new FFA3D(210.56203f, 498.70352f, 197.83089f, 24),
		    new FFA3D(271.69495f, 655.4851f, 180.66493f, 55),
		    new FFA3D(229.812594f, 571.3857f, 178.48009f, 26),
		    new FFA3D(143.538f, 247.86418f, 209.81859f, 27),
		    new FFA3D(217.02643f, 638.2475f, 182.34702f, 11),};
		//Udas Temple
		static FFA3D[] posMedium8 = new FFA3D[]{
		    new FFA3D(636.5956f, 654.64496f, 133.23831f, 105),
		    new FFA3D(826.54834f, 484.16077f, 134.95059f, 58),
		    new FFA3D(787.7538f, 410.05258f, 131.90742f, 118),
		    new FFA3D(513.6373f, 505.6693f, 131.42784f, 61),
		    new FFA3D(508.0596f, 408.69284f, 134.42802f, 32),
		    new FFA3D(510.80237f, 700.32184f, 126.06522f, 86),};
		//ohpidan bridge 300590000
		static FFA3D[] posMedium9 = new FFA3D[]{
			new FFA3D(766.0864f, 570.65594f, 573.3696, 36),
			new FFA3D(562.8296f, 521.80255f, 607.89075f, 87),
			new FFA3D(437.63663f, 496.52597f, 604.8871f, 2),
			new FFA3D(645.59314f, 420.77634f, 606.57635f, 44),
			new FFA3D(666.2821f, 474.97546f, 601.1673f, 112),
			new FFA3D(758.0605f, 576.4918f, 572.875f, 87),};
		//sulfur
		static FFA3D[] posMedium10 = new FFA3D[]{
		    new FFA3D(463.5722f, 347.48862f, 162.81717f, 30),  
		    new FFA3D(463.30612f, 580.02344f, 165.77994f, 90),       
		    new FFA3D(390.14612f, 552.9489f, 165.77994f, 110),      
		    new FFA3D(384.9894f, 468.80222f, 164.01689f, 5),    
		    new FFA3D(417.1364f, 390.10526f, 165.7726f, 24),     
		    new FFA3D(534.6254f, 403.95004f, 166.13959f, 46),    
		    new FFA3D(541.2394f, 480.92896f, 164.01666f, 60),  
		    new FFA3D(535.5123f, 529.41223f, 165.77994f, 74), 
		    new FFA3D(463.056f, 491.49872f, 163.9515f, 91),};  
		//autoram sky fortress - Open Door
		static FFA3D[] posMedium11 = new FFA3D[]{
		    new FFA3D(672.3723f, 68.6072f, 612.4829f, 90), //300240000 
		    new FFA3D(755.9946f, 165.5009f, 608.79083f, 53), //300240000
		    new FFA3D(732.3265f, 89.64582f, 613.68164f, 0), //300240000
		    new FFA3D(714.48627f, 88.41492f, 603.58844f, 70), //300240000
		    new FFA3D(619.5347f, 26.89916f, 614.0806f, 31), //300240000
		    new FFA3D(604.0f, 113.78615f, 614.0411f, 28), //300240000
		    new FFA3D(581.87244f, 147.091f, 612.87054f, 119), //300240000
		    new FFA3D(543.6546f, 24.561714f, 610.0971f, 3), //300240000    
		    new FFA3D(541.61835f, 64.8886f, 610.09717f, 115), //300240000    
		    new FFA3D(552.80914f, 139.7869f, 615.107f, 33),}; //300240000    
		//Asteria Chamber  - Open Door
		  static FFA3D[] posMedium12 = new FFA3D[]{
		   new FFA3D(469.0f, 568.0f, 201.70633f, 8), //300050000
		   new FFA3D(666.69354f, 565.25336f, 206.14534f, 71), //300050000
		   new FFA3D(523.12036f, 426.3582f, 199.75935f, 29),}; //300050000
		//Alquimia Lab - open door 
		static FFA3D[] posMedium13 = new FFA3D[]{
		    new FFA3D(602.1943f, 527.3014f, 200.05792f, 59), //320110000
		    new FFA3D(551.1791f, 567.1244f, 199.73503f, 119), //320110000
		    new FFA3D(595.36035f, 565.201f, 199.73503f, 88), //320110000
		    new FFA3D(595.30426f, 489.73212f, 199.73503f, 43), //320110000
		    new FFA3D(517.4157f, 489.79916f, 199.73503f, 10), //320110000
		    new FFA3D(476.39178f, 488.0198f, 204.69067f, 29), //320110000
		    new FFA3D(477.02524f, 561.3962f, 204.69067f, 90), //320110000
		    new FFA3D(458.49097f, 485.50113f, 205.33063f, 45), //320110000
		    new FFA3D(397.38693f, 486.49445f, 205.34003f, 13), //320110000
		    new FFA3D(400.6863f, 533.3298f, 204.69067f, 0), //320110000
		    new FFA3D(400.05548f, 559.36395f, 204.69067f, 119), //320110000
		    new FFA3D(456.3939f, 558.77057f, 204.69067f, 60), //320110000
		    new FFA3D(275.78726f, 501.13138f, 213.09743f, 0), //320110000
		    new FFA3D(368.1714f, 523.5576f, 209.92009f, 74), //320110000
		    new FFA3D(368.44785f, 481.50098f, 209.92009f, 50),}; //320110000
		//LinkgateFoundry - open door
		static FFA3D[] posMedium14 = new FFA3D[]{
			new FFA3D(360.72992f, 259.04083f, 311.36432f, 59), //301270000
			new FFA3D(306.45596f, 258.79977f, 311.41086f, 32), //301270000
			new FFA3D(290.57056f, 302.45245f, 311.42422f, 44), //301270000		
			new FFA3D(242.78978f, 323.00323f, 311.54514f, 62), //301270000		
			new FFA3D(199.20912f, 301.51715f, 311.39203f, 76), //301270000
			new FFA3D(172.3031f, 261.35437f, 312.64062f, 92), //301270000	
			new FFA3D(199.56609f, 215.47038f, 311.365f, 105), //301270000		
			new FFA3D(245.46407f, 196.64331f, 311.48083f, 1), //301270000
			new FFA3D(289.52026f, 215.76114f, 311.4664f, 18),}; //301270000	
		//ShadowCourt 320120000
		static FFA3D[] posMedium15 = new FFA3D[]{
			new FFA3D(407.3566f, 403.2982f, 245.10768f, 29),
			new FFA3D(448.5527f, 474.07068f, 242.01869f, 59),
			new FFA3D(369.0938f, 474.54498f, 242.00453f, 0),
			new FFA3D(407.2672f, 555.09717f, 231.1734f, 90),
			new FFA3D(511.41748f, 511.89008f, 222.5006f, 29),
			new FFA3D(511.44348f, 594.0056f, 222.48643f, 90),
			new FFA3D(592.1598f, 554.5269f, 211.6554f, 60),
			new FFA3D(549.29987f, 450.31366f, 202.9827f, 119),
			new FFA3D(633.22046f, 450.4f, 202.96854f, 59),
			new FFA3D(591.70276f, 399.59564f, 206.53279f, 30),};
		//Draupnir Cave
		static FFA3D[] posMedium16 = new FFA3D[]{
			new FFA3D(637.9372f, 411.40445f, 319.44244f, 37),
			new FFA3D(677.406f, 468.62378f, 316.19058f, 2),
			new FFA3D(765.5092f, 502.0949f, 319.92065f, 96),
			new FFA3D(872.62756f, 533.41815f, 329.7143f, 71),
			new FFA3D(823.7476f, 426.47595f, 318.2671f, 55),
			new FFA3D(808.12244f, 370.9118f, 320.3139f, 44),
			new FFA3D(726.0071f, 365.9905f, 323.41275f, 9),
			new FFA3D(715.15186f, 432.8395f, 318.7473f, 7),
			new FFA3D(765.1226f, 445.41638f, 320.01462f, 48),
			new FFA3D(817.96515f, 498.5655f, 319.70627f, 76),
			new FFA3D(778.79443f, 414.1308f, 320.35992f, 99),}; 
		
		private void randomLoc () {
		if (worldNumber == mediumMapID1) { 
			loc = posMedium1[Rnd.get(posMedium1.length - 1)];
		} else if (worldNumber == mediumMapID2) {
			loc = posMedium2[Rnd.get(posMedium2.length - 1)];
		} else if (worldNumber == mediumMapID3) { //share the same world number with largemap12
			loc = posMedium3[Rnd.get(posMedium3.length - 1)];
		} else if (worldNumber == mediumMapID4) {
			loc = posMedium4[Rnd.get(posMedium4.length - 1)];
		} else if (worldNumber == mediumMapID5) {
			loc = posMedium5[Rnd.get(posMedium5.length - 1)];
		} else if (worldNumber == mediumMapID6) { //share the same world number with largemap8
			loc = posMedium6[Rnd.get(posMedium6.length - 1)];
		} else if (worldNumber == mediumMapID7) {
			loc = posMedium7[Rnd.get(posMedium7.length - 1)];
		} else if (worldNumber == mediumMapID8) {
			loc = posMedium8[Rnd.get(posMedium8.length - 1)];
		} else if (worldNumber == mediumMapID9) {
			loc = posMedium9[Rnd.get(posMedium9.length - 1)];
		} else if (worldNumber == mediumMapID10) {
			loc = posMedium10[Rnd.get(posMedium10.length - 1)];
		} else if (worldNumber == mediumMapID11) {
			loc = posMedium11[Rnd.get(posMedium11.length - 1)];
		} else if (worldNumber == mediumMapID12) {
			loc = posMedium12[Rnd.get(posMedium12.length - 1)];
		} else if (worldNumber == mediumMapID13) {
			loc = posMedium13[Rnd.get(posMedium13.length - 1)];
		} else if (worldNumber == mediumMapID14) {
			loc = posMedium14[Rnd.get(posMedium14.length - 1)];
		} else if (worldNumber == mediumMapID15) {
			loc = posMedium15[Rnd.get(posMedium15.length - 1)];
		} else if (worldNumber == mediumMapID16) {
			loc = posMedium16[Rnd.get(posMedium16.length - 1)];
		}
	}
	private int getRandomNum() {
		return randomNum;
	}
	private void setRandomNum(int randomNum) {
		this.randomNum = randomNum;
	}
	private void randomMediumInstance() {	
	   	Random r = new Random();
	   	int  i = r.nextInt(16) + 1;
	   	if (getRandomNum() == 0) {
	   		setRandomNum(i);
	   	} else {
		   	while (i == randomNum) {
				i = r.nextInt(16) + 1;
			}
		   	setRandomNum(i);
	   	}
		if (i == 1) {
			worldName = "Kamar Battlefield";
			worldNumber = 301120000;
			instance = mediumInstance1;
	    } else if (i == 2) {
			worldName = "Fire Temple"; 
			worldNumber = 320100000;
			instance = mediumInstance2; 
	    } else if (i == 3) {
	    	worldName = "Beshmundir Temple";
	    	worldNumber = 300170000;
	    	instance = mediumInstance3; // share the same instance with large map 12
	    } else if (i == 4) {
	    	worldName = "Padmarashka's Cave";
	    	worldNumber = 320150000;
	    	instance = mediumInstance4;
	    } else if (i == 5) {
	    	worldName = "Right Wing Chamber";
	    	worldNumber = 300090000;
	    	instance = mediumInstance5;
	    } else if (i == 6) {
	    	worldName = "Kromede Trial";
	    	worldNumber = 300230000;
	    	instance = mediumInstance6; // share the same instance with large map 8
	    	openDoors(instance);
	    } else if (i == 7) {
	    	worldName = "Rentus Base";
	    	worldNumber = 300280000;
	    	instance = mediumInstance7;
	    } else if (i == 8) {
	    	worldName = "Udas Temple";
	    	worldNumber = 300150000;
	    	instance = mediumInstance8;
	    } else if (i == 9) {
	    	worldName = "Ohpidan Bridge";
	    	worldNumber = 300590000;
	    	instance = mediumInstance9;
	    } else if (i == 10) {
	    	worldName = "Sulfur Treenet";
	    	worldNumber = 300060000;
	    	instance = mediumInstance10;
	    } else if (i == 11) {
			worldName = "Aturam Fortress"; 
			worldNumber = 300240000;
			instance = mediumInstance11; 
	    } else if (i == 12) {
			worldName = "Asteria Chamber"; 
			worldNumber = 300050000;
			instance = mediumInstance12; 
	    } else if (i == 13) {
			worldName = "Alquimia Lab"; 
			worldNumber = 320110000;
			instance = mediumInstance13; 
			openDoors(instance);
	    } else if (i == 14) {
			worldName = "LinkgateFoundry"; 
			worldNumber = 301270000;
			instance = mediumInstance14; 
			openDoors(instance);
	    } else if (i == 15) {
			worldName = "Shadow Court"; 
			worldNumber = 320120000;
			instance = mediumInstance15; 
			openDoors(instance);
	    } else if (i == 16) {
			worldName = "Draupnir Cave"; 
			worldNumber = 320080000;
			instance = mediumInstance16; 
	    } 
	} 	
    private void changeMap(final WorldMapInstance inst) {        	

    	randomMediumInstance();
    
		log.info("Rotating 2v2v2 Map: " + worldNumber + " - " + worldName +"");
        for (Player p : inst.getPlayersInside()) {
        	PacketSendUtility.sendPacket(p, new SM_ITEM_USAGE_ANIMATION(p.getObjectId(), 0, 0, (int) TimeUnit.SECONDS.toMillis(10), 0, 0));
        	PacketSendUtility.sendWhiteMessageOnCenter(p, "2v2v2: Rotating map. All players will be teleport in 10 seconds.");
    	 	paralizePlayer(p, true);
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
        		for (final Player p : inst.getPlayersInside()) {
        			paralizePlayer(p, false);
        			if (p.isOnline() && !p.getLifeStats().isAlreadyDead()) {        					
    					if (p.isInGroup2()) {
    						PlayerGroup group = p.getPlayerGroup2();
    						if (group.isLeader(p)) {
    							randomLoc();
    							for (Player member : group.getMembers()) {   
    								PacketSendUtility.broadcastPacketAndReceive(member, new SM_ITEM_USAGE_ANIMATION(p.getObjectId(), 0, 0, 0, 1, 0));
    								TeleChangeMap(member);        								
    							}
    						}        						
    					} else {
    						PacketSendUtility.broadcastPacketAndReceive(p, new SM_ITEM_USAGE_ANIMATION(p.getObjectId(), 0, 0, 0, 1, 0));
        			    	randomLoc();
        					TeleChangeMap(p);   
    					}
    				}
    			}    
            }
        }, (int) TimeUnit.SECONDS.toMillis(10));  
    }        
   	private void TeleChangeMap (Player player) {
    	TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
	    HealPlayer(player, false, true);
   	}   	
    private ScheduledFuture<?> instanceTask(int delayInMinutes) {
    	return ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {	
    		@Override
    		public void run() {    
				oldInstance = instance;		
				deSpawnPortal(spawnAsmo);
				deSpawnPortal(spawnEly);
				changeMap(oldInstance);     	
    		}
    	}, delayInMinutes / 2 * 1000 * 60, delayInMinutes / 2 * 1000 * 60);  
    }    
    public int getDFFASize () {
    	return getPlayerCountDFFA(instance);
    }   	
    
    /**
 	 * Teleport Players In
 	 */
    
    public void TeleIn(Player player) {
    	randomLoc(); 	
        if (player.isInDuoFFA()){
        	//re-spawn after death        	
        	if (player.isInGroup2()) {
        		PlayerGroup group = player.getPlayerGroup2();
	        	for (Player member : group.getMembers()) {   
	        		if (player != member) {
	        			if (!member.getLifeStats().isAlreadyDead() && member.isOnline()) {
	        				TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), member.getX(), member.getY(), member.getZ(), (byte) member.getHeading(), TeleportAnimation.JUMP_ANIMATION_3);
	        				PacketSendUtility.sendWhiteMessageOnCenter(player, "[2v2v2] You are teleported to your team mate.");
	        			} else {
	        				TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
	        			}
	        		}
	        	}   
        	} else {
        		TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
        	}
    	    HealPlayer(player, false, true);
            AddProtection(player, 10 * 1000);
            player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        } else {        	
        	//first enter 2v2v2 with group    	
        	PlayerGroup group = player.getPlayerGroup2();
    		for (Player member : group.getMembers()) {          	
		    	TeleportService2.teleportTo(member, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
	        	resetStart(member);   
    		}      		
            log.info(String.format("[2v2v2] %s's Team Enter - Map: %s", player.getName(), worldName));
            if (instance != null && getPlayerCountDFFA(instance) > 1) {
            	PacketSendUtility.sendSpecMessage("2v2v2", player.getName() + "'s team has joined 2v2v2 with " + getPlayerCountDFFA(instance) +" players.");
            	PacketSendUtility.sendMessage(player, " To Leave 2v2 Type (.2v2)");	
            } else {
            	PacketSendUtility.sendSpecMessage("2v2v2", player.getName() + "'s team has joined 2v2v2.");
            	PacketSendUtility.sendMessage(player, " To Leave 2v2 Type (.2v2)");	
            }
        }
    }    
    
    public void TeleOut(Player player) {	
    	resetEnd(player);
        if (player.isInGroup2()) {
            PlayerGroupService.removePlayer(player);
        }
        if (player.world() != 0) {
            TeleportService2.teleportTo(player, player.world(), player.locX(), player.locY(), player.locZ(), (byte) player.locH(), TeleportAnimation.JUMP_ANIMATION_3);
        }
        else {
            TeleportService2.moveToBindLocation(player, true);
        }
        player.clearPrevLoc();    
    }        
    
    /**
     * ---------------- TradeKillAlert Reducing When Using Same Mac
     */

	private void checkIfSameMac(Player winner, Player loser){
        String ip1 = winner.getClientConnection().getIP();
        String mac1 = winner.getClientConnection().getMacAddress();
        String ip2 = loser.getClientConnection().getIP();
        String mac2 = loser.getClientConnection().getMacAddress();
        if ((mac1 != null) && (mac2 != null)) {
            if ((ip1.equalsIgnoreCase(ip2)) && (mac1.equalsIgnoreCase(mac2))) {
                AuditLogger.info(winner, "[TradeKillAlert] You really need to check player " + winner.getName() + " and " + loser.getName() + ", They have same IP and MAC and possible they are Trade Killing in FFA, so please go and check in invisible! (MAC: " + mac1 + ").");
                int lose_ap = 30000;
                int lose_gp = 5000;
                int omegaId = 166020000;
                int tsId = 166030005;
                AbyssPointsService.addAp(winner, -lose_ap); // reducing ap from trade killers
                AbyssPointsService.addAp(loser, -lose_ap);

                AbyssPointsService.addAGp(winner, 0, -lose_gp); // reducing gp from trade killers
                AbyssPointsService.addAGp(loser, 0, -lose_gp);

                winner.getInventory().decreaseByItemId(omegaId, 1); // removing Omega from trade killers
                loser.getInventory().decreaseByItemId(omegaId, 1);

                loser.getInventory().decreaseByItemId(tsId, 1); //removeing TS from trade killers
                winner.getInventory().decreaseByItemId(tsId, 1);
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] You lost " + lose_ap + " AP for Trade Killing!"); // AP Lose msg
                PacketSendUtility.sendMessage(loser, "[TradeKillAlert] You lost " + lose_ap + " AP for Trade Killing!");
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] You lost " + lose_gp + " GP for Trade Killing!"); // GP Lose msg
                PacketSendUtility.sendMessage(loser, "[TradeKillAlert] You lost " + lose_gp + " GP for Trade Killing!");
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] You lost [item:" + omegaId + "] x 1 and [item:" + tsId + "] x 1 item(s) for Trade Killing!"); // item lose Item msg
                PacketSendUtility.sendMessage(loser, "[TradeKillAlert] You lost [item:" + omegaId + "] x 1 and [item:" + tsId + "] x 1 item(s) for Trade Killing!"); // item lose Item msg
                PacketSendUtility.sendMessage(winner, "[TradeKillAlert] Next Time Don't Trade Kill >_>");
                PacketSendUtility.sendMessage(loser, "[TradeKillAlert] Next Time Don't Trade Kill >_>");
                log.info("[FFA-TradeKill] Player " + winner.getName() + " killed " + loser.getName() + " and Have same IP and MAC!");
                return;
            }
            if (ip1.equalsIgnoreCase(ip2)) {
                AuditLogger.info(winner, "[TradeKillAlert] Possible chances that " + winner.getName() + " and " + loser.getName() + " are trade killing in FFA. They have same ip " + ip1 + ".");
                AuditLogger.info(winner, "[TradeKillAlert] If not, they are in some kinda cafe, with same network. OR USING SAME WTFAST Connection!!");
            }
        }
    }
    
    /**
     * ----------------On die Section & Rewards
     */

	public void onRevive(Player player) {      		
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);		
		if (player.isInDuoFFA()) {
			TVTService.getInstance().TeleIn(player); 
		} else {
        	TeleportService2.teleportTo(player, worldNumber, instance.getInstanceId(), (float) loc.x, (float) loc.y, (float) loc.z, (byte) loc.h, TeleportAnimation.JUMP_ANIMATION_3);
		}
	}
	
    public void onDead(final Player player, Creature lastAttacker) {
        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
        PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));    	
    	onReward(player, lastAttacker);
    }    
    public void onReward(Player victim, Creature lastAttacker) {	
    	if (lastAttacker.getActingCreature() instanceof Player && victim != lastAttacker) {    	
    		Player winner;
    		Killer =  victim.getAggroList().getMostPlayerDamage();	
    		if (Killer.getLifeStats().isAlreadyDead()) {
				winner = (Player) lastAttacker;		
				checkIfSameMac(winner, victim);
			} else {
				winner = Killer;
			}	
    		for (Player p: winner.getPlayerGroup2().getMembers()){
			AbyssPointsService.addAGp(p, 0, 100);
			InGameShopEn.getInstance().addToll(p, 3);
			PacketSendUtility.sendMessage(p, "You have receive 3 tolls. Totals: "+ winner.getPlayerAccount().getToll());
    		}
    		winner.setSpecialKills();
    		checkIfSameMac(winner, victim);
         	//send 2v2v2 Message die
        	sendSpecMessage("2v2v2", String.format("%s's team has slain %s..ouch \uE07A!", winner.getName(), victim.getName()), instance); 
	        throwStreakAnnouncement(winner);
        }
    }	
    private void throwStreakAnnouncement(Player winner) {
        if(winner.getSpecialKills() == 5) {        	
            PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is your new Enemy! +10 Hero Points  (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
            ItemService.addItem(winner, 166030005, 1);	 
        } else if (winner.getSpecialKills() == 10) {
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is the War Champion! +15 Hero Points (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
        	ItemService.addItem(winner, 166030005, 1);	 
        } else if (winner.getSpecialKills() == 15) {
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " wants more blood! +20 Hero Points (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
        	ItemService.addItem(winner, 166030005, 1);	 
        } else if (winner.getSpecialKills() == 20) {
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is like a insane monster! +25 Hero Points (Kills: " + winner.getSpecialKills() +")", ChatType.BRIGHT_YELLOW_CENTER, false); 
        	ItemService.addItem(winner, 166030005, 1);	 
        } else if (winner.getSpecialKills() == 25) {
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is a Killing Machine! Go Hunt Him Down (+30 HP)", ChatType.BRIGHT_YELLOW_CENTER, false); 
        	ItemService.addItem(winner, 166030005, 1);	 
        } else if (winner.getSpecialKills() == 30) {
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + ", Are you okay? " + winner.getSpecialKills() + " fucking kills?? (+35 HP)", ChatType.BRIGHT_YELLOW_CENTER, false); 
        	ItemService.addItem(winner, 166030005, 1);	 
        } else if (winner.getSpecialKills() == 35) {
        	ItemService.addItem(winner, 166030005, 1);	 
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is a BOSS, certified KILLER! (+40 HP)", ChatType.BRIGHT_YELLOW_CENTER, false); 
        } else if (winner.getSpecialKills() == 40) {
        	ItemService.addItem(winner, 166030005, 1);	 
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is a Hero! (+45 HP)", ChatType.BRIGHT_YELLOW_CENTER, false); 
        } else if (winner.getSpecialKills() == 45) {
        	ItemService.addItem(winner, 166030005, 1);	 
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " knows the Enemies best! (+50 HP)", ChatType.BRIGHT_YELLOW_CENTER, false); 
        } else if (winner.getSpecialKills() == 50) {
        	ItemService.addItem(winner, 166030005, 1);	 
        	PacketSendUtility.sendAnnounceMessage("[2v2v2] "+ winner.getName() + " is the king of 2v2v2.", ChatType.BRIGHT_YELLOW_CENTER, false); 
            TeleIn(winner);
        } 
    }    
    private void resetEnd(Player player) {
        player.setInDuoFFA(false);
        AddProtection(player, 10 * 1000);
        player.getController().cancelCurrentSkill();
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        if (player.getAccessLevel() > 4) {
			player.setInvul(true);
			player.setAdminNeutral(3);
			player.setAdminEnmity(0);
			player.setSeeState(CreatureSeeState.SEARCH10);
		}	
		HealPlayer(player, false, true);
       	log.info(String.format("[2v2v2] %s Exit - Killed: %s", player.getName(), player.getSpecialKills()));
       	player.setZeroSpecialKills();
       	//PacketSendUtility.sendSpecMessage("2v2v2", player.getName() + " has left the battle.");
    }
    private void resetStart(Player player) {
    	player.setInDuoFFA(true);
    	player.setZeroSpecialKills();
    	InstanceService.registerPlayerWithInstance(instance, player);
    	AddProtection(player, 10 * 1000);
    	if (player.getAccessLevel() > 4) {
			player.setInvul(false);
			player.setAdminNeutral(0);
			player.unsetSeeState(CreatureSeeState.SEARCH10);
		}	
    	HealPlayer(player, false, true);
    }
}
