package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.block.CrucibleBlock;
import com.github.klikli_dev.theurgy.common.block.crystal.CrystalBlock;
import com.github.klikli_dev.theurgy.common.block.crystal.ICrystalSpreadHandler;
import com.github.klikli_dev.theurgy.common.block.crystal.PureCrystalCondition;
import com.github.klikli_dev.theurgy.common.block.crystal.PureCrystalSpreader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockRegistry {
    //region Fields
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Theurgy.MODID);
    public static final Map<ResourceLocation, BlockDataGenSettings> BLOCK_DATA_GEN_SETTINGS = new HashMap<>();

    //Machines
    public static final RegistryObject<CrucibleBlock> CRUCIBLE =
            register("crucible", () -> new CrucibleBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(2.0F).notSolid()));

    //Resources
    public static final ICrystalSpreadHandler PURE_CRYSTAL_SPREADER = new PureCrystalSpreader(
            new PureCrystalCondition(),
            new PureCrystalCondition(), //TODO: provide the correct spread conditions
            new PureCrystalCondition(),
            new PureCrystalCondition(),
            new PureCrystalCondition(),
            new PureCrystalCondition()
    );
    public static final RegistryObject<CrystalBlock> PURE_CRYSTAL = register("pure_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f).tickRandomly()
            , PURE_CRYSTAL_SPREADER) {
    });
    //TODO: provide the correct spread handler
    public static final RegistryObject<CrystalBlock> PRIMA_MATERIA_CRYSTAL = register("prima_materia_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f).tickRandomly(),
            null) {
    });

    //endregion Fields

    //region Static Methods
    public static <I extends Block> RegistryObject<I> register(final String name, final Supplier<? extends I> sup) {
        return register(name, sup, true);
    }

    public static <I extends Block> RegistryObject<I> register(final String name, final Supplier<? extends I> sup,
                                                               boolean generateDefaultBlockItem) {
        return register(name, sup, generateDefaultBlockItem, LootTableType.DROP_SELF);
    }

    public static <I extends Block> RegistryObject<I> register(final String name, final Supplier<? extends I> sup,
                                                               boolean generateDefaultBlockItem,
                                                               LootTableType lootTableType) {
        RegistryObject<I> object = BLOCKS.register(name, sup);
        BLOCK_DATA_GEN_SETTINGS.put(object.getId(), new BlockDataGenSettings(generateDefaultBlockItem, lootTableType));
        return object;
    }
    //endregion Static Methods

    public enum LootTableType {
        EMPTY,
        DROP_SELF
    }

    public static class BlockDataGenSettings {
        //region Fields
        public boolean generateDefaultBlockItem;
        public LootTableType lootTableType;
        //endregion Fields

        //region Initialization
        public BlockDataGenSettings(boolean generateDefaultBlockItem,
                                    LootTableType lootTableType) {
            this.generateDefaultBlockItem = generateDefaultBlockItem;
            this.lootTableType = lootTableType;
        }
        //endregion Initialization
    }
}
