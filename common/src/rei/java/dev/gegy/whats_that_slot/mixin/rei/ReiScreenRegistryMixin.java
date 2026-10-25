package dev.gegy.whats_that_slot.mixin.rei;

import dev.gegy.whats_that_slot.ui.SlotQueryingScreen;
import me.shedaniel.rei.api.client.registry.screen.ClickArea;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "me.shedaniel.rei.impl.client.registry.screen.ScreenRegistryImpl", remap = false)
public class ReiScreenRegistryMixin {
    // A popup obscures recipe arrows beneath it, including their hover tooltips.
    @Inject(method = "evaluateClickArea", at = @At("HEAD"), cancellable = true)
    private void hideCoveredClickAreas(Class<?> screenClass, ClickArea.ClickAreaContext<?> context,
                                      CallbackInfoReturnable<List<ClickArea.Result>> ci) {
        if (context.getScreen() instanceof SlotQueryingScreen screen) {
            var bounds = screen.whats_that_slot$getQueryBounds();
            var mouse = context.getMousePosition();
            if (bounds != null && bounds.contains(mouse.x, mouse.y)) {
                ci.setReturnValue(List.of());
            }
        }
    }
}
