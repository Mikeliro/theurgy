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

package com.github.klikli_dev.theurgy.integration.jei;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.PurificationRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.ReplicationRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.TransmutationRecipe;
import com.github.klikli_dev.theurgy.integration.jei.recipes.EssentiaRecipeCategory;
import com.github.klikli_dev.theurgy.integration.jei.recipes.PurificationRecipeCategory;
import com.github.klikli_dev.theurgy.integration.jei.recipes.ReplicationRecipeCategory;
import com.github.klikli_dev.theurgy.integration.jei.recipes.TransmutationRecipeCategory;
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.github.klikli_dev.theurgy.registry.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    //region Overrides
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Theurgy.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EssentiaRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PurificationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ReplicationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new TransmutationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipeManager = world.getRecipeManager();

        List<EssentiaRecipe> essentiaRecipes = recipeManager.getRecipesForType(RecipeRegistry.ESSENTIA_TYPE.get());
        registration.addRecipes(essentiaRecipes, RecipeRegistry.ESSENTIA.getId());
        List<PurificationRecipe> purificationRecipes =
                recipeManager.getRecipesForType(RecipeRegistry.PURIFICATION_TYPE.get());
        registration.addRecipes(purificationRecipes, RecipeRegistry.PURIFICATION.getId());
        List<ReplicationRecipe> replicationRecipes =
                recipeManager.getRecipesForType(RecipeRegistry.REPLICATION_TYPE.get());
        registration.addRecipes(replicationRecipes, RecipeRegistry.REPLICATION.getId());
        List<TransmutationRecipe> transmutationRecipes =
                recipeManager.getRecipesForType(RecipeRegistry.TRANSMUTATION_TYPE.get());
        registration.addRecipes(transmutationRecipes, RecipeRegistry.TRANSMUTATION.getId());

        //Register ingredient info
        this.registerIngredientInfo(registration, BlockRegistry.PRIMA_MATERIA_CRYSTAL.get());
        this.registerIngredientInfo(registration,BlockRegistry.AER_CRYSTAL.get());
        this.registerIngredientInfo(registration,BlockRegistry.AQUA_CRYSTAL.get());
        this.registerIngredientInfo(registration,BlockRegistry.IGNIS_CRYSTAL.get());
        this.registerIngredientInfo(registration,BlockRegistry.TERRA_CRYSTAL.get());
        this.registerIngredientInfo(registration, ItemRegistry.END_INFUSED_CRYSTAL_SWORD.get());
    }

    public void registerIngredientInfo(IRecipeRegistration registration, IItemProvider ingredient){
        registration.addIngredientInfo(new ItemStack(ingredient.asItem()), VanillaTypes.ITEM,
                "jei."+ Theurgy.MODID + ".ingredient."+ingredient.asItem().getRegistryName().getPath()+".description");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CRUCIBLE.get()),
                RecipeRegistry.ESSENTIA.getId());
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CRUCIBLE.get()),
                RecipeRegistry.PURIFICATION.getId());
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CRUCIBLE.get()),
                RecipeRegistry.REPLICATION.getId());
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CRUCIBLE.get()),
                RecipeRegistry.TRANSMUTATION.getId());
    }

    //endregion Overrides

}
