package mobcatchers;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.WormMobBody;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.AttackingFollowingMob;
import necesse.inventory.item.Item;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import mobcatchers.items.CatchedMobItem;
import mobcatchers.items.MobCatcher;
import mobcatchers.items.MobEssence;

@ModEntry
public class MobCatchers {
    public void init() {
        ItemRegistry.registerItem("mobcatcher", new MobCatcher(), -1F, true);
        MobRegistry.getMobs().forEach(
                m -> {
                    Mob mob = MobRegistry.getMob(m.getStringID(), null);
                    if (
                            (
                                    mob.isHostile || mob.isHuman || mob.isBoss()
                            )
                                    && !(mob instanceof WormMobBody)
                                    && !(mob instanceof AttackingFollowingMob)
                    ) {
                        Item.Rarity rarity;
                        if(mob.getHealth() >= 500) {
                            rarity = Item.Rarity.LEGENDARY;
                        } else if(mob.getHealth() >= 400) {
                            rarity = Item.Rarity.EPIC;
                        } else if(mob.getHealth() >= 300) {
                            rarity = Item.Rarity.UNCOMMON;
                        } else if(mob.getHealth() >= 200) {
                            rarity = Item.Rarity.UNCOMMON;
                        } else if(mob.getHealth() >= 125) {
                            rarity = Item.Rarity.COMMON;
                        } else {
                            rarity = Item.Rarity.NORMAL;
                        }
                        MobEssence.registerEssence(rarity, m.getStringID());
                        CatchedMobItem.registerCatchedMob(rarity, m.getStringID());
                    }
                }
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
    }
}
