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
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Credits goes to all Open Source Core Developer Groups listed below
 * Please do not change here something, ragarding the developer credits, except the "developed by XXXX".
 * Even if you edit a lot of files in this source, you still have no rights to call it as "your Core".
 * Everybody knows that this Emulator Core was developed by Aion Lightning 
 * @-Aion-Unique-
 * @-Aion-Lightning
 * @Aion-Engine
 * @Aion-Extreme
 * @Aion-NextGen
 * @Aion-Core Dev.
 */
package com.aionemu.gameserver.dataholders;
import com.aionemu.gameserver.model.templates.PlayerCpTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
/**
 * @author ATracer
 */
@XmlRootElement(name = "player_cp_points")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerCpTable {

    @XmlElement(name = "player_cp_point")
    private List<PlayerCpTemplate> skillTemplates;
    private final TIntObjectHashMap<ArrayList<PlayerCpTemplate>> templates = new TIntObjectHashMap<ArrayList<PlayerCpTemplate>>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
        for (PlayerCpTemplate template : skillTemplates) {
            addTemplate(template);
        }
        //skillTemplates = null;
    }

    private void addTemplate(PlayerCpTemplate template) {
        int playerlevel = template.getPlayerCpPoint() ;
        ArrayList<PlayerCpTemplate> value = templates.get(playerlevel);
        if (value == null) {
            value = new ArrayList<PlayerCpTemplate>();
            templates.put(playerlevel, value);
        }
        value.add(template);
    }
    private TIntObjectHashMap<PlayerCpTemplate> skillData = new TIntObjectHashMap<PlayerCpTemplate>();
    public TIntObjectHashMap<ArrayList<PlayerCpTemplate>> getTemplates() {
        return templates;
    }
    
    public PlayerCpTemplate getSlotTemplate(int slotId) {
        return skillData.get(slotId);
    }
    
    public PlayerCpTemplate[] getTemplatesForGroup(int slotid) {
        List<PlayerCpTemplate> newSkills = new ArrayList<PlayerCpTemplate>();
        List<PlayerCpTemplate> generalTemplates = templates.get(slotid);
        
        if (generalTemplates != null) {
            newSkills.addAll(generalTemplates);
        }

        return newSkills.toArray(new PlayerCpTemplate[newSkills.size()]);
    }

    public int size() {
        int size = 0;
        for (Integer key : templates.keys()) {
            size += templates.get(key).size();
        }
        return size;
    }

}
