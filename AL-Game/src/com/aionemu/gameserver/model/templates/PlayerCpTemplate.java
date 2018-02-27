/**
 * This file is part of Archsoft Home Development
 */
package com.aionemu.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Xnemonix
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "player_cp_point")
public class PlayerCpTemplate {

    @XmlAttribute(name = "player_level", required = true)
    private int playerlevel;
    @XmlAttribute(name = "cp", required = true)
    private int cp;

    
    
    public int getPlayerCpPoint() {
        return playerlevel;
    }
    
    public int getCpPoint() {
        return cp;
    }
    
}
