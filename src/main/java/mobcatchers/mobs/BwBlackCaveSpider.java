package mobcatchers.mobs;

import mobcatchers.ai.ConfusedCollisionHostileChaserWandererAI;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.hostile.BlackCaveSpiderMob;
import necesse.entity.mobs.hostile.ZombieMob;
import necesse.entity.particle.Particle;
import necesse.inventory.item.matItem.MultiTextureMatItem;
import necesse.inventory.lootTable.LootItemInterface;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;

import java.awt.*;

public class BwBlackCaveSpider extends BwGiantCaveSpider {

    public static LootTable lootTable = new LootTable(new LootItemInterface[]{LootItem.between("cavespidergland", 1, 2, MultiTextureMatItem.getGNDData(1))});

    public BwBlackCaveSpider() {
        super(Variant.BLACK, 250, new GameDamage(35.0F), new GameDamage(25.0F));
        this.setSpeed(35.0F);
        this.setArmor(10);
    }

    public LootTable getLootTable() {
        return lootTable;
    }
}
