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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import world.LargeOakTreeSaplingGenerator;

import java.util.Random;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin
{
    @Inject
    (
        method = "generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Ljava/util/Random;)V",
        at = @At("HEAD"), cancellable = true
    )
    public void injectGenerate(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfo ci)
    {
        if (state.get(SaplingBlock.STAGE) != 0 && state.getBlock().equals(Blocks.OAK_SAPLING))
        {
            LargeOakTreeSaplingGenerator.GENERATOR.generate
                (
                    world, world.getChunkManager().getChunkGenerator(), pos, state, random
                );

            ci.cancel();
        }
    }
}
