package dev.gegy.whats_that_slot.ui.state;

import dev.gegy.whats_that_slot.ui.HoveredItem;
import dev.gegy.whats_that_slot.ui.Bounds2i;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SlotQueryState {
    @Nonnull
    SlotQueryState tick(@Nullable Slot focusedSlot, boolean requestingQuery);

    void draw(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta);

    default InteractionResult isSlotSelected(Slot slot, double mouseX, double mouseY) {
        return InteractionResult.PASS;
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    default boolean mouseScrolled(double amount) {
        return false;
    }

    @Nullable
    default HoveredItem getHoveredItemAt(double x, double y) {
        return null;
    }

    default boolean isActive() {
        return false;
    }

    @Nullable
    default Bounds2i getQueryBounds() {
        return null;
    }
}
