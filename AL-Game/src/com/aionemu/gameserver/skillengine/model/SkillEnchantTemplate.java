/**
 * This file is part of Archsoft Home Development
 */
package com.aionemu.gameserver.skillengine.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Xnemonix
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cp_info")
public class SkillEnchantTemplate {

    @XmlAttribute(name = "id", required = true)
    private int id;
    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlAttribute(name = "category", required = true)
    private String category;
    @XmlAttribute(name = "skill_group", required = true)
    private String skillgroup;
    @XmlAttribute
    private int stat_value;
    @XmlAttribute
    private int cp_count_max;
    @XmlAttribute
    private int cp_cost;
    @XmlAttribute
    private int cp_cost_adj;
    
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getSkillGroup() {
        return skillgroup;
    }
    
    public int getStatValue() {
        return stat_value;
    }
    
    public int getCpCountMax() {
        return cp_count_max;
    }
    
    public int getCpCost() {
        return cp_cost;
    }
    
    public int getCpCostAdj() {
        return cp_cost_adj;
    }
}
