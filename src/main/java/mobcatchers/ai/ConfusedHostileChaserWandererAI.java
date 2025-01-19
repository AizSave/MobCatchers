package mobcatchers.ai;

import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.event.ConfuseWanderAIEvent;
import necesse.entity.mobs.ai.behaviourTree.leaves.ConfusedWandererAINode;

import java.awt.geom.Point2D;
import java.util.function.Supplier;

abstract public class ConfusedHostileChaserWandererAI<T extends AttackAnimMob> extends HostileChaserWandererAI<T> {
    public final ConfusedWandererAINode<T> confusedWandererNode;
    protected boolean wanderAfterAttack = false;

    public ConfusedHostileChaserWandererAI(Supplier<Boolean> shouldEscape, int searchDistance, int shootDistance, int wanderFrequency, boolean smartPositioning, boolean changePositionOnHit) {
        super(shouldEscape, searchDistance, shootDistance, wanderFrequency, smartPositioning, changePositionOnHit);
        this.addChildFirst(new AINode<T>() {
            protected void onRootSet(AINode<T> root, T mob, Blackboard<T> blackboard) {
                blackboard.onEvent("wanderAfterAttack", (e) -> {
                    ConfusedHostileChaserWandererAI.this.wanderAfterAttack = true;
                });
            }

            public void init(T mob, Blackboard<T> blackboard) {
            }

            public AINodeResult tick(T mob, Blackboard<T> blackboard) {
                if (ConfusedHostileChaserWandererAI.this.wanderAfterAttack && !mob.isAttacking) {
                    ConfusedHostileChaserWandererAI.this.wanderAfterAttack = false;
                    ConfusedHostileChaserWandererAI.this.runWanderAfterAttack(mob, blackboard);
                }

                return AINodeResult.FAILURE;
            }
        });
        this.addChildFirst(this.confusedWandererNode = new ConfusedWandererAINode<>());
    }

    protected int getRandomConfuseTime() {
        return GameRandom.globalRandom.getChance(0.1F) ? GameRandom.globalRandom.getIntBetween(2000, 4000) : GameRandom.globalRandom.getIntBetween(1000, 1500);
    }

    public void runWanderAfterAttack(T mob, Blackboard<T> blackboard) {
        if (mob.attackDir != null) {
            float attackAngle = GameMath.getAngle(mob.attackDir);
            float runAwayAngle;
            if (GameRandom.globalRandom.nextBoolean()) {
                runAwayAngle = GameRandom.globalRandom.getFloatBetween(attackAngle - 90.0F, attackAngle - 110.0F);
            } else {
                runAwayAngle = GameRandom.globalRandom.getFloatBetween(attackAngle + 90.0F, attackAngle + 110.0F);
            }

            runAwayAngle = GameMath.fixAngle(runAwayAngle);
            Point2D.Float runAwayDir = GameMath.getAngleDir(runAwayAngle);
            int confuseTime = this.getRandomConfuseTime();
            blackboard.submitEvent("confuseWander", new ConfuseWanderAIEvent((long) confuseTime, runAwayDir));
        }

    }
}
