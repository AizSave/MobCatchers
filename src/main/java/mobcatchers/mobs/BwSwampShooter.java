package mobcatchers.mobs;

import mobcatchers.ai.StationaryHostileShooterAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.hostile.SwampShooterMob;
import necesse.entity.particle.Particle;
import necesse.entity.projectile.SwampBoltProjectile;

import java.awt.*;

public class BwSwampShooter extends SwampShooterMob {

    public BwSwampShooter() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.ai = new BehaviourTreeAI<>(this, new StationaryHostileShooterAI<BwSwampShooter>(448) {
            public void shootTarget(BwSwampShooter mob, Mob target) {
                SwampBoltProjectile projectile = new SwampBoltProjectile(BwSwampShooter.this.getLevel(), mob, mob.x, mob.y, target.x, target.y, 100.0F, 640, BwSwampShooter.damage, 50);
                projectile.setTargetPrediction(target);
                BwSwampShooter.this.attack((int) (mob.x + projectile.dx * 100.0F), (int) (mob.y + projectile.dy * 100.0F), false);
                projectile.x += Math.signum(BwSwampShooter.this.attackDir.x) * 10.0F;
                projectile.y += BwSwampShooter.this.attackDir.y * 6.0F;
                BwSwampShooter.this.getLevel().entityManager.projectiles.add(projectile);
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
