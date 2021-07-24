package mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.enchantment.ProtectionEnchantment.Type.*;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin extends Enchantment
{
    @Shadow
    @Final
    public ProtectionEnchantment.Type protectionType;

    public ProtectionEnchantmentMixin(Rarity weight, ProtectionEnchantment.Type protectionType, EquipmentSlot... slotTypes)
    {
        super(weight, protectionType == ProtectionEnchantment.Type.FALL ? EnchantmentTarget.ARMOR_FEET : EnchantmentTarget.ARMOR, slotTypes);
    }

    @Inject(method = "getMaxLevel()I", at = @At("RETURN"), cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
    {
        cir.setReturnValue(5);
    }

    @Inject(method = "canAccept(Lnet/minecraft/enchantment/Enchantment;)Z", at = @At("HEAD"), cancellable = true)
    public void acceptInject(Enchantment other, CallbackInfoReturnable<Boolean> cir)
    {
        if (other instanceof ProtectionEnchantment)
        {
            ProtectionEnchantment.Type type = ((ProtectionEnchantment) other).protectionType;

            if (this.protectionType == type) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(this.protectionType == FALL || type == FALL || this.protectionType == ALL || type == ALL);
            }
        }
    }
}
