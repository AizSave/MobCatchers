package mobcatchers.ai;

import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionChaserAI;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;

import java.awt.*;

public class CollisionHostileChaserAI<T extends Mob> extends CollisionChaserAI<T> {
    public CollisionHostileChaserAI(int searchDistance, GameDamage damage, int knockback) {
        super(searchDistance, damage, knockback);
    }

    public GameAreaStream<Mob> streamPossibleTargets(T mob, Point base, TargetFinderDistance<T> distance) {
        return mob.getLevel().entityManager.streamAreaMobsAndPlayers(base.x, base.y, distance.searchDistance).filter(
                m -> m.isHostile
        );
    }
}
