package dev.gegy.whats_that_slot.ui.action;

import dev.gegy.whats_that_slot.mixin.AbstractContainerScreenAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ScreenSlotActions {
    // Container clicks use protocol button 0 for primary, independent of native mouse IDs.
    private static final int PRIMARY_CONTAINER_CLICK = 0;

    private final List<Action> actions = new ArrayList<>();

    public ScreenSlotActions swapStacks(Slot sourceSlot, Slot targetSlot) {
        if (sourceSlot == targetSlot) return this;
        if (!targetSlot.mayPlace(sourceSlot.getItem())) return this;

        this.clickSlot(sourceSlot);
        this.clickSlot(targetSlot);

        if (targetSlot.hasItem() && sourceSlot.mayPlace(targetSlot.getItem())) {
            this.clickSlot(sourceSlot);
        }

        return this;
    }

    public ScreenSlotActions clickSlot(Slot slot) {
        this.actions.add((screen, screenAccess) -> {
            screenAccess.invokeSlotClicked(slot, slot.index, PRIMARY_CONTAINER_CLICK, ContainerInput.PICKUP);
        });
        return this;
    }

    public void execute(AbstractContainerScreen<?> screen) {
        var client = Minecraft.getInstance();
        this.executeNextAction(client, screen, this.actions.iterator());
    }

    private void executeNextAction(Minecraft client, AbstractContainerScreen<?> screen, Iterator<Action> iterator) {
        // Queued clicks must never target a menu that the player has already closed.
        if (client.gui.screen() != screen || client.player == null
                || client.player.containerMenu != screen.getMenu()) {
            return;
        }
        if (iterator.hasNext()) {
            var action = iterator.next();
            action.run(screen, (AbstractContainerScreenAccess) screen);

            client.execute(() -> this.executeNextAction(client, screen, iterator));
        }
    }

    public interface Action {
        void run(AbstractContainerScreen<?> screen, AbstractContainerScreenAccess screenAccess);
    }
}
