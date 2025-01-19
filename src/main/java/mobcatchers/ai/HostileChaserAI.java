package mobcatchers.ai;

import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.leaves.TargetFinderAINode;
import necesse.entity.mobs.ai.behaviourTree.trees.ChaserAI;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;

import java.awt.*;

abstract public class HostileChaserAI<T extends Mob> extends ChaserAI<T> {
    public HostileChaserAI(int searchDistance, int shootDistance, boolean smartPositioning, boolean changePositionOnHit) {
        super(searchDistance, shootDistance, smartPositioning, changePositionOnHit);
    }

    public GameAreaStream<Mob> streamPossibleTargets(T mob, Point base, TargetFinderDistance<T> distance) {
        return mob.getLevel().entityManager.streamAreaMobsAndPlayers(base.x, base.y, distance.searchDistance).filter(
                m -> m.isHostile
        );
    }
}
