package mobcatchers.mobs;

import mobcatchers.ai.ConfusedCollisionHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.event.ConfuseWanderAIEvent;
import necesse.entity.mobs.ai.behaviourTree.trees.ConfusedCollisionPlayerChaserWandererAI;
import necesse.entity.mobs.hostile.GoblinMob;
import necesse.entity.mobs.hostile.ZombieMob;
import necesse.entity.particle.Particle;

import java.awt.*;
import java.awt.geom.Point2D;

public class BwGoblin extends GoblinMob {

    public BwGoblin() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.ai = new BehaviourTreeAI<>(this, new ConfusedCollisionHostileChaserWandererAI<BwGoblin>(() -> {
            return !this.getLevel().isCave && !this.getLevel().getServer().world.worldEntity.isNight();
        }, 384, new GameDamage(16.0F), 100, 40000) {
            public boolean attackTarget(BwGoblin mob, Mob target) {
                boolean success = super.attackTarget(mob, target);
                Point2D.Float runAwayDir = GameMath.normalize(mob.x - target.x, mob.y - target.y);
                int confuseTime = GameRandom.globalRandom.getIntBetween(2000, 3000);
                this.getBlackboard().submitEvent("confuseWander", new ConfuseWanderAIEvent((long)confuseTime, runAwayDir));
                return success;
            }
        });
        this.isHostile = false;
        this.setTeam(-1);
    }

    @Override
    public GameMessage getLocalization() {
        return new StaticMessage(Localization.translate("item", "brainwashedmobname", "mob", MobRegistry.getLocalization(this.getStringID().replace("_brainwashed", "")).translate()));
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (this.isVisible()) {
            this.getLevel().entityManager.addParticle(this.x + GameRandom.globalRandom.nextInt(5) + (float) (GameRandom.globalRandom.nextGaussian() * 6.0), this.y + GameRandom.globalRandom.nextInt(5) + (float) (GameRandom.globalRandom.nextGaussian() * 8.0), Particle.GType.IMPORTANT_COSMETIC).movesFriction(this.dx / 10.0F, this.dy / 10.0F, GameRandom.globalRandom.getFloatBetween(0, 10F)).color(new Color(255, 0, 255)).height(GameRandom.globalRandom.getFloatBetween(this.getFlyingHeight(), 16F + this.getFlyingHeight()));
        }
    }
}
