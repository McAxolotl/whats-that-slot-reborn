package dev.gegy.whats_that_slot.ui;

import com.mojang.blaze3d.platform.InputConstants;

public final class SlotQueryInput {
    public static final int REQUEST_TICKS = 20;
    private static final int KEY = InputConstants.KEY_LALT;

    public static boolean isRequestingQuery() {
        return InputConstants.isKeyDown(KEY);
    }
}
