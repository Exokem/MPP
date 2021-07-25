package mixin.grindstone;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import utility.SlotOverrides;

import java.util.Iterator;
import java.util.Map;

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

//    @Inject
//    (
//        method = "updateResult()V",
//        slice = @Slice
//            (
//                from = @At(value = "TAIL", target = "sendContentUpdates"),
//                to = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
//            ),
//        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")
//    )
//    private void allowBooks(CallbackInfo ci)
//    {
//        System.out.println("DEBUG TEST");
//    }

    @Final
    @Shadow
    private ScreenHandlerContext context;

    @Final
    @Shadow
    Inventory input;

    @Final
    @Shadow
    private Inventory result;

    private void updateResult() {
        ItemStack itemStack = this.input.getStack(0);
        ItemStack itemStack2 = this.input.getStack(1);
        boolean bl = !itemStack.isEmpty() || !itemStack2.isEmpty();
        boolean bl2 = !itemStack.isEmpty() && !itemStack2.isEmpty();
        if (!bl) {
            this.result.setStack(0, ItemStack.EMPTY);
        } else {
            boolean bl3 = !itemStack.isEmpty() && !itemStack.isOf(Items.ENCHANTED_BOOK) && !itemStack.hasEnchantments() || !itemStack2.isEmpty() && !itemStack2.isOf(Items.ENCHANTED_BOOK) && !itemStack2.hasEnchantments();
            if (itemStack.getCount() > 1 || itemStack2.getCount() > 1 || !bl2 && bl3) {
                this.result.setStack(0, ItemStack.EMPTY);
                this.sendContentUpdates();
                return;
            }

            boolean allowDefault = !itemStack2.isOf(Items.BOOK) || itemStack2.hasEnchantments();

            int i = 1;
            int m;
            ItemStack itemStack3;
            if (bl2) {

                if (allowDefault)
                {
                    if (!itemStack.isOf(itemStack2.getItem())) {
                        this.result.setStack(0, ItemStack.EMPTY);
                        this.sendContentUpdates();
                        return;
                    }
                }

                Item item = itemStack.getItem();
                int j = item.getMaxDamage() - itemStack.getDamage();
                int k = item.getMaxDamage() - itemStack2.getDamage();
                int l = j + k + item.getMaxDamage() * 5 / 100;
                m = Math.max(item.getMaxDamage() - l, 0);
                itemStack3 = this.transferEnchantments(itemStack, itemStack2);

                if (allowDefault)
                {
                    if (!itemStack3.isDamageable())
                    {
                        if (!ItemStack.areEqual(itemStack, itemStack2))
                        {
                            this.result.setStack(0, ItemStack.EMPTY);
                            this.sendContentUpdates();
                            return;
                        }

                        i = 2;
                    }
                }
            } else {
                boolean bl4 = !itemStack.isEmpty();
                m = bl4 ? itemStack.getDamage() : itemStack2.getDamage();
                itemStack3 = bl4 ? itemStack : itemStack2;
            }

            this.result.setStack(0, this.grind(itemStack3, m, i));
        }

        this.sendContentUpdates();
    }

    @Shadow
    private ItemStack transferEnchantments(ItemStack target, ItemStack source)
    {
        return null;
    }

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
