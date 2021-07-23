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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin extends Enchantment
{
    @Shadow
    public final ProtectionEnchantment.Type protectionType;

    public ProtectionEnchantmentMixin(Rarity weight, ProtectionEnchantment.Type protectionType, EquipmentSlot... slotTypes)
    {
        super(weight, protectionType == ProtectionEnchantment.Type.FALL ? EnchantmentTarget.ARMOR_FEET : EnchantmentTarget.ARMOR, slotTypes);
        this.protectionType = protectionType;
    }

    @Inject(method = "getMaxLevel()I", at = @At("RETURN"), cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> cir)
    {
        cir.setReturnValue(5);
    }

    public boolean canAccept(Enchantment other)
    {
        if (other instanceof ProtectionEnchantment) {
            ProtectionEnchantment protectionEnchantment = (ProtectionEnchantment) other;
            if (this.protectionType == protectionEnchantment.protectionType) {
                return false;
            } else {
                return this.protectionType == ProtectionEnchantment.Type.FALL || protectionEnchantment.protectionType == ProtectionEnchantment.Type.FALL
                    || this.protectionType == ProtectionEnchantment.Type.ALL || protectionEnchantment.protectionType == ProtectionEnchantment.Type.ALL;
            }
        } else {
            return super.canAccept(other);
        }
    }
}
