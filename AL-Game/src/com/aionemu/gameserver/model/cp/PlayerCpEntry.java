/*
 * 
 */
package com.aionemu.gameserver.model.cp;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author xnemonx
 *
 */
public class PlayerCpEntry extends CpEntry {

    private boolean isStatup;
    private boolean isEnchantSkill;
    private boolean isLearnSkill;

    private PersistentState persistentState;

    public PlayerCpEntry(int slotId, boolean isStatup, boolean isEnchantSkill, boolean isLearnSkill, int cpPoint, PersistentState persistentState) {
        super(slotId, cpPoint);
        this.isStatup = isStatup;
        this.isEnchantSkill = isEnchantSkill;
        this.isLearnSkill = isLearnSkill;
        this.persistentState = persistentState;
    }

    /**
     * @return isStigma
     */
    public boolean isStatup() {
        return this.isStatup;
    }
    
    public boolean isEnchantSkill() {
        return this.isEnchantSkill;
    }
    
    public boolean isLearnSkill() {
        return this.isLearnSkill;
    }

    public void setCpPoint(int cpPoint) {
        super.setCpPoint(cpPoint);
        setPersistentState(PersistentState.UPDATE_REQUIRED);
    }

    /**
     * @return the pState
     */
    public PersistentState getPersistentState() {
        return persistentState;
    }

    /**
     * @param persistentState the pState to set
     */
    public void setPersistentState(PersistentState persistentState) {
        switch (persistentState) {
            case DELETED:
                if (this.persistentState == PersistentState.NEW) {
                    this.persistentState = PersistentState.NOACTION;
                } else {
                    this.persistentState = PersistentState.DELETED;
                }
                break;
            case UPDATE_REQUIRED:
                if (this.persistentState != PersistentState.NEW) {
                    this.persistentState = PersistentState.UPDATE_REQUIRED;
                }
                break;
            case NOACTION:
                break;
            default:
                this.persistentState = persistentState;
        }
    }
}

