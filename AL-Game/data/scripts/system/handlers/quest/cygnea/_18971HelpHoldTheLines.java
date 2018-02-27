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
package quest.cygnea;

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
public class _18971HelpHoldTheLines extends QuestHandler {

    private final static int questId = 18971;
    private final static int[] mob_ids = {235824, 235825, 235867, 235868, 235881};

    public _18971HelpHoldTheLines() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(804865).addOnQuestStart(questId);
        qe.registerQuestNpc(804865).addOnTalkEvent(questId);
		qe.registerQuestNpc(805213).addOnTalkEvent(questId);
		qe.registerQuestNpc(805214).addOnTalkEvent(questId);
		qe.registerQuestNpc(805215).addOnTalkEvent(questId);
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
				int[] rotrons = {235824, 235825};
				int[] statues = {235867, 235868};
				int[] slimes = {235881};
                int targetId = env.getTargetId();
                int var1 = qs.getQuestVarById(1);
                int var2 = qs.getQuestVarById(2);
				int var3 = qs.getQuestVarById(3);
                switch (targetId) {
					case 235824:
                    case 235825: {
                        if (var1 < 3) {
                            return defaultOnKillEvent(env, rotrons, 0, 3, 1);
                        } else if (var1 == 3) {
                            if (var2 == 4 && var3 == 4) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, rotrons, 3, 4, 1);
                            }
                        }
                        break;
                    }
					case 235867:
                    case 235868: {
                        if (var2 < 3) {
                            return defaultOnKillEvent(env, statues, 0, 3, 2);
                        } else if (var2 == 3) {
                            if (var1 == 4 && var3 == 4) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, statues, 3, 4, 2);
                            }
                        }
                        break;
                    }
					case 235881: {
                        if (var3 < 3) {
                            return defaultOnKillEvent(env, slimes, 0, 3, 3);
                        } else if (var3 == 3) {
                            if (var1 == 4 && var2 == 4) {
                                qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
                                return true;
                            } else {
                                return defaultOnKillEvent(env, slimes, 3, 4, 3);
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
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 804865) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            if ((targetId == 805213) || (targetId == 805214) || (targetId == 805215)) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) { 
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestDialog(env, 1352);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if ((targetId == 805213) || (targetId == 805214) || (targetId == 805215)) {
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
