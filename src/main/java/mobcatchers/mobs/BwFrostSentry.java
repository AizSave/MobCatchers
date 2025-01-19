package mobcatchers.mobs;

import mobcatchers.ai.StationaryHostileShooterAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.hostile.FrostSentryMob;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.FrostSentryProjectile;

import java.awt.*;

public class BwFrostSentry extends FrostSentryMob {

    public BwFrostSentry() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.ai = new BehaviourTreeAI<>(this, new StationaryHostileShooterAI<BwFrostSentry>(320) {
            public void shootTarget(BwFrostSentry mob, Mob target) {
                FrostSentryProjectile projectile = new FrostSentryProjectile(BwFrostSentry.this.getLevel(), mob, mob.x, mob.y, target.x, target.y, 75.0F, 512, BwFrostSentry.damage, 50);
                projectile.x -= projectile.dx * 20.0F;
                projectile.y -= projectile.dy * 20.0F;
                BwFrostSentry.this.attack((int) (mob.x + projectile.dx * 100.0F), (int) (mob.y + projectile.dy * 100.0F), false);
                BwFrostSentry.this.getLevel().entityManager.projectiles.add(projectile);
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
