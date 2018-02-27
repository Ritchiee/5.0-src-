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
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author pralinka
 */
public class _28971DisputedTerritoryDefense extends QuestHandler {

    private final static int questId = 28971;
    private final static int[] mob_ids = {219686, 219687, 219728, 219729, 219746, 219747};

    public _28971DisputedTerritoryDefense() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(804924).addOnQuestStart(questId);
        qe.registerQuestNpc(804924).addOnTalkEvent(questId);
		qe.registerQuestNpc(805216).addOnTalkEvent(questId);
		qe.registerQuestNpc(805217).addOnTalkEvent(questId);
		qe.registerQuestNpc(805218).addOnTalkEvent(questId);
        for (int mob_id : mob_ids) {
            qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
        }
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 0) {
				int[] spirits = {219686, 219687};
				int[] lapilimas = {219728, 219729};
				int[] vortiles = {219746, 219747};
                int targetId = env.getTargetId();
                int var1 = qs.getQuestVarById(1);
                int var2 = qs.getQuestVarById(2);
				int var3 = qs.getQuestVarById(3);
                switch (targetId) {
					case 219686:
                    case 219687: {
                        if (var1 < 3) {
                            return defaultOnKillEvent(env, spirits, 0, 3, 1);
                        } else if (var1 == 3) {
                            if (var2 == 4 && var3 == 4) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, spirits, 3, 4, 1);
                            }
                        }
                        break;
                    }
					case 219728:
                    case 219729: {
                        if (var2 < 3) {
                            return defaultOnKillEvent(env, lapilimas, 0, 3, 2);
                        } else if (var2 == 3) {
                            if (var1 == 4 && var3 == 4) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, lapilimas, 3, 4, 2);
                            }
                        }
                        break;
                    }
					case 219746:
					case 219747: {
                        if (var3 < 3) {
                            return defaultOnKillEvent(env, vortiles, 0, 3, 3);
                        } else if (var3 == 3) {
                            if (var1 == 4 && var2 == 4) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, vortiles, 3, 4, 3);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 804924) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if ((targetId == 805216) || (targetId == 805217) || (targetId == 805218)) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) { 
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestDialog(env, 1352);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if ((targetId == 805216) || (targetId == 805217) || (targetId == 805218)) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 5);
                } else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
                    return sendQuestDialog(env, 5);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}
