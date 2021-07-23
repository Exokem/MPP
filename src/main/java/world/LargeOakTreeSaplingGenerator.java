package world;

import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.MegaJungleTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.Random;

public class LargeOakTreeSaplingGenerator extends LargeTreeSaplingGenerator
{
    private static final class InternalFeature
    {
        public static final TreeFeatureConfig LARGE_OAK_CONFIG = new TreeFeatureConfig.Builder
            (
                new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
                new GreatOakTrunkPlacer(12, 2, 1),
                new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.OAK_SAPLING.getDefaultState()),
                new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                new ThreeLayersFeatureSize(1, 4, 0, 1, 2,OptionalInt.empty())
            ).ignoreVines().build();

        public static final ConfiguredFeature<TreeFeatureConfig, ?> LARGE_OAK = register("large_oak", Feature.TREE.configure(LARGE_OAK_CONFIG));

        private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
            return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
        }
    }

    public static final LargeOakTreeSaplingGenerator GENERATOR = new LargeOakTreeSaplingGenerator();

    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees)
    {
        if (random.nextInt(10) == 0) return bees ? ConfiguredFeatures.FANCY_OAK_BEES_005 : ConfiguredFeatures.FANCY_OAK;
        else return bees ? ConfiguredFeatures.OAK_BEES_005 : ConfiguredFeatures.OAK;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getLargeTreeFeature(Random random)
    {
        return InternalFeature.LARGE_OAK;
    }
}
