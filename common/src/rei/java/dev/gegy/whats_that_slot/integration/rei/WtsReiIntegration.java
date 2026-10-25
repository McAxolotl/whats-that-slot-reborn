package dev.gegy.whats_that_slot.integration.rei;

import dev.architectury.event.CompoundEventResult;
import dev.gegy.whats_that_slot.ui.SlotQueryingScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.List;

/** Optional viewer integration; compiled into each loader, never loaded by the core mod. */
public abstract class WtsReiIntegration implements REIClientPlugin {
    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerFocusedStack((screen, mouse) -> {
            if (screen instanceof SlotQueryingScreen queryingScreen) {
                var item = queryingScreen.whats_that_slot$getHoveredItemAt(mouse.x, mouse.y);
                if (item != null) {
                    return CompoundEventResult.interruptTrue(EntryStacks.of(item.stack()));
                }
            }
            return CompoundEventResult.pass();
        });
        registry.exclusionZones().register(AbstractContainerScreen.class, screen -> {
            if (screen instanceof SlotQueryingScreen queryingScreen) {
                var bounds = queryingScreen.whats_that_slot$getQueryBounds();
                if (bounds != null) {
                    return List.of(new Rectangle(bounds.x0(), bounds.y0(), bounds.width(), bounds.height()));
                }
            }
            return List.of();
        });
    }
}
