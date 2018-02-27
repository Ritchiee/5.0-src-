/**
 */
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HealType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealExpEffect")
public class HealExpEffect extends AbstractHealEffect {

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, HealType.EXP);
    }

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect, HealType.EXP);
    }

    @Override
    protected int getCurrentStatValue(Effect effect) {
        return 0;
    }

    @Override
    protected int getMaxStatValue(Effect effect) {
        return 0;
    }
}
