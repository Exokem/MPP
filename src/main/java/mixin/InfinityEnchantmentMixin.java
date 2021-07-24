package mixin;

import net.minecraft.enchantment.*;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfinityEnchantment.class)
public class InfinityEnchantmentMixin extends Enchantment
{
    public InfinityEnchantmentMixin(Rarity weight, EquipmentSlot... slotTypes)
    {
        super(weight, EnchantmentTarget.BOW, slotTypes);
    }

    @Inject(method = "canAccept(Lnet/minecraft/enchantment/Enchantment;)Z", at = @At("HEAD"), cancellable = true)
    public void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(super.canAccept(other));
    }
}
