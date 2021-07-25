package mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.poi.PointOfInterestType;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CompassItem.class)
public class CompassItemMixin extends Item implements Vanishable
{
    public CompassItemMixin(Settings settings)
    {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void targetStructure(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)
    {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();

        BlockState state = world.getBlockState(blockPos);

        BlockPos destination = null;

        if (world instanceof ServerWorld)
        {
            ServerWorld serverWorld = (ServerWorld) world;

            DimensionType dimension = world.getDimension();

            if (dimension.isBedWorking())
            {
                if (state.isOf(Blocks.BELL))
                    destination = serverWorld.locateStructure(StructureFeature.VILLAGE, blockPos, 100, true);
                if (state.isOf(Blocks.SPONGE) || state.isOf(Blocks.WET_SPONGE))
                    destination = serverWorld.locateStructure(StructureFeature.MONUMENT, blockPos, 100, true);
            }

            if (dimension.isUltrawarm())
            {
                if (state.isOf(Blocks.CRYING_OBSIDIAN))
                    destination = serverWorld.locateStructure(StructureFeature.FORTRESS, blockPos, 100, true);
                else if (state.isOf(Blocks.GILDED_BLACKSTONE))
                    destination = serverWorld.locateStructure(StructureFeature.BASTION_REMNANT, blockPos, 100, true);
            }

            if (dimension.hasEnderDragonFight() && state.isOf(Blocks.DRAGON_HEAD))
            {
                destination = serverWorld.locateStructure(StructureFeature.END_CITY, blockPos, 100, true);
            }

            if (destination != null)
            {
                serverWorld.getPointOfInterestStorage().add(destination, PointOfInterestType.LODESTONE);

                world.playSound(null, blockPos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0F, 1.0F);

                PlayerEntity playerEntity = context.getPlayer();
                ItemStack itemStack = context.getStack();
                boolean notCreativeAndSingleItem = !playerEntity.getAbilities().creativeMode && itemStack.getCount() == 1;

                if (notCreativeAndSingleItem)
                {
                    writeNbt(world.getRegistryKey(), destination, itemStack.getOrCreateNbt());
                }

                else
                {
                    ItemStack itemStack2 = new ItemStack(Items.COMPASS, 1);
                    NbtCompound nbtCompound = itemStack.hasNbt() ? itemStack.getNbt().copy() : new NbtCompound();
                    itemStack2.setNbt(nbtCompound);

                    if (!playerEntity.getAbilities().creativeMode)
                    {
                        itemStack.decrement(1);
                    }

                    writeNbt(world.getRegistryKey(), destination, nbtCompound);

                    if (!playerEntity.getInventory().insertStack(itemStack2)) {
                        playerEntity.dropItem(itemStack2, false);
                    }
                }

                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }

    @Shadow
    private void writeNbt(RegistryKey<World> worldKey, BlockPos pos, NbtCompound nbt) {}
}
