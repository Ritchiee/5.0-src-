/**
 * This file is part of Archsoft.
 */
package com.aionemu.gameserver.model.cp;

import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Xnemonix
 */
public interface CpList<T extends Creature> {

    /**
     * Add skill to list
     *
     * @return true if operation was successful
     */
    boolean setCpLearn(T creature, int slotId, int cpPoint);
    boolean setCpEnchant(T creature, int slotId, int cpPoint);
    boolean setCpStat(T creature, int slotId, int cpPoint);
    /**
     * Remove skill from list
     *
     * @return true if operation was successful
     */
    boolean removeCp(int slotId);

    /**
     * Check whether skill is present in list
     */
    boolean isCpPresent(int slotId);

    int getCpPoint(int slotId);

    /**
     * Size of skill list
     */
    int size();
    

}
