package dev.gegy.whats_that_slot.mixin.jei;

import dev.gegy.whats_that_slot.ui.Bounds2i;
import dev.gegy.whats_that_slot.ui.SlotQueryingScreen;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Pseudo
@Mixin(targets = "mezz.jei.library.gui.helpers.ScreenHelper", remap = false)
public class JeiScreenHelperMixin {
    // Suppress recipe arrows and their tooltips only where the popup covers them.
    @Inject(method = "getGuiClickableArea", at = @At("HEAD"), cancellable = true)
    private void hideCoveredClickAreas(AbstractContainerScreen<?> container, double guiMouseX, double guiMouseY,
                                      CallbackInfoReturnable<Stream<IGuiClickableArea>> ci) {
        if (container instanceof SlotQueryingScreen screen) {
            var bounds = screen.whats_that_slot$getQueryBounds();
            var containerBounds = Bounds2i.ofScreen(container);
            if (bounds != null && bounds.contains(guiMouseX + containerBounds.x0(), guiMouseY + containerBounds.y0())) {
                ci.setReturnValue(Stream.empty());
            }
        }
    }
}
