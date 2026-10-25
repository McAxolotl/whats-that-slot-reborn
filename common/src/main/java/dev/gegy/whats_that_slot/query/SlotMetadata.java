package dev.gegy.whats_that_slot.query;

import dev.gegy.whats_that_slot.mixin.AbstractFurnaceMenuAccess;
import dev.gegy.whats_that_slot.query.recipe.RecipeItemResults;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
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
            if (menu instanceof AbstractFurnaceMenuAccess furnace) {
                var recipeType = furnace.getRecipeType();
                if (recipeType == RecipeType.SMELTING) {
                    return SearchRecipeBookCategory.FURNACE;
                } else if (recipeType == RecipeType.SMOKING) {
                    return SearchRecipeBookCategory.SMOKER;
                } else if (recipeType == RecipeType.BLASTING) {
                    return SearchRecipeBookCategory.BLAST_FURNACE;
                }
            }
        }
        return null;
    }
}
