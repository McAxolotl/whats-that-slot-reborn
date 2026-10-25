package dev.gegy.whats_that_slot.integration.jei;

import dev.gegy.whats_that_slot.WhatsThatSlot;
import dev.gegy.whats_that_slot.ui.SlotQueryingScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IClickableIngredientFactory;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

@JeiPlugin
public final class WtsJeiPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(WhatsThatSlot.ID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(AbstractContainerScreen.class, new IGuiContainerHandler<>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(AbstractContainerScreen screen) {
                if (screen instanceof SlotQueryingScreen queryingScreen) {
                    var bounds = queryingScreen.whats_that_slot$getQueryBounds();
                    if (bounds != null) {
                        return List.of(bounds.toRect());
                    }
                }
                return List.of();
            }

            @Override
            public Optional<? extends IClickableIngredient<?>> getClickableIngredientUnderMouse(
                    IClickableIngredientFactory factory, AbstractContainerScreen screen, double mouseX, double mouseY
            ) {
                if (screen instanceof SlotQueryingScreen queryingScreen) {
                    var item = queryingScreen.whats_that_slot$getHoveredItemAt(mouseX, mouseY);
                    if (item != null) {
                        return factory.createBuilder(item.stack()).buildWithArea(item.bounds().toRect());
                    }
                }
                return Optional.empty();
            }
        });
    }
}
