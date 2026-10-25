package dev.gegy.whats_that_slot.mixin;

import dev.gegy.whats_that_slot.ui.SlotQueryingScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow @Final private Minecraft minecraft;

    // Handle the popup before loader screen events (including REI recipe-arrow clicks),
    // but after vanilla has updated activeButton so scrollbar dragging still works.
    @Inject(
            method = "onPress(JIII)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", ordinal = 0),
            cancellable = true
    )
    private void onPopupMouseButton(long windowHandle, int button, int action, int modifiers, CallbackInfo ci) {
        if (this.minecraft.getOverlay() != null || !(this.minecraft.screen instanceof SlotQueryingScreen screen)) {
            return;
        }
        var window = this.minecraft.getWindow();
        double x = this.minecraft.mouseHandler.getScaledXPos(window);
        double y = this.minecraft.mouseHandler.getScaledYPos(window);
        boolean consumed = action == GLFW.GLFW_PRESS
                ? screen.whats_that_slot$mouseClicked(x, y, button)
                : action == GLFW.GLFW_RELEASE && screen.whats_that_slot$mouseReleased(x, y, button);
        if (consumed) {
            this.minecraft.screen.afterMouseAction();
            ci.cancel();
        }
    }

    @WrapOperation(
            method = "onScroll(JDD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z")
    )
    private boolean onMouseScroll(
            Screen screen, double mouseX, double mouseY, double scrollX, double scrollY,
            Operation<Boolean> original
    ) {
        if (screen instanceof SlotQueryingScreen queryingScreen
                && queryingScreen.whats_that_slot$mouseScrolled(scrollY)) {
            return true;
        }
        return original.call(screen, mouseX, mouseY, scrollX, scrollY);
    }
}
