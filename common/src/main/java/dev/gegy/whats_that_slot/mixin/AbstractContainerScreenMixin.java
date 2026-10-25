package dev.gegy.whats_that_slot.mixin;

import dev.gegy.whats_that_slot.ui.HoveredItem;
import dev.gegy.whats_that_slot.ui.Bounds2i;
import dev.gegy.whats_that_slot.ui.SlotQueryInput;
import dev.gegy.whats_that_slot.ui.SlotQueryingScreen;
import dev.gegy.whats_that_slot.ui.state.SlotQueryController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin implements SlotQueryingScreen {
    @Shadow @Nullable protected Slot hoveredSlot;
    @Shadow @Final protected AbstractContainerMenu menu;

    @Unique private final SlotQueryController queryController = new SlotQueryController(
            (AbstractContainerScreen<?>) (Object) this
    );
    @Unique private Slot querySlot;

    @Inject(method = "init()V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.queryController.reset();
        this.querySlot = null;
    }

    @Inject(method = "renderContents(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("RETURN"))
    private void captureAndClearFocusedSlot(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.querySlot = this.hoveredSlot;
        if (this.queryController.isActive()) {
            this.hoveredSlot = null;
        }
    }

    @Inject(method = "containerTick()V", at = @At("HEAD"))
    private void tickQuery(CallbackInfo ci) {
        var window = Minecraft.getInstance().getWindow();
        var carriedItem = this.menu.getCarried();
        this.queryController.tick(this.querySlot, SlotQueryInput.isRequestingQuery(window) && carriedItem.isEmpty());
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("HEAD"))
    private void drawQueryDisplay(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        float delta = Minecraft.getInstance().getDeltaTracker().getRealtimeDeltaTicks();
        this.queryController.draw(graphics, mouseX, mouseY, delta);
    }

    @Inject(method = "isHovering(Lnet/minecraft/world/inventory/Slot;DD)Z", at = @At("HEAD"), cancellable = true)
    private void disableItemHover(Slot slot, double pointX, double pointY, CallbackInfoReturnable<Boolean> ci) {
        var result = this.queryController.isSlotSelected(slot, pointX, pointY);
        if (result != InteractionResult.PASS) {
            ci.setReturnValue(result == InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> ci) {
        if (this.whats_that_slot$mouseClicked(event.x(), event.y(), event.button())) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method = "mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z", at = @At("HEAD"), cancellable = true)
    private void mouseDragged(MouseButtonEvent event, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> ci) {
        if (this.queryController.mouseDragged(event.x(), event.y(), event.button(), deltaX, deltaY)) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method = "mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z", at = @At("HEAD"), cancellable = true)
    private void mouseReleased(MouseButtonEvent event, CallbackInfoReturnable<Boolean> ci) {
        if (this.whats_that_slot$mouseReleased(event.x(), event.y(), event.button())) {
            ci.setReturnValue(true);
        }
    }

    @Override
    public boolean whats_that_slot$mouseClicked(double x, double y, int button) {
        return this.queryController.mouseClicked(x, y, button);
    }

    @Override
    public boolean whats_that_slot$mouseReleased(double x, double y, int button) {
        return this.queryController.mouseReleased(x, y, button);
    }

    @Override
    public boolean whats_that_slot$mouseScrolled(double amount) {
        return this.queryController.mouseScrolled(amount);
    }

    @Nullable
    @Override
    public HoveredItem whats_that_slot$getHoveredItemAt(double x, double y) {
        return this.queryController.getHoveredItemAt(x, y);
    }

    @Nullable
    @Override
    public Bounds2i whats_that_slot$getQueryBounds() {
        return this.queryController.getQueryBounds();
    }
}
