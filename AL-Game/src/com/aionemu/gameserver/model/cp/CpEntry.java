/*
 * This file is part of Archsoft Home Software Development
 */
package com.aionemu.gameserver.model.cp;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.model.SkillEnchantTemplate;

/**
 * @author xnemonix
 *
 */
public abstract class CpEntry {

    protected final int slotId;
    protected int cpPoint;

    CpEntry(int slotId, int cpPoint) {
        this.slotId = slotId;
        this.cpPoint = cpPoint;
    }

    public final int getSlotId() {
        return slotId;
    }

    public final int getCpPoint() {
        return cpPoint;
    }

    public final String getCpName() {
        return DataManager.SKILL_ENCHANT_DATA.getSlotTemplate(slotId).getName();
    }
    
/*    public final String getCpCategory() {
        return DataManager.SKILL_ENCHANT_DATA.getSlotTemplate(getSlotId()).getCategory();
    }
    
    public final String getCpSkillGroup() {
        return DataManager.SKILL_ENCHANT_DATA.getSlotTemplate(getSlotId()).getSkillGroup();
    }*/

    public void setCpPoint(int cpPoint) {
        this.cpPoint = cpPoint;
    }

    public final SkillEnchantTemplate getSlotTemplate() {
        return DataManager.SKILL_ENCHANT_DATA.getSlotTemplate(getSlotId());
    }
}
