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
import com.aionemu.gameserver.skillengine.model.SkillEnchantTemplate;

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
@XmlRootElement(name = "cp_infos")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillEnchantData {

    @XmlElement(name = "cp_info")
    private List<SkillEnchantTemplate> skillTemplates;
    private final TIntObjectHashMap<ArrayList<SkillEnchantTemplate>> templates = new TIntObjectHashMap<ArrayList<SkillEnchantTemplate>>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
        for (SkillEnchantTemplate template : skillTemplates) {
            addTemplate(template);
        }
        //skillTemplates = null;
    }

    private void addTemplate(SkillEnchantTemplate template) {
        int slotId = template.getId() ;
        ArrayList<SkillEnchantTemplate> value = templates.get(slotId);
        if (value == null) {
            value = new ArrayList<SkillEnchantTemplate>();
            templates.put(slotId, value);
        }
        value.add(template);
    }
    private TIntObjectHashMap<SkillEnchantTemplate> skillData = new TIntObjectHashMap<SkillEnchantTemplate>();
    public TIntObjectHashMap<ArrayList<SkillEnchantTemplate>> getTemplates() {
        return templates;
    }
    
    public SkillEnchantTemplate getSlotTemplate(int slotId) {
        return skillData.get(slotId);
    }
    
    public SkillEnchantTemplate[] getTemplatesForGroup(int slotid) {
        List<SkillEnchantTemplate> newSkills = new ArrayList<SkillEnchantTemplate>();
        List<SkillEnchantTemplate> generalTemplates = templates.get(slotid);
        
        if (generalTemplates != null) {
            newSkills.addAll(generalTemplates);
        }

        return newSkills.toArray(new SkillEnchantTemplate[newSkills.size()]);
    }

    public int size() {
        int size = 0;
        for (Integer key : templates.keys()) {
            size += templates.get(key).size();
        }
        return size;
    }

}
