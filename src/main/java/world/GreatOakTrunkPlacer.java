package world;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class GreatOakTrunkPlacer extends TrunkPlacer
{
    private final class GenerationContext
    {
        private GenerationContext(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int heightLimit, BlockPos startPos, TreeFeatureConfig config)
        {
            this.world = world;
            this.replacer = replacer;
            this.random = random;
            this.heightLimit = heightLimit;
            this.startPos = startPos;
            this.config = config;
        }

        private TestableWorld world;
        private BiConsumer<BlockPos, BlockState> replacer;
        private Random random;
        private final int heightLimit;
        private final BlockPos startPos;
        private final TreeFeatureConfig config;

        private void breakGrass()
        {
            BlockPos blockPos = startPos.down();
            setToDirt(world, replacer, random, blockPos, config);
            setToDirt(world, replacer, random, blockPos.east(), config);
            setToDirt(world, replacer, random, blockPos.south(), config);
            setToDirt(world, replacer, random, blockPos.south().east(), config);
        }

        private void generateTrunk()
        {
            int startY = startPos.getY();
            int placeX = startPos.getX(), placeZ = startPos.getZ();

            for(int height = 0; height < heightLimit - 1; ++ height)
            {
                int placeY = startY + height;
                BlockPos blockPos2 = new BlockPos(placeX, placeY, placeZ);

                if (TreeFeature.isAirOrLeaves(world, blockPos2))
                {
                    getAndSetState(world, replacer, random, blockPos2, config);
                    getAndSetState(world, replacer, random, blockPos2.east(), config);
                    getAndSetState(world, replacer, random, blockPos2.south(), config);
                    getAndSetState(world, replacer, random, blockPos2.east().south(), config);
                }
            }
        }

        private List<FoliagePlacer.TreeNode> generateFoliage()
        {
            List<FoliagePlacer.TreeNode> list = Lists.newArrayList();

            int startX = startPos.getX(), startY = startPos.getY(), startZ = startPos.getZ();
            int p = startY + heightLimit - 1;
            int height, placeY;

            list.add(new FoliagePlacer.TreeNode(new BlockPos(startX, p, startZ), 0, true));

            for(height = -1; height <= 2; ++ height)
            {
                for(placeY = -1; placeY <= 2; ++ placeY)
                {
                    if ((height < 0 || height > 1 || placeY < 0 || placeY > 1) && random.nextInt(3) <= 0)
                    {
                        int u = random.nextInt(3) + 2;

                        for(int v = 0; v < u; ++ v)
                        {
                            getAndSetState(world, replacer, random, new BlockPos(startX + height, p - v - 1, startZ + placeY), config);
                        }

                        list.add(new FoliagePlacer.TreeNode(new BlockPos(startX + height, p, startZ + placeY), 0, false));
                    }
                }
            }

            return list;
        }
    }

    public GreatOakTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
    {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    private static final LargeOakTrunkPlacer test = new LargeOakTrunkPlacer(3, 11, 0);

    @Override
    protected TrunkPlacerType<?> getType()
    {
        return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int heightLimit, BlockPos startPos, TreeFeatureConfig config)
    {
        GenerationContext context = new GenerationContext(world, replacer, random, heightLimit, startPos, config);

        List<FoliagePlacer.TreeNode> nodes = Lists.newArrayList();

        context.breakGrass();
        context.generateTrunk();

        nodes.addAll(context.generateFoliage());
        nodes.addAll(test.generate(world, replacer, random, heightLimit, startPos, config));

        return nodes;
    }
}
