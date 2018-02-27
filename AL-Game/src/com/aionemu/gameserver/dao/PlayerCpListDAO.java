/**
 * This file is part of Archsoft Development.
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.cp.PlayerCpList;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * Created on: 15.07.2009 19:33:07 Edited On: 13.09.2009 19:48:00
 *
 * @author Xnemonix
 */
public abstract class PlayerCpListDAO implements DAO {

    /**
     * Returns unique identifier for PlayerSkillListDAO
     *
     * @return unique identifier for PlayerSkillListDAO
     */
    @Override
    public final String getClassName() {
        return PlayerCpListDAO.class.getName();
    }

    /**
     * Returns a list of skilllist for player
     *
     * @param playerId Player object id.
     * @return a list of skilllist for player
     */
    public abstract PlayerCpList loadCpList(int playerId);

    /**
     * Updates skill with new information
     *
     * @param playerId
     * @param skillId
     * @param skillLevel
     */
    public abstract boolean storeCps(Player player);
}
