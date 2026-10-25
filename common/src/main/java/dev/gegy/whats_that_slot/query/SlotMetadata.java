package dev.gegy.whats_that_slot.query;

import dev.gegy.whats_that_slot.query.recipe.RecipeItemResults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import javax.annotation.Nullable;

public final class SlotMetadata {
    @Nullable
    public static Iterable<ItemStack> getCustomItems(Minecraft client, AbstractContainerScreen<?> screen, Slot slot) {
        if (client.level == null || client.player == null) {
            return null;
        }
        var category = getRecipeCategoryForSlot(screen, slot);
        if (category != null) {
            return new RecipeItemResults(client.player.getRecipeBook(), SlotDisplayContext.fromLevel(client.level), category);
        }
        return null;
    }

    @Nullable
    private static SearchRecipeBookCategory getRecipeCategoryForSlot(AbstractContainerScreen<?> screen, Slot slot) {
        if (slot instanceof ResultSlot) {
            return SearchRecipeBookCategory.CRAFTING;
        } else if (slot instanceof FurnaceResultSlot) {
            var menu = screen.getMenu();
            if (menu instanceof AbstractFurnaceMenu furnace) {
                return switch (furnace.getRecipeBookType()) {
                    case FURNACE -> SearchRecipeBookCategory.FURNACE;
                    case SMOKER -> SearchRecipeBookCategory.SMOKER;
                    case BLAST_FURNACE -> SearchRecipeBookCategory.BLAST_FURNACE;
                    default -> null;
                };
            }
        }
        return null;
    }
}
