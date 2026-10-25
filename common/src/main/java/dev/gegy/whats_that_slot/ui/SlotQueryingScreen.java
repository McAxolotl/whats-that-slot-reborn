package dev.gegy.whats_that_slot.ui;

import javax.annotation.Nullable;

public interface SlotQueryingScreen {
    boolean whats_that_slot$mouseClicked(double x, double y, int button);

    boolean whats_that_slot$mouseReleased(double x, double y, int button);

    boolean whats_that_slot$mouseScrolled(double amount);

    @Nullable
    HoveredItem whats_that_slot$getHoveredItemAt(double x, double y);

    @Nullable
    Bounds2i whats_that_slot$getQueryBounds();
}
