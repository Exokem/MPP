package mixin;

import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import world.LargeOakTreeSaplingGenerator;

import java.util.Random;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin extends PlantBlock implements Fertilizable
{
    @Shadow
    public static IntProperty STAGE;

    @Shadow
    private SaplingGenerator generator;

    protected SaplingBlockMixin(Settings settings)
    {
        super(settings);
    }

    @Shadow
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient)
    {
        return false;
    }

    @Shadow
    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state)
    {
        return false;
    }

    @Shadow
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state)
    {

    }

    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random)
    {
        if (state.get(STAGE) == 0)
        {
            world.setBlockState(pos, state.cycle(STAGE), 4);
        }

        else
        {
            if (state.getBlock().equals(Blocks.OAK_SAPLING))
            {
                LargeOakTreeSaplingGenerator.GENERATOR.generate
                    (
                        world, world.getChunkManager().getChunkGenerator(), pos, state, random
                    );
            }

            else this.generator.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
        }
    }
}
