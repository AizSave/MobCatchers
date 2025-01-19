package mobcatchers.methodpatches;

import necesse.engine.localization.Localization;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.recipe.Ingredient;
import net.bytebuddy.asm.Advice;
import mobcatchers.items.CatchedMobItem;
import mobcatchers.items.MobEssence;

@ModMethodPatch(target = Ingredient.class, name = "getDisplayName", arguments = {})
public class ItemName {

    @Advice.OnMethodEnter
    static boolean onEnter(@Advice.This Ingredient ingredient) {
        return false;
    }

    @Advice.OnMethodExit
    static void onExit(@Advice.This Ingredient ingredient, @Advice.Return(readOnly = false) String displayName) {
        Item item = ItemRegistry.getItem(ingredient.getIngredientID());
        String itemStringID = item.getStringID();
        if (itemStringID.endsWith("_catched")) {
            displayName = Localization.translate("item", "catchedmobname", "mob", MobRegistry.getLocalization(((CatchedMobItem) item).summonMob).translate());
        } else if (itemStringID.endsWith("_brainwashed")) {
            displayName = Localization.translate("item", "brainwashedmobname", "mob", MobRegistry.getLocalization(((CatchedMobItem) item).summonMob).translate());
        } else if (itemStringID.endsWith("_essence")) {
            displayName = Localization.translate("item", "mobessence", "mob", MobRegistry.getLocalization(((MobEssence) item).mob).translate());
        }
    }
}
