package mobcatchers.objects;

import mobcatchers.MobCatchers;
import necesse.engine.registries.GlobalIngredientRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.sound.SoundPlayer;
import necesse.engine.sound.gameSound.GameSound;
import necesse.entity.objectEntity.AnyLogFueledProcessingTechInventoryObjectEntity;
import necesse.entity.objectEntity.FueledProcessingTechInventoryObjectEntity;
import necesse.gfx.GameResources;
import necesse.inventory.InventoryItem;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Tech;
import necesse.level.maps.Level;

import java.util.Objects;

public class BrainwasherObjectEntity extends FueledProcessingTechInventoryObjectEntity {
    public static int fuelTime = 10000;
    public static int processTime = 1000;
    public GameSound workingSound;
    private SoundPlayer playingSound;

    public BrainwasherObjectEntity(Level level, int x, int y) {
        super(level, "brainwasher", x, y, 1, 1, 1, false, false, false, MobCatchers.BRAINWASHER);
        this.workingSound = GameResources.cling;
    }

    public int getFuelTime(InventoryItem item) {
        return fuelTime;
    }

    public int getProcessTime(Recipe recipe) {
        return processTime;
    }

    public boolean shouldBeAbleToChangeKeepFuelRunning() {
        return false;
    }

    public void clientTick() {
        super.clientTick();
        if (this.workingSound != null && this.isFuelRunning()) {
            if (this.playingSound == null || this.playingSound.isDone()) {
                this.playingSound = SoundManager.playSound(this.workingSound, SoundEffect.effect(this).falloffDistance(400).volume(0.25F));
            }

            if (this.playingSound != null) {
                this.playingSound.refreshLooping(1.0F);
            }
        }

    }

    public boolean isValidFuelItem(InventoryItem item) {
        return Objects.equals(item.item.getStringID(), "brainwashshard");
    }

    public int getNextFuelBurnTime(boolean useFuel) {
        return this.itemToBurnTime(useFuel, (item) -> {
            return Objects.equals(item.item.getStringID(), "brainwashshard") ? this.getFuelTime(item) : 0;
        });
    }
}
