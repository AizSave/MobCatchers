package mobcatchers.mobs;

import mobcatchers.ai.CollisionHostileChaserAI;
import mobcatchers.ai.ConfusedCollisionHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionPlayerChaserAI;
import necesse.entity.mobs.hostile.ZombieMob;
import necesse.entity.mobs.hostile.pirates.PirateParrotMob;
import necesse.entity.particle.Particle;

import java.awt.*;

public class BwPirateParrot extends PirateParrotMob {

    public BwPirateParrot() {
        super();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void init() {
        super.init();
        this.isHostile = false;
        this.setTeam(-1);
    }

    public void setupAI() {
        if (this.baseTile == null || this.baseTile.x == 0 && this.baseTile.y == 0) {
            this.baseTile = new Point(this.getX() / 32, this.getY() / 32);
        }

        this.ai = new BehaviourTreeAI<>(this, new CollisionHostileChaserAI<>(1280, new GameDamage((float)this.meleeDamage), 100));
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
