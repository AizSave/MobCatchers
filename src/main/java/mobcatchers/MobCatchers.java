package mobcatchers;

import mobcatchers.items.BrainwashedMobItem;
import mobcatchers.items.CatchedMobItem;
import mobcatchers.items.MobCatcher;
import mobcatchers.items.MobEssence;
import mobcatchers.mobs.*;
import mobcatchers.objects.Brainwasher;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.WormMobBody;
import necesse.entity.mobs.summon.SummonedMob;
import necesse.inventory.item.Item;
import necesse.inventory.item.matItem.MatItem;
import necesse.inventory.item.toolItem.miscToolItem.NetToolItem;
import necesse.inventory.lootTable.LootTablePresets;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import necesse.inventory.recipe.Tech;

@ModEntry
public class MobCatchers {

    public static Tech BRAINWASHER;

    public void init() {
        String suffix = "_brainwashed";
        Class<? extends Mob>[] mobs = new Class[]{
                // Basic
                BwZombie.class,
                BwZombieArcher.class,
                BwCrawlingZombie.class,
                BwGoblin.class,
                BwVampire.class,
                BwJackal.class,
                BwDeepCaveSpirit.class,
                BwSkeletonTrhower.class,
                BwGiantCaveSpider.class,
                BwSkeleton.class,
                BwSkeletonMiner.class,

                // Forest
                BwCaveMole.class,
                BwStabbyBushMonster.class,

                // Snow
                BwFrostSentry.class,
                BwFrozenDwarf.class,
                BwTrapperZombie.class,
                BwCryoFlake.class,
                BwBlackCaveSpider.class,
                BwSnowWolf.class,

                // Swamp
                BwSwampZombie.class,
                BwSwampShooter.class,
                BwSwampSlime.class,
                BwSwampCaveSpider.class,
                BwSwampDweller.class,
                BwSwampSkeleton.class,

                // Desert
                BwSandSpirit.class,
                BwMummyMage.class,
                BwAncientSkeletonMage.class,
                BwAncientSkeletonThrower.class,
                BwMummy.class,
                BwDesertCrawler.class,
                BwAncientSkeleton.class,
                BwAncientArmoredSkeleton.class,

                // Dungeon
                BwEnchantedZombie.class,
                BwEnchantedCrawlingZombie.class,
                BwVoidApprentice.class,

                // Pirate
                BwPirateParrot.class
        };
        for (Class<? extends Mob> mob : mobs) {
            MobRegistry.registerMob(mob.getName().toLowerCase().replace("mobcatchers.mobs.bw", "") + suffix, mob, false);
        }

        ItemRegistry.registerItem("brainwashshard", new MatItem(100, Item.Rarity.EPIC), 10F, true);

        ItemRegistry.registerItem("mobcatcher", new MobCatcher(), -1F, true);
        NetToolItem netToolItem = (NetToolItem) ItemRegistry.getItem("net");
        MobRegistry.getMobs().forEach(
                m -> {
                    try {
                        Mob mob = MobRegistry.getMob(m.getStringID(), null);
                        if (!m.getStringID().endsWith("_brainwashed") && mob.canTakeDamage() || netToolItem.canHitMob(mob, null) && !(mob instanceof WormMobBody) && !(mob instanceof SummonedMob)) {
                            Item.Rarity rarity;
                            if (mob.getHealth() >= 500) {
                                rarity = Item.Rarity.LEGENDARY;
                            } else if (mob.getHealth() >= 400) {
                                rarity = Item.Rarity.EPIC;
                            } else if (mob.getHealth() >= 300) {
                                rarity = Item.Rarity.UNCOMMON;
                            } else if (mob.getHealth() >= 200) {
                                rarity = Item.Rarity.UNCOMMON;
                            } else if (mob.getHealth() >= 125) {
                                rarity = Item.Rarity.COMMON;
                            } else {
                                rarity = Item.Rarity.NORMAL;
                            }
                            MobEssence.registerEssence(rarity, m.getStringID());
                            CatchedMobItem.registerCatchedMob(rarity, m.getStringID());
                            if (MobRegistry.mobExists(m.getStringID() + "_brainwashed")) {
                                BrainwashedMobItem.registerCatchedMob(rarity, m.getStringID());
                            }
                        }
                    } catch (RuntimeException err) {
                        throw new RuntimeException(err);
                    }
                }
        );

        BRAINWASHER = RecipeTechRegistry.registerTech("brainwasher", "brainwasher");
        ObjectRegistry.registerObject("brainwasher", new Brainwasher(), -1F, true);

        LootTablePresets.globalMobDrops.items.add(
                new ChanceLootItem(0.05F, "brainwashshard")
        );
    }

    public void postInit() {
        Recipes.registerModRecipe(
                new Recipe(
                        "mobcatcher",
                        RecipeTechRegistry.DEMONIC_ANVIL,
                        new Ingredient[]{
                                new Ingredient("net", 1),
                                new Ingredient("goldbar", 8),
                                new Ingredient("anymobessence", 20)
                        }
                )
        );

        Recipes.registerModRecipe(
                new Recipe("brainwasher", RecipeTechRegistry.DEMONIC_ANVIL, new Ingredient[]{new Ingredient("brainwashshard", 10), new Ingredient("ironbar", 20), new Ingredient("amethyst", 3)})
        );

        for (Item item : ItemRegistry.getItems()) {
            if (item.getStringID().endsWith("_brainwashed")) {
                Recipes.registerModRecipe(
                        new Recipe(item.getStringID(), BRAINWASHER, new Ingredient[]{new Ingredient(item.getStringID().replace("_brainwashed", "") + "_catched", 1)})
                );
            }
        }
    }
}
