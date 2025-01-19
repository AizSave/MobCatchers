package mobcatchers.mobs;

import mobcatchers.ai.ConfusedHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.hostile.CaveMoleMob;
import necesse.entity.particle.Particle;

import java.awt.*;

public class BwCaveMole extends CaveMoleMob {

    public BwCaveMole() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.ai = new BehaviourTreeAI<>(this, new ConfusedHostileChaserWandererAI<CaveMoleMob>(null, 512, 256, 40000, true, false) {
            public boolean attackTarget(CaveMoleMob mob, Mob target) {
                boolean success = this.shootSimpleProjectile(mob, target, "stone", CaveMoleMob.damage, 80, 384);
                if (success) {
                    this.wanderAfterAttack = GameRandom.globalRandom.getChance(0.75F);
                }

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
