package mixin.grindstone;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import utility.SlotOverrides;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin extends ScreenHandler
{
    protected GrindstoneScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId)
    {
        super(type, syncId);
    }

    @ModifyArg
    (
        method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"), index = 0
    )
    private Slot replaceSlots(Slot intercepted)
    {
        if (intercepted.inventory instanceof PlayerInventory) return intercepted;

        return switch (intercepted.getIndex())
            {
                case 0 -> SlotOverrides.input1(intercepted.inventory, intercepted.getIndex(), intercepted.x, intercepted.y);
                case 1 -> SlotOverrides.input2(intercepted.inventory, intercepted.getIndex(), intercepted.x, intercepted.y);
                case 2 -> SlotOverrides.result(context, input, intercepted.inventory, intercepted.getIndex(), intercepted.x, intercepted.y);
                default -> intercepted;
            };
    }

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void injectBookResult(CallbackInfo ci)
    {
        ItemStack main = input.getStack(0), book = input.getStack(1);

        if (!main.isEmpty() && main.hasEnchantments() && book.isOf(Items.BOOK) && book.getCount() == 1 && !book.hasEnchantments())
        {
            ItemStack resultStack = this.grind(main, main.getDamage(), main.getCount());
            this.result.setStack(0, resultStack);
            ci.cancel();
        }
    }

    @Final
    @Shadow
    private ScreenHandlerContext context;

    @Final
    @Shadow
    Inventory input;

    @Final
    @Shadow
    private Inventory result;

    @Shadow
    private ItemStack grind(ItemStack item, int damage, int amount)
    {
        return null;
    }

    @Shadow
    public boolean canUse(PlayerEntity player)
    {
        return false;
    }
}
