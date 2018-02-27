/**
 * This file is part of Archsoft Development.
 *  @author xnemonx
 */
package com.aionemu.gameserver.skillengine.periodicaction;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author xnemonx
 */
public class DpUsePeriodicAction extends PeriodicAction {

    @XmlAttribute(name = "value")
    protected int value;

    @Override
    public void act(Effect effect) {
      /*  Creature effected = effect.getEffected();
        int requiredDp = value;
        if (effected.getLifeStats().getCurrentHp() < requiredDp) {
            effect.endEffect();
            return;
        }

        effected.getLifeStats().reduceHp(requiredDp, effected);*/
    }
}