/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.integration.jei.recipes;

import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleRecipe;
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class CrucibleRecipeCategory<T extends CrucibleRecipe> implements IRecipeCategory<T> {

    //region Fields
    private final IDrawable background;
    private final IDrawable icon;
    private final ItemStack construct = new ItemStack(BlockRegistry.CRUCIBLE.get());
    private final ItemStack stirrer;
    //endregion Fields

    //region Initialization
    public CrucibleRecipeCategory(IGuiHelper guiHelper, ItemStack stirrer) {
        this.background = guiHelper.createBlankDrawable(168, 86);
        this.stirrer = stirrer;
        this.icon = guiHelper.createDrawableIngredient(this.construct);
        this.construct.getOrCreateTag().putBoolean("RenderFull", true);
        if(this.stirrer != ItemStack.EMPTY)
            this.stirrer.getOrCreateTag().putBoolean("RenderFull", true);
    }
    //endregion Initialization

    //region Overrides

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(T recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
        int index = 0;
        int iconWidth = this.icon.getWidth();
        int x = this.background.getWidth() / 2 - iconWidth / 2;
        int y = 0;

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

        //draw trigger ingredient
        recipeLayout.getItemStacks().init(index, true, x, y);
        recipeLayout.getItemStacks().set(index, inputs.get(0));
        index++;
        y += 20;

        //draw essentia ingredients
        inputs.remove(0); //remove trigger ingredient
        int essentiaSlotOffset = 10;
        int essentiaX = x;
        if (inputs.size() > 1)
            essentiaX = essentiaX - (inputs.size() * (iconWidth + essentiaSlotOffset)) / 2 +
                        (iconWidth + essentiaSlotOffset) / 2;
        for (int i = 0; i < inputs.size(); i++) {
            recipeLayout.getItemStacks().init(index, false, essentiaX + i * (iconWidth + essentiaSlotOffset), y);
            recipeLayout.getItemStacks().set(index, inputs.get(i));
            index++;
        }
        y += 20;

        //draw crucible
        recipeLayout.getItemStacks().init(index, true, x, y);
        recipeLayout.getItemStacks().set(index, this.construct);
        index++;
        y += 20;

        //draw stirrer in upper left corner
        recipeLayout.getItemStacks().init(index, true, 0, 0);
        recipeLayout.getItemStacks().set(index, this.stirrer);
        index++;

        //draw output
        recipeLayout.getItemStacks().init(index, false, x, y);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    //endregion Overrides
}
